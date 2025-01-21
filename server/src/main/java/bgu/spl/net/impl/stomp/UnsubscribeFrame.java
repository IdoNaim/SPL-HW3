package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class UnsubscribeFrame extends Frame {
    public UnsubscribeFrame(int connectionId){
        super(connectionId);
    }

    @Override
    public void process(Connections<String> connections) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
        connections.unsubscribe(headers.get("id"), this.connectionId);
    }
    
}
