#include "../include/keyboardInput.h"
#include "../include/StompProtocol.h"
#include "../include/event.h"
#include <fstream>
#include <iostream>
#include <thread>
#include <cstdlib>
#include <stdexcept>
#include <queue>
#include <chrono>
#include <list>
using namespace std;

    
map<string, int> subscriptions;
bool loggedIn = false;
queue<string> frameQueue;
mutex queueMutex;
unique_ptr<ConnectionHandler> connectionHandler;
string username;
list<User> users;
struct User {
    string username;
    list<Channel> channels;
};
struct Channel {
    string channelName;
    vector<Event> events;
};

ConnectionHandler* getConnectionHandler() {
    return connectionHandler.get();
}

bool login(string line){
    string command, host, portString, hostWithPort, password;
    istringstream iss(line);
    iss >> command >> hostWithPort >> username >> password;
    User newUser;
    newUser.username = username;
    users.push_back(newUser);
    int colonPosition = hostWithPort.find(':');
    host = hostWithPort.substr(0, colonPosition);
    portString = hostWithPort.substr(colonPosition+1);
    short port = static_cast<short>(std::stoi(portString));
    // what if argc smaller than 3?
    connectionHandler = make_unique<ConnectionHandler>(host, port);
    return getConnectionHandler()->connect();
}
int idGenerator() {
    static int id = 1;  // This is initialized only once
    return id++;
}
int receiptIdGenerator(){
    static int receiptId = 1;  // This is initialized only once
    return receiptId++;
}

int main(int argc, char *argv[]) {

    keyboardInput keyboardInput(queueMutex, frameQueue);
    keyboardInput.start();

    while(true){


        if(!frameQueue.empty()){
            queueMutex.lock();
            string line = frameQueue.front();
            frameQueue.pop();
            queueMutex.unlock();
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
                    cerr << "Could not connect to server" << endl;
                    else{
                        loggedIn = true;
                        string connectFrame = createConnectFrame(line);
                        if (!getConnectionHandler()->sendLine(connectFrame)) {
                            // WHAT TO DO IF IT CANT READ LINE?
                                std::cout << "Disconnected. Exiting...\n" << std::endl;
                                break;
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
                        // Is there a chance it wont send back?
                        // WHAT TO DO WITH ANSWERS?

                    }
                }
            else if(command == "join"){
                string subscribeFrame = createSubscribeFrame(line);
                getConnectionHandler()->sendLine(subscribeFrame);
                string answer;
                getConnectionHandler()->getLine(answer);
                ///
            }
            else if(command == "exit"){
                string unsubscribeFrame = createUnsubscribeFrame(line);
                getConnectionHandler()->sendLine(unsubscribeFrame);
                string answer;
                getConnectionHandler()->getLine(answer);
                ///
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
                ///
                getConnectionHandler()->close();
                connectionHandler.reset();
                loggedIn = false;
                subscriptions.clear();
                frameQueue = queue<string>();

            }
        }
            // send frame to server
            // check for answer
            // what is reciept and summary? what exactly do i need to save?
            // when we close?
            // what to synchronize with mutex?
        else {
            this_thread::sleep_for(chrono::milliseconds(200));
        }
    }

}

string createConnectFrame(string line) {
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
string createSubscribeFrame(string line){
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
string createUnsubscribeFrame(string line){
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
string createSendFrame(Event event){
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
string createLogoutFrame(){
        return string("DISCONNECT\n") +
        "receipt:" + to_string(receiptIdGenerator()) + "\n\n" +
        string(1,'\0'); 
}
void handleReportCommand(string line){
    istringstream iss(line);
    string command, file;
    iss >> command >> file;
    names_and_events parsedData = parseEventsFile(file);
    string channelName = parsedData.channel_name;
    vector<Event> events = parsedData.events;
    bool foundChannel = false;
    for (User user : users) {
        if (user.username == username) {
            for(Channel channel : user.channels){
                if(channel.channelName == channelName){
                    foundChannel = true;
                    if(channel.events.empty()){
                        channel.events = events;
                    }
                    else{
                        for(Event event : events){
                            channel.events.push_back(event);
                        }
                    }
                    sortEvents(channel.events);
                    break;
                }
            }
            if(!foundChannel){
                Channel newChannel;
                newChannel.channelName = channelName;
                newChannel.events = events;
                sortEvents(newChannel.events);
                user.channels.push_back(newChannel);
            }
            break;
        }
    }

    for(Event event : events){
        string sendFrame = createSendFrame(event);
        getConnectionHandler()->sendLine(sendFrame);
    }

}
void handleSummaryCommand(string line){
    istringstream iss(line);
    string command, channelName, summaryUsername, file;
    iss >> command >> channelName >> summaryUsername >> file;

    int total_reports = 0;
    int active_count = 0;
    int forces_arrival_count = 0;
    
    vector<string> report_lines;
    vector<Event> events;

    for(User user : users){
        if(user.username == summaryUsername){
            for(Channel channel : user.channels){
                if(channel.channelName == channelName){
                    events = channel.events;
                    break;
                }
            }
            break;
        }
    }

    for (Event &event : events) {
        total_reports++;
        if (event.get_general_information().at("active") == "true") {
            active_count++;
        }
        if (event.get_general_information().at("forces_arrival_at_scene") == "true") {
            forces_arrival_count++;
        }

        string report_line;
        report_line = "city: " + event.get_city() + "\n" +
        "date time: " + epochToDate(event.get_date_time()) + "\n" +
        "event name: " + event.get_name() + "\n" + 
        "summary: " + summarizeDescription(event.get_description()) + "\n\n";
        report_lines.push_back(report_line);
    }

    ofstream file_stream(file);
    if (!file_stream) {
        cerr << "cannot open the file." << file << endl;
        return;
    }

    file_stream << "Channel " << channelName << "\nStats :\nTotal : " << total_reports 
                << "\nactive : " << active_count << "\nforces arrival at scene : "
                << forces_arrival_count << "\nEvent Reports :\n";

    for (size_t i = 0; i < report_lines.size(); i++) {
        file_stream << "Report_" << (i + 1) << " :\n";
        file_stream << report_lines[i];
    }

    file_stream.close();


}

void sortEvents(vector<Event>& events) {
    sort(events.begin(), events.end(), [](const Event& a, const Event& b) {
        if (a.get_date_time() == b.get_date_time()) {
            return a.get_name() < b.get_name();
        }
        return a.get_date_time() < b.get_date_time();
    });
}
string summarizeDescription(const string &description) {
    if (description.length() > 27) {
        return description.substr(0, 27) + "...";
    }
    return description;
}
string epochToDate(int date_time) {
    time_t raw_time = date_time;
    struct tm * time_info;
    char buffer[80];

    time_info = localtime(&raw_time);
    strftime(buffer, sizeof(buffer), "%d/%m/%y %H:%M", time_info);
    return string(buffer);
}
