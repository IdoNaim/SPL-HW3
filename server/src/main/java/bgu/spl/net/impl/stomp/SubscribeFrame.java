package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class SubscribeFrame extends Frame {
    public SubscribeFrame(int connectionId){
        super(connectionId);
    }

    @Override
    public boolean process(Connections<String> connections) {
        String destination = headers.get("destination");
        // destination is like /police:
        String[] destArray = destination.split("/"); //should hold [, police]
        String subscriptionId = headers.get("id");
        if(connections.isUserOnline(connectionId)){
            connections.subscribe(destArray[destArray.length-1], subscriptionId, this.connectionId);
            String recieptId = headers.get("receipt");
            String receipt =
            "RECEIPT"+'\n'+
            "receipt-id:"+recieptId+'\n'+
            ""+'\n'+
            '\u0000';
            connections.send(connectionId, receipt);
            return false;
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
                return true;
        }

    }
    
}
