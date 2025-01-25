#include "../include/keyboardInput.h"
#include <iostream>
using namespace std;

keyboardInput::keyboardInput(mutex& queueMutex, queue<string>& frameQueue)
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
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
		string line(buf);
        iss.clear();
        iss.str(line);
        iss >> command;

        queueMutex.lock();
        if (command == "login" || command == "join" || command == "exit" || command == "report" ||
        command == "summary" || command == "logout") {
            frameQueue.push(line);
        } else {
            cerr << "Unknown command. Please try again." << endl;
        }
        queueMutex.unlock();


    }
}
