#include "../include/StompProtocol.h"
#include <sstream>

class StompProtocol {

    static int idGenerator() {
        static int id = 1;  // This is initialized only once
        return id++;
    }
    static int receiptIdGenerator(){
        static int receiptId = 1;  // This is initialized only once
        return receiptId++;
    }
    static map<string, int> subscriptions;

    static string createSendFrame(string destination, string message) {

    }

    static string createConnectFrame(string line) {
        istringstream iss(line);
        string command, hostWithPort, username, password, host, portString;
        iss >> command >> hostWithPort >> username >> password;
        int colonPosition = hostWithPort.find(':');
        host = hostWithPort.substr(0, colonPosition);
        portString = hostWithPort.substr(colonPosition+1);
        short port = static_cast<short>(stoi(portString));
        return string("CONNECT\n") +
               "accept-version:1.2\n" +
               "host:" + host + "\n" +
               "login:" + username + "\n" +
               "passcode:" + password + "\n\n" +
               string(1,'\0');
    }

    static string createSubscribeFrame(string line){
        istringstream iss(line);
        string command, destination;
        iss >> command >> destination;
        int id = idGenerator();
        subscriptions[destination] = id;
        int receiptId = receiptIdGenerator();
        return string("SUBSCRIBE\n") +
                "destination:/" + destination + "\n" +
                "id:" + to_string(id) + "\n" +
                "receipt:" + to_string(receiptId) + "\n\n" +
                string(1,'\0');
    }

    static string createUnsubscribeFrame(string line){
        istringstream iss(line);
        string command, channelName;
        iss >> command >> channelName;
        int idToDelete;
        for (auto it = subscriptions.begin(); it != subscriptions.end(); it++) {
            if (it->first == channelName) {
                idToDelete = it->second;
                subscriptions.erase(it);
                break;
            }
        }
        return string("UNSUBSCRIBE\n") +
                "id:" + to_string(idToDelete) + "\n" +
                "receipt:" + to_string(receiptIdGenerator()) + "\n\n" +
                string(1,'\0');
    }

    /////// TODO: REPORT FRAME, SUMMARY FRAME ///////

    static string createLogoutFrame(){
            return string("DISCONNECT\n") +
            "receipt:" + to_string(receiptIdGenerator()) + "\n\n" +
            string(1,'\0'); 
    }

    // Other frame creation methods (e.g., SUBSCRIBE, UNSUBSCRIBE, etc.) can be added here
}
