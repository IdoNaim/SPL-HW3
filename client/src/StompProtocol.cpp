#include "../include/StompProtocol.h"
#include <sstream>

StompProtocol::StompProtocol() : clientID("") {}

string StompProtocol::createFrame(const string& command, const map<string, string>& headers, const string& body) {
    ostringstream frame;
    frame << command << "\n";

    for (const auto& [key, value] : headers) {
        frame << key << ":" << value << "\n";
    }

    frame << "\n" << body << "\0";
    return frame.str();
}

string StompProtocol::connectFrame() {
    map<string, string> headers = {
        {"accept-version", "1.2"}
    };
    return createFrame("CONNECT", headers, "");
}

string StompProtocol::subscribeFrame(const string& topic) {
    map<string, string> headers = {
        {"destination", topic},
        {"id", topic}
    };
    return createFrame("SUBSCRIBE", headers, "");
}

string StompProtocol::unsubscribeFrame(const string& topic) {
    map<string, string> headers = {
        {"id", topic}
    };
    return createFrame("UNSUBSCRIBE", headers, "");
}

string StompProtocol::sendFrame(const string& topic, const string& message) {
    map<string, string> headers = {
        {"destination", topic}
    };
    return createFrame("SEND", headers, message);
}

string StompProtocol::disconnectFrame() {
    return createFrame("DISCONNECT", {}, "");
}
