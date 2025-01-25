#include "../include/keyboardInput.h"
#include "../include/StompProtocol.h"
#include "../include/event.h"
#include "../include/StompClient.h"  // Include the header file for the class
#include <fstream>
#include <iostream>
#include <thread>
#include <cstdlib>
#include <stdexcept>
#include <queue>
#include <chrono>
#include <list>
#include <sstream>
using namespace std;

// map<string, int> StompProtocol::subscriptions;
// bool StompProtocol::loggedIn = false;
// queue<string> StompProtocol::frameQueue;
// mutex StompProtocol::queueMutex;
// unique_ptr<ConnectionHandler> StompProtocol::connectionHandler;
// string StompProtocol::username;
// list<StompClient::User> StompProtocol::users;
StompProtocol::StompProtocol(mutex& newqueueMutex, queue<string>& frameQueue):
frameQueue(frameQueue), queueMutex(newqueueMutex), subscriptions(), loggedIn(false),
connectionHandler(), username(),users(){}

ConnectionHandler* StompProtocol::getConnectionHandler() {
    return connectionHandler.get();
}

bool StompProtocol::login(string line){
    string command, host, portString, hostWithPort, password;
    istringstream iss(line);
    iss >> command >> hostWithPort >> username >> password;
    if(password.empty()){
        return false;
    }
    User newUser;
    newUser.username = username;
    users.push_back(newUser);
    int colonPosition = hostWithPort.find(':');
    host = hostWithPort.substr(0, colonPosition);
    portString = hostWithPort.substr(colonPosition+1);
    short port = static_cast<short>(std::stoi(portString));
    connectionHandler = std::unique_ptr<ConnectionHandler>(new ConnectionHandler(host, port));
    return getConnectionHandler()->connect();
}

int StompProtocol::idGenerator() {
    static int id = 1;
    return id++;
}

int StompProtocol::receiptIdGenerator(){
    static int receiptId = 1;
    return receiptId++;
}

// void StompProtocol::startKeyboardInput() {
//     keyboardInput keyboardInput(queueMutex, frameQueue);
//     keyboardInput.start();
// }

void StompProtocol::processCommand(const string& line) {
    istringstream iss(line);
    string command;
    iss >> command;
    
    if(command != "login" && !loggedIn){
        cerr << "Please log in first" << endl;
    }
    else if(command == "login" && loggedIn){
        cerr << "The client is already logged in, log out before trying again" << endl;
    }
    else if(command == "login"){
        bool connected = login(line);
        if(!connected)
            cerr << "Could not connect to server. Make sure you typed correctly!" << endl;
        else{
            loggedIn = true;
            string connectFrame = createConnectFrame(line);
            if (!getConnectionHandler()->sendLine(connectFrame)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                return;
            }
            string answer;
            getConnectionHandler()->getLine(answer);
            iss.clear();
            iss.str(answer);
            string connected;
            iss >> connected;
            if(connected == "CONNECTED"){
                cout << "Login successful" << endl;
            }
        }
    }
    // Further command handling (join, exit, etc.)
    else if(command == "join"){
        string subscribeFrame = createSubscribeFrame(line);
        getConnectionHandler()->sendLine(subscribeFrame);
        string answer;
        getConnectionHandler()->getLine(answer);
        // Additional handling if necessary
    }
    else if(command == "exit"){
        string unsubscribeFrame = createUnsubscribeFrame(line);
        getConnectionHandler()->sendLine(unsubscribeFrame);
        string answer;
        getConnectionHandler()->getLine(answer);
        // Additional handling if necessary
    }
    else if(command == "report"){
        handleReportCommand(line);
    }
    else if(command == "summary"){
        handleSummaryCommand(line);
    }
    else{
        string logoutFrame = createLogoutFrame();
        getConnectionHandler()->sendLine(logoutFrame);
        string answer;
        getConnectionHandler()->getLine(answer);
        getConnectionHandler()->close();
        connectionHandler.reset();
        loggedIn = false;
        subscriptions.clear();
        frameQueue = queue<string>();
    }
}

string StompProtocol::createConnectFrame(string line) {
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

string StompProtocol::createSubscribeFrame(string line) {
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

string StompProtocol::createUnsubscribeFrame(string line) {
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

string StompProtocol::createSendFrame(Event event) {
    auto mapToString = [](const map<string,string>& general_information) {
        ostringstream oss;
        for (const auto& [key, value] : general_information) {
            oss << "     " << key << ":" << value;
        }
        return oss.str();
    };
    return string("SEND\n") +
    "destination:/" + event.get_channel_name() + "\n\n" +
    "user:" + username + "\n" +
    "city:" + event.get_city() + "\n" +
    "event name:" + event.get_name() + "\n" +
    "date time:" + to_string(event.get_date_time()) + "\n" +
    "general information:\n" + mapToString(event.get_general_information()) + "\n" +
    "description:\n" + event.get_description() + "\n" + 
    string(1,'\0');
}

string StompProtocol::createLogoutFrame() {
    return string("DISCONNECT\n") +
           "receipt:" + to_string(receiptIdGenerator()) + "\n\n" +
           string(1,'\0'); 
}

void StompProtocol::handleReportCommand(string line) {
    // Implement your report handling logic
}

void StompProtocol::handleSummaryCommand(string line) {
    // Implement your summary handling logic
}

void StompProtocol::sortEvents(vector<Event>& events) {
    sort(events.begin(), events.end(), [](const Event& a, const Event& b) {
        if (a.get_date_time() == b.get_date_time()) {
            return a.get_name() < b.get_name();
        }
        return a.get_date_time() < b.get_date_time();
    });
}

string StompProtocol::summarizeDescription(const string &description) {
    if (description.length() > 27) {
        return description.substr(0, 27) + "...";
    }
    return description;
}

string StompProtocol::epochToDate(int date_time) {
    time_t raw_time = date_time;
    struct tm * time_info;
    char buffer[80];

    time_info = localtime(&raw_time);
    strftime(buffer, sizeof(buffer), "%d/%m/%y %H:%M", time_info);
    return string(buffer);
}

// Main function to drive the client

