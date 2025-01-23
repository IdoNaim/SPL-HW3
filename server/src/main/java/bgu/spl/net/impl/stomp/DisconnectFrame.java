package bgu.spl.net.impl.stomp;
import bgu.spl.net.srv.Connections;

public class DisconnectFrame extends Frame{
    public DisconnectFrame(int connectionId){
        super(connectionId);
    }
    public void process(Connections<String> connections){
        //check if user is not connected
        String recieptId = headers.get("receipt");
        String recieptMsg =
        "RECEIPT"+'\n'+
        "receipt-id:"+recieptId+'\n'+
        ""+'\n'+
        '\u0000';
        connections.disconnect(this.connectionId, recieptMsg);
    }
}
