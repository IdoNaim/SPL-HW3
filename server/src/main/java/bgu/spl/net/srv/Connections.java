package bgu.spl.net.srv;

import java.io.IOException;

import bgu.spl.net.impl.stomp.Pair;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void send(String channel, T msg);

    void disconnect(int connectionId);


    void subscribe(String channel, String subscribtionId, int connectionId);
    void unsubscribe(String subId, int connectionId);
    void connect(String userName, int connectionId);
    void disconnect(int connectionId, T msgWithReceipt);
    boolean userExists(String userName);
    void registerUser(String userName, String password);
    boolean userPassword(String userName, String password);
    boolean isUserOnline(String userName);
    boolean isUserOnline(int connectionId);
    boolean isUserSubbed(int intSubId, int connectionId);
    boolean isUserSubbed(String channel, int connectionId);
    Pair<String, Integer> getPairbyChannel(String channel, int connectionId);

}
