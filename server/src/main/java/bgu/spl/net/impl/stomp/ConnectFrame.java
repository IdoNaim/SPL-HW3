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
        connections.connect(userName,password, this.connectionId);

        
        // if(!connections.userExists(userName)){
        //     connections.connect(userName,password);

        // }
        // else{
        //     if(connections.getPassword(userName).equals(password)){
        //         connections.connect(userName,password, this.connectionId);
        //     }
        //     else{
        //         String errorMsg=
        //         "message: Password does not match UserName"+'\n'+
        //         ""+'\n'+
        //         "The message:"+'\n'+
        //         "----"+'\n'+
        //         this.ogMessage+'\n'+
        //         "----"+'\n'+
        //         "User "+userName+"'s password is different than what you inserted";
        //         ;
        //         connections.send(this.connectionId, errorMsg);
        //     }
        //}
    }
    
}
