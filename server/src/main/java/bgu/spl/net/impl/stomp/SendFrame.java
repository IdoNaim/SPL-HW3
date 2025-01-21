package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class SendFrame extends Frame {
    public SendFrame(int connectionId){
        super(connectionId);
    }

    @Override
    public void process(Connections<String> connections) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
        String destination = headers.get("destination");
        //assuming destination is like /topic/police:
        String[] destArray = destination.split("/"); //should hold [,topic , police]
        connections.send(destArray[2], body);
    }
    
}
