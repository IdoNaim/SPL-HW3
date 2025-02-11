#pragma once

#include <sstream>
#include <thread>
#include <atomic>
#include <mutex>
#include <queue>
using namespace std;


class keyboardInput
{
private:
    atomic<bool> running;
    mutex& queueMutex;
    queue<string>& frameQueue;
    istringstream iss;
    string command;
    thread keyboardThread;

    void readFromKeyboard();

public:
    keyboardInput(mutex& queueMutex, queue<string>& frameQueue);
    void start();
    void stop();
};
