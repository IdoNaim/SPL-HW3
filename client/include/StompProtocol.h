#pragma once
#include "../include/keyboardInput.h"
#include "../include/ConnectionHandler.h"
#include "../include/event.h"
#include <map>
#include <queue>
#include <list>
#include <string>
#include <vector>
#include <memory>
#include <mutex>

class StompProtocol {
public:
    struct Channel {
        std::string channelName;
        std::vector<Event> events;
    };
    struct User {
        std::string username;
        std::list<struct Channel> channels;
    };

    std::list<User> users;
    std::unique_ptr<ConnectionHandler> connectionHandler;
    StompProtocol(mutex& queueMutex, queue<string>& frameQueue);
    ~StompProtocol();

    ConnectionHandler* getConnectionHandler();
    bool login(const std::string line);
    std::string createConnectFrame(const std::string line);
    std::string createSubscribeFrame(const std::string line);
    std::string createUnsubscribeFrame(const std::string line);
    std::string createSendFrame(const Event event);
    std::string createLogoutFrame();
    void handleReportCommand(const std::string line);
    void handleSummaryCommand(const std::string line);
    void sortEvents(std::vector<Event>& events);
    std::string summarizeDescription(const std::string& description);
    std::string epochToDate(int date_time);
    void processCommand(const string& line);
    void getMessages();
    Event parseMessageToEvent(const std::string& message);


private:
    int idGenerator();
    int receiptIdGenerator();

    
    std::map<std::string, int> subscriptions;
    bool loggedIn;
    std::queue<std::string> frameQueue;
    std::mutex& queueMutex;
    
    std::string username;
};


