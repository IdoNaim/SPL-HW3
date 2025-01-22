#pragma once

#include "../include/ConnectionHandler.h"
using namespace std;

class StompProtocol {
private:
    string clientID;
    map<string, bool> subscriptions;
    string createFrame(const string& command, const map<string, string>& headers, const string& body);

public:
    StompProtocol();

    string connectFrame();
    string subscribeFrame(const string& topic);
    string unsubscribeFrame(const string& topic);
    string sendFrame(const string& topic, const string& message);
    string disconnectFrame();
};
