# CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
# LDFLAGS:=-lboost_system -lpthread
# # TODO change the makefile for StompEMIlient
# all: EchoClient

# EchoClient: bin/ConnectionHandler.o bin/echoClient.o
# 	g++ -o bin/EchoClient bin/ConnectionHandler.o bin/echoClient.o $(LDFLAGS)

# StompWCIClient: bin/ConnectionHandler.o bin/StompClient.obin/event.o
# 	g++ -o bin/StompWCIClient bin/ConnectionHandler.o bin/StompClient.o $(LDFLAGS)

# bin/ConnectionHandler.o: src/ConnectionHandler.cpp
# 	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

# bin/echoClient.o: src/echoClient.cpp
# 	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

# bin/event.o: src/event.cpp
# 	g++ $(CFLAGS) -o bin/event.o src/event.cpp

# bin/StompClient.o: src/StompClient.cpp
# 	g++ $(CFLAGS) -o bin/StompClient.o src/StompClient.cpp

# .PHONY: clean
# clean:
# 	rm -f bin/*


all:  link

link: compile
	g++ -o bin/StompEMIlient bin/StompClient.o bin/ConnectionHandler.o bin/event.o bin/keyboardInput.o bin/StompProtocol.o

compile: clean src/StompClient.cpp src/ConnectionHandler.cpp src/event.cpp src/keyboardInput.cpp src/StompProtocol.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11  -o bin/ConnectionHandler.o src/ConnectionHandler.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11  -o bin/event.o src/event.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11  -o bin/keyboardInput.o src/keyboardInput.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11  -o bin/StompClient.o src/StompClient.cpp
	g++ -g -c -Wall -Weffc++ -std=c++11  -o bin/StompProtocol.o src/StompProtocol.cpp


clean:
	rm -rf bin/*