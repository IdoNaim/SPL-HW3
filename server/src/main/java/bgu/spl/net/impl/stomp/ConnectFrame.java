package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class ConnectFrame extends Frame {
    public ConnectFrame(int connectionId){
        super(connectionId);
    }
    @Override
    public void process(Connections<String> connections) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
        String userName = headers.get("login");
        String password = headers.get("passcode");
        
        if(!connections.userExists(userName)){
            connections.registerUser(userName,password);
            connections.connect(userName, connectionId);
        }else{
            if(connections.userPassword(userName,password)){
                connections.connect(userName, connectionId);
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
                "User "+userName+"'s password is different than what you inserted";
                connections.send(connectionId, errorMsg);
            }
        }
    }   
}
