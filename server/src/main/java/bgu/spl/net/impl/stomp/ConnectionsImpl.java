package bgu.spl.net.impl.stomp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> activeUsers; // should add and remove
    private ConcurrentHashMap<String, List<Integer>> subscribtions; // should add and remove from *list*
    private ConcurrentHashMap<String, String> userNameToPasscode; //should only add
    private ConcurrentHashMap<Integer,List<Pair<String,Integer>>> connectionIdToChannelSubscribtionId; // should add and remove from list and hashmap
    private ConcurrentHashMap<Integer, String> connectionIdToUserName; //should add and remove
    private int index;
    public ConnectionsImpl(){
        activeUsers = new ConcurrentHashMap<>();
        subscribtions = new ConcurrentHashMap<>();
        userNameToPasscode = new ConcurrentHashMap<>();
        connectionIdToUserName = new ConcurrentHashMap<>();
        connectionIdToChannelSubscribtionId = new ConcurrentHashMap<>();
        index = 0; 
    }
    public void startConnection(int connectionId, ConnectionHandler<T> ch){
        activeUsers.putIfAbsent(connectionId, ch);
    }

    public void connect(String userName, int connectionId){
        connectionIdToUserName.putIfAbsent(connectionId, userName);
        
    }
    public void registerUser(String userName,String password){
        userNameToPasscode.putIfAbsent(userName, password);
    }
    

    public boolean send(int connectionId, T msg){
        //TODO: implement
        try{
        ConnectionHandler<T> ch = activeUsers.get(connectionId);
        ch.send(msg);
        return true;
        }catch(Exception e){
            return false;
        }

    }

    public void send(String channel, T msg){
        //TODO: implement
        List<Integer> list = subscribtions.get(channel);
        if(list != null){
            for (Integer user : list) {
                send(user, msg);
            }
        }
    }

    public void disconnect(int connectionId, T msgWithReceipt){
        //TODO: implement
        ConnectionHandler<T> ch = activeUsers.get(connectionId);
        disconnect(connectionId);
        ch.send(msgWithReceipt);

        //send message back with receipt

    }
    public void disconnect(int connectionId){
        if(activeUsers.containsKey(connectionId)){
            activeUsers.remove(connectionId);
        }
        if(connectionIdToUserName.containsKey(connectionId)){
             connectionIdToUserName.remove(connectionId);
        }
    }
    public void subscribe(String channel, String subscribtionId, int connectionId){
        int intSubId = Integer.parseInt(subscribtionId);
        if(!subscribtions.containsKey(channel)){
            subscribtions.putIfAbsent(channel, new ArrayList<Integer>());
        }
        subscribtions.get(channel).add(connectionId);
        if(!connectionIdToChannelSubscribtionId.containsKey(connectionId)){
            connectionIdToChannelSubscribtionId.putIfAbsent(connectionId, new ArrayList<Pair<String,Integer>>());
        }
        Pair<String,Integer> pair = new Pair<String,Integer>(channel, intSubId);
        connectionIdToChannelSubscribtionId.get(connectionId).add(pair);

    }
    public void unsubscribe(String subId, int connectionId){
        int intSubId = Integer.parseInt(subId);
        Pair<String,Integer> pair = getPairbySubId(intSubId, connectionId);
        String channel = pair.getFirst();
        subscribtions.get(channel).remove(connectionId);
        connectionIdToChannelSubscribtionId.get(connectionId).remove(pair);
        if(connectionIdToChannelSubscribtionId.get(connectionId).isEmpty()){
            connectionIdToChannelSubscribtionId.remove(connectionId);
        }
    }
    public Pair<String,Integer> getPairbySubId(int subId, int connectionId){
        List<Pair<String,Integer>> list = connectionIdToChannelSubscribtionId.get(connectionId);
        for(Pair<String,Integer> pair:list){
            if(pair.getSecond() == subId ){
                return pair;
            }
        }
        return null;
    }
    public boolean userExists(String userName){
        for (String name : userNameToPasscode.keySet()) {
            if (name.equals(userName)){
                return true;
            }
        }
        return false;
    }
    public boolean userPassword(String userName, String password){
        return userNameToPasscode.get(userName).equals(password);
    }
    public boolean isUserOnline(String userName){
        for (String name : connectionIdToUserName.values()) {
            if (name.equals(userName)){
                return true;
            }
        }
        return false;
    }
    public boolean isUserOnline(int connectionId){
        return connectionIdToUserName.containsKey(connectionId);
    }

}
