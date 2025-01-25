#pragma once


#include "../include/keyboardInput.h"
#include "../include/StompProtocol.h"
#include "../include/event.h"
#include <map>
#include <queue>
#include <list>
#include <string>
#include <vector>
#include <memory>
#include <mutex>

class StompClient {
public:
    struct User {
        std::string username;
        std::list<struct Channel> channels;
    };

    struct Channel {
        std::string channelName;
        std::vector<Event> events;
    };

    StompClient();
    ~StompClient();

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

private:
    int idGenerator();
    int receiptIdGenerator();

    
    std::map<std::string, int> subscriptions;
    bool loggedIn;
    std::queue<std::string> frameQueue;
    std::mutex queueMutex;
    std::unique_ptr<ConnectionHandler> connectionHandler;
    std::string username;
    std::list<User> users;
};


