package bgu.spl.net.impl.stomp;

import java.util.List;

import bgu.spl.net.srv.Connections;

public class SendFrame extends Frame {
    public SendFrame(int connectionId){
        super(connectionId);
    }

    @Override
    public void process(Connections<String> connections) {
        // TODO Auto-generated method stub
        String destination = headers.get("destination");

        String[] destArray = destination.split("/"); //should hold [,police]
        String channel = destArray[1];

        if(connections.isUserOnline(connectionId)){
            if(connections.isUserSubbed(channel,connectionId)){
                List<Integer> subscribers = connections.getSubscribers(channel);
                for (int subscriber : subscribers) {
                    Pair<String,Integer> pair = connections.getPairbyChannel(channel,subscriber);
                    int subscriberSubId = pair.getSecond();
                    String Message = 
                    "MESSAGE"+'\n'+
                    "subscription:"+subscriberSubId+'\n'+
                    "message-id:"+IdGenerator.getInstance().getId()+'\n'+
                    "destination:"+destination+'\n'+
                    ""+'\n'+
                    body+'\n'+
                    '\u0000';
                    connections.send(connectionId, Message);
                }
            }
            else{
                String errorMsg=
                "ERROR"+ '\n'+
                "message:You are not subscribed to this channel"+'\n'+
                ""+'\n'+
                 "The message:"+'\n'+
                 "----"+'\n'+
                 this.ogMessage+'\n'+
                "----"+'\n'+
                "you tried sending a message to channel "+channel+" but you are not subscribed to it"+'\n'+
                '\u0000';
                connections.send(connectionId, errorMsg);
            }
        }
        else{
            String errorMsg =
            "ERROR"+ '\n'+
                "message:User tried reporting without logging in"+'\n'+
                ""+'\n'+
                 "The message:"+'\n'+
                 "----"+'\n'+
                 this.ogMessage+'\n'+
                "----"+'\n'+
                "client with connection ID "+connectionId+" tried reporting but wasnt logged in"+'\n'+
                '\u0000';
                connections.send(connectionId, errorMsg);
        }
    }
    
}
