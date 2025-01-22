package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class SubscribeFrame extends Frame {
    public SubscribeFrame(int connectionId){
        super(connectionId);
    }

    @Override
    public void process(Connections<String> connections) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
        String destination = headers.get("destination");
        //assuming destination is like /topic/police:
        String[] destArray = destination.split("/"); //should hold [,topic , police]
        String subscriptionId = headers.get("id");
        if(connections.isUserOnline(connectionId)){
            connections.subscribe(destArray[2], subscriptionId, this.connectionId);
        }
        else{
            String errorMsg =
            "ERROR"+ '\n'+
                "message:User tried subscribing without logging in"+'\n'+
                ""+'\n'+
                 "The message:"+'\n'+
                 "----"+'\n'+
                 this.ogMessage+'\n'+
                "----"+'\n'+
                "client with connection ID "+connectionId+" tried subscribing but wasnt logged in"+'\n'+
                '\u0000';
                connections.send(connectionId, errorMsg);
        }

    }
    
}
