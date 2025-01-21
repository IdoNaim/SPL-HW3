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
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, Integer>> connectionIdChannelToSubscribtionId;
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
    // public int connect(ConnectionHandler<T> ch){
    //     // activeUsers.putIfAbsent(index, ch);
    //     // index++;
    //     // return index-1;
    // }
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
                "subscribtion:" + connectionIdChannelToSubscribtionId.get(user).get(channel)+'\n'+
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

    public void disconnect(int connectionId){
        //TODO: implement
        //activeUsers.remove(connectionId);
    }
    public void subscribe(String channel, String subscribtionId, int connectionId){
        try{
        int intSubId = Integer.parseInt(subscribtionId);
        if(!subscribtions.containsKey(channel)){
            subscribtions.putIfAbsent(channel, new ArrayList<Integer>());
        }
        subscribtions.get(channel).add(connectionId);
        connectionIdChannelToSubscribtionId.get(connectionId).putIfAbsent(channel, intSubId);
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

}
