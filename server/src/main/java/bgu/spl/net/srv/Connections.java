package bgu.spl.net.srv;

import java.io.IOException;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void send(String channel, T msg);

    void disconnect(int connectionId);
    void subscribe(String channel, String subscribtionId, int connectionId);
    void unsubscribe(String subId, int connectionId);
    void connect(String userName, int connectionId);
    void disconnect(int connectionId, T msgWithReceipt);
}
