package bgu.spl.net.impl.stomp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> activeUsers;
    private ConcurrentHashMap<String, List<Integer>> subscribtions;
    private ConcurrentHashMap<String, String> userNameToPasscode;
    private ConcurrentHashMap<Integer,List<Pair<String,Integer>>> connectionIdChannelToSubscribtionId;

    private ConcurrentHashMap<String, Integer> userNameToConnectionId; //maybe unneeded
    private int index;
    public ConnectionsImpl(){
        activeUsers = new ConcurrentHashMap<>();
        subscribtions = new ConcurrentHashMap<>();
        userNameToPasscode = new ConcurrentHashMap<>();
        userNameToConnectionId = new ConcurrentHashMap<>();
        connectionIdChannelToSubscribtionId = new ConcurrentHashMap<>();
        index = 0; 
    }
    public void connect(String userName,String password, int connectionId){
        //TODO: implement
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
                String stompMessage = 
                "MESSAGE"+'\n'+
                "subscribtion:" + connectionIdChannelToSubscribtionId.get(user).getFirst()+'\n'+
                "message-id:"+ index +'\n'+
                "destination:/"+channel+'\n'+
                ""+'\n'+
                msg+'\n'+
                '\u0000';
                send(user, stompMessage);
            }
            index ++;
        }else{
//             String errorMsg=
//                 "message: channerl doesnt exist"+'\n'+
//                   ""+'\n'+
//                      "The message:"+'\n'+
//                      "----"+'\n'+
//                      msg+'\n'+
//                      "----"+'\n'+
// ;
//             send()
        }
    }

    public void disconnect(int connectionId, String receipt){
        //TODO: implement

    }
    public void subscribe(String channel, String subscribtionId, int connectionId){
        try{
        int intSubId = Integer.parseInt(subscribtionId);
        if(!subscribtions.containsKey(channel)){
            subscribtions.putIfAbsent(channel, new ArrayList<Integer>());
        }
        subscribtions.get(channel).add(connectionId);
        if(!connectionIdChannelToSubscribtionId.containsKey(connectionId)){
            connectionIdChannelToSubscribtionId.putIfAbsent(connectionId, new ArrayList<Pair<String,Integer>>());
        }
        Pair<String,Integer> pair = new Pair<String,Integer>(channel, intSubId);
        connectionIdChannelToSubscribtionId.get(connectionId).add(pair);
        }catch(Exception e){
        String errorMsg = 
        "ERROR"+'\n'+
        "message:unable to subscribe"+'\n'+
        ""+'\n'+
        "The Erro:"+'\n'+
        "----"+'\n'+
        "message:"+'\n'+
        e.getMessage()+'\n'+
        "----"+'\n'+
        "got an Exception when trying to subscribe client number "+ connectionId+ " to channel "+ channel;
        send(connectionId, errorMsg);
        }
    }
    public void unsubscribe(String subId, int connectionId){
        int intSubId = Integer.parseInt(subId);
        Pair<String,Integer> pair = getPairbySubId(intSubId, connectionId);
        String channel = pair.getFirst();
        subscribtions.get(channel).remove(connectionId);
        connectionIdChannelToSubscribtionId.get(connectionId).remove(pair);
    }
    public Pair<String,Integer> getPairbySubId(int subId, int connectionId){
        List<Pair<String,Integer>> list = connectionIdChannelToSubscribtionId.get(connectionId);
        for(Pair<String,Integer> pair:list){
            if(pair.getSecond() == subId ){
                return pair;
            }
        }
        return null;
    }

}
