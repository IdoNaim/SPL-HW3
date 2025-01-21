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
        connections.subscribe(destArray[2], subscriptionId, this.connectionId);

    }
    
}
