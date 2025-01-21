package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolImpl implements StompMessagingProtocol<String> {

    private boolean shouldTerminate = false;
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
        String command = message.split("\n")[0];
        if(command.equals("CONNECT")){
            ConnectFrame frame = new ConnectFrame(this.connectionId);
            frame.initFrame(message);
            frame.process(connections);

        }else if(command.equals("DISCONNECT")){
            DisconnectFrame frame = new DisconnectFrame(this.connectionId);
            frame.initFrame(message);
            frame.process(connections);

        }else if(command.equals("SUBSCRIBE")){
            SubscribeFrame frame = new SubscribeFrame(this.connectionId);
            frame.initFrame(message);
            frame.process(connections);

        }else if(command.equals("UNSUBSCRIBE")){
            UnsubscribeFrame frame = new UnsubscribeFrame(this.connectionId);
            frame.initFrame(message);
            frame.process(connections);

        }else if(command.equals("SEND")){
            SendFrame frame = new SendFrame(this.connectionId);
            frame.initFrame(message);
            frame.process(connections);
        }
        else{
           //figure out what to do in error
           String errorMsg=
           "message:malformed frame recieved"+'\n'+
           ""+'\n'+
           "The message: "+'\n'+
           "----"+'\n'+
           message+'\n'+
           "----"+'\n'+
           '\u0000';
           connections.sendError(errorMsg);
        }
        

    }

    public boolean shouldTerminate(){
        //TODO: implement
        return shouldTerminate;
    }

}
