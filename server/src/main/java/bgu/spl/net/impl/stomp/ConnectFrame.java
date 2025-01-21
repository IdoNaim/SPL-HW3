package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Connections;

public class ConnectFrame extends Frame {
    public ConnectFrame(){
        super();
    }
    @Override
    public void process(Connections<String> connections) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
        String userName = headers.get("login");
        String password = headers.get("passcode");
        if(!connections.userExists(userName)){
            connections.connect(userName,password);

        }
        else{
            if(connections.getPassword(userName).equals(password)){
                connections.connect(userName,password);
            }
            else{
                String errorMsg=
                "message: Password does not match UserName"+'\n'+
                ""+'\n'+
                "The message:"+'\n'+
                "----"+'\n'+
                this.ogMessage+'\n'+
                "----"+'\n'+
                "User "+userName+"'s password is deifferent than what you inserted";
                ;
            }
        }
    }
    
}
