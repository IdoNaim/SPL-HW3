#include "../include/keyboardInput.h"
#include <iostream>
using namespace std;

keyboardInput::keyboardInput(mutex& queueMutex, queue<Frame> frameQueue)
    : queueMutex(queueMutex), frameQueue(frameQueue), running(false) {}

void keyboardInput::start() {
    if(!running){
        running = true;
        keyboardThread = thread(&keyboardInput::readFromKeyboard, this);
    }
}

void keyboardInput::stop() {
    running = false;
    if (keyboardThread.joinable()) {
    keyboardThread.join();  // Wait for the thread to finish before destruction
    // need to delete the thread?
    }
}

void keyboardInput::readFromKeyboard() {
    while (running) {
        string input;
        getline(cin, input);
        inputLine.str(input);
        inputLine >> command;

        queueMutex.lock();

        if (command == "login") {
            // Frame send: CONNECT
            // Frame Recieved: CONNECTED
            // ADD "Frame send" to queue, StompClient will deliver it to the Server.
            // need to recieve back a frame in StompClient, how to do it? also, need to save frames, how to do it?
        } else if (command == "join") {
            // Frame send: SUBSCRIBE
            // Frame Recieved: RECIEPT
            // ADD "Frame send" to queue, StompClient will deliver it to the Server.
            // need to recieve back a frame in StompClient, how to do it? also, need to save frames, how to do it?
        } else if (command == "exit") {
            // Frame send: UNSUBSCRIBE
            // Frame Recieved: RECIEPT
            // ADD "Frame send" to queue, StompClient will deliver it to the Server.
            // need to recieve back a frame in StompClient, how to do it? also, need to save frames, how to do it?
        } else if (command == "send") {
            // Frame send: SEND
            // Frame Recieved: - (ERROR if: the server cant proccess the send frame OR this client not subscribed to this topic)
            // ADD "Frame send" to queue, StompClient will deliver it to the Server.
            // need to recieve back a frame in StompClient, how to do it? also, need to save frames, how to do it?
        } else if (command == "logout") {
            // Frame send: DISCONNECT
            // Frame Recieved: RECIEPT
            // ADD "Frame send" to queue, StompClient will deliver it to the Server.
            // need to recieve back a frame in StompClient, how to do it? also, need to save frames, how to do it?
        } else {
            // I think there is summary and maybe one more I'm not sure, so need to add more ifs.
            // what to do if it the command isn't any of those?
        }
        queueMutex.unlock();


    }
}
