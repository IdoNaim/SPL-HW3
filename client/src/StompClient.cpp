#include <stdlib.h>
#include "../include/ConnectionHandler.h"
 #include <thread>
 #include <mutex>
int main(int argc, char *argv[]) {
	// TODO: implement the STOMP client
	std::string host = argv[1];
	short port = atoi(argv[2]);
	ConnectionHandler connectionHandler(host, port);
	if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	std::thread obj([](ConnectionHandler connectionHandler)-> void {
		while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
		int len=line.length();
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;

 
        
        std::string answer;
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        
		len=answer.length();
        answer.resize(len-1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }});

	return 0;
}