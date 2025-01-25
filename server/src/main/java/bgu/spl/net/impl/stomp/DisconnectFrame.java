package bgu.spl.net.impl.stomp;
import bgu.spl.net.srv.Connections;

public class DisconnectFrame extends Frame{
    public DisconnectFrame(int connectionId){
        super(connectionId);
    }
    public boolean process(Connections<String> connections){
        //check if user is not connected
        if(connections.isUserOnline(connectionId)){
            String recieptId = headers.get("receipt");
            String recieptMsg =
            "RECEIPT"+'\n'+
            "receipt-id:"+recieptId+'\n'+
            ""+'\n'+
            '\u0000';
            connections.disconnect(this.connectionId, recieptMsg);
            return true;
        }else{
            String errorMsg =
            "ERROR"+'\n'+
            "message:Tried to disconnect a disconnected user"+'\n'+
            ""+'\n'+
            "The message:"+'\n'+
            "----"+'\n'+
            ogMessage +'\n'+
            "----"+'\n'+
            '\u0000';
            connections.send(connectionId, errorMsg);
            return false;
        }
    }
}
