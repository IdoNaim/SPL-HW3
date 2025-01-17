package bgu.spl.net.srv;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> activeUsers;
    private ConcurrentHashMap<String, List<Integer>> subscribtions;
    private int index;
    public ConnectionsImpl(){
        activeUsers = new ConcurrentHashMap<>();
    }
    public int connect(ConnectionHandler<T> ch){
        activeUsers.putIfAbsent(index, ch);
        index++;
        return index-1;
    }
    public boolean send(int connectionId, T msg){
        //TODO: implement
        ConnectionHandler<T> ch = activeUsers.get(connectionId);
        ch.send(msg);
        return true;
    }

    public void send(String channel, T msg){
        //TODO: implement
        List<Integer> list = subscribtions.get(channel);
        for (Integer user : list) {
            send(user, msg);
        }
    }

    public void disconnect(int connectionId){
        //TODO: implement
        activeUsers.remove(connectionId);
    }
}
