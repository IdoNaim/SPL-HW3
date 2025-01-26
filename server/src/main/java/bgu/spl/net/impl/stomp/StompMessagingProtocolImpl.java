package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

public class StompMessagingProtocolImpl implements StompMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private Connections<String> connections;
    private int connectionId;

    public void start(int connectionId, Connections<String> connections){
        
        this.connections = connections;
        this.connectionId = connectionId;
        
    }
    
    public void process(String message){
        
        String command = message.split("\n")[0];
        if(command.equals("CONNECT")){
            ConnectFrame frame = new ConnectFrame(this.connectionId);
            frame.initFrame(message);
            boolean result = frame.process(connections);
            if(result){
                shouldTerminate = true;
            }

        }else if(command.equals("DISCONNECT")){
            DisconnectFrame frame = new DisconnectFrame(this.connectionId);
            frame.initFrame(message);
            boolean result = frame.process(connections);
            shouldTerminate = true;

        }else if(command.equals("SUBSCRIBE")){
            SubscribeFrame frame = new SubscribeFrame(this.connectionId);
            frame.initFrame(message);
            boolean result =  frame.process(connections);
            if(result){
                shouldTerminate = true;
            }

        }else if(command.equals("UNSUBSCRIBE")){
            UnsubscribeFrame frame = new UnsubscribeFrame(this.connectionId);
            frame.initFrame(message);
            boolean result = frame.process(connections);
            if(result){
                shouldTerminate = true;
            }

        }else if(command.equals("SEND")){
            SendFrame frame = new SendFrame(this.connectionId);
            frame.initFrame(message);
            boolean result = frame.process(connections);
            if(result){
                shouldTerminate = true;
            }
        }
        else{
           String errorMsg=
           "ERROR"+'\n'+
           "message:malformed frame recieved"+'\n'+
           ""+'\n'+
           "The message: "+'\n'+
           "----"+'\n'+
           message+'\n'+
           "----"+'\n'+
           '\u0000';
           connections.send(connectionId,errorMsg);
           shouldTerminate = true;

        }
        

    }

    public boolean shouldTerminate(){
        
        return shouldTerminate;
    }

}
