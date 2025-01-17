package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolImpl implements StompMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private ConcurrentHashMap<String, Runnable> commands;
    private Connections<String> connections;
    private int connectionId;

    public void start(int connectionId, Connections<String> connections){
        //TODO: implement
        this.connections = connections;
        this.connectionId = connectionId;
        // String message = "Login succesful";
        // connections.send(connectionId, message);
    }
    
    public void process(String message){
        //TODO: impelement
        Frame frame = Frame.createFrame(message);
        if(frame == null){
            //message isnt in format maybe wouldnt even get here
        }
        else{
            frame.process(connections);
        }
        

    }

    public boolean shouldTerminate(){
        //TODO: implement
        return shouldTerminate;
    }

}
