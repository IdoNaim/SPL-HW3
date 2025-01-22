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
        if(connections.isUserOnline(connectionId)){
            if(connections.isUserSubbed(headers.get("id"), this.connectionId)){
                connections.unsubscribe(headers.get("id"), this.connectionId);
            }
            else{
                String errorMsg=
                "ERROR"+ '\n'+
                "message:you are not subscribed to this channelId"+'\n'+
                ""+'\n'+
                 "The message:"+'\n'+
                 "----"+'\n'+
                 this.ogMessage+'\n'+
                "----"+'\n'+
                "You tried to unsubscribe from a channel you are not subscribed to"+'\n'+
                '\u0000';
                connections.send(connectionId, errorMsg);
            }
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
                "client with connection ID "+connectionId+" tried unsubscribing but wasnt logged in"+'\n'+
                '\u0000';
                connections.send(connectionId, errorMsg);
        }
    }
    
}
