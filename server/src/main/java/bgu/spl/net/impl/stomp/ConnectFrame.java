package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class ConnectFrame extends Frame {
    public ConnectFrame(int connectionId){
        super(connectionId);
    }
    @Override
    public void process(Connections<String> connections) {
        String userName = headers.get("login");
        String password = headers.get("passcode");
        
        if(!connections.userExists(userName)){
            connections.registerUser(userName,password);
            connections.connect(userName, connectionId);
        }else{
            if(connections.userPassword(userName,password)){
                if(!connections.isUserOnline(userName)){
                    connections.connect(userName, connectionId);
                }
                else{
                    String errorMsg=
                    "ERROR"+ '\n'+
                "message:User already logged in"+'\n'+
                ""+'\n'+
                 "The message:"+'\n'+
                 "----"+'\n'+
                 this.ogMessage+'\n'+
                "----"+'\n'+
                "User "+userName+"' is logged in somewhere else"+'\n'+
                '\u0000';
                connections.send(connectionId,errorMsg);
                }
            }
            else{
                String errorMsg=
                "ERROR"+ '\n'+
                "message: Password does not match UserName"+'\n'+
                ""+'\n'+
                 "The message:"+'\n'+
                 "----"+'\n'+
                 this.ogMessage+'\n'+
                "----"+'\n'+
                "User "+userName+"'s password is different than what you inserted"+'\n'+
                '\u0000';
                connections.send(connectionId, errorMsg);
            }
        }
    }   
}
