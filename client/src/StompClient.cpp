#include "../include/keyboardInput.h"
#include "../include/StompProtocol.h"
#include <iostream>
#include <thread>
#include <cstdlib>
#include <stdexcept>
#include <queue>
#include <condition_variable>
using namespace std;

queue<Frame> frameQueue;  // Shared queue for frames
mutex queueMutex;  // Mutex for synchronizing access to the queue

int main(int argc, char *argv[]) {
    // Check for valid arguments (host and port)
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = std::atoi(argv[2]);

    // Create a ConnectionHandler object
    ConnectionHandler connectionHandler(host, port);

    // Attempt to connect to the server
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return -1;
    }

    keyboardInput keyboardInput(queueMutex, frameQueue);

    // Start the KeyboardInput to handle user commands
    keyboardInput.start();

    // Might need to delay in readFromKeyboard function so it
    // won't start the keyboard thread before this one

    while(true){
        /*
        THE IDEA:
        *** While working on the queue, dont forget to use the queue mutex
        check if queue is empty:
            if its not empty:
                - send frame to server
                - keep in mind u might need to get a frame back
                - keep in mind u need to save frames for summary later
                - *** when do we close? i mean when do we break the loop?
            if its empty:
            - sleep for a short time(idk, like 200 miliseconds?)
        
         */
    }

    // I Probably forgot a ton oh stuff so double check(in the loop and outside of it)

    return 0;
}

