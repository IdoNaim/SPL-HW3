package bgu.spl.net.impl.stomp;
import bgu.spl.net.srv.Connections;

public class DisconnectFrame extends Frame{
    public DisconnectFrame(int connectionId){
        super(connectionId);
    }
    public void process(Connections<String> connections){
        String reciept = headers.get("receipt");
        connections.disconnect(this.connectionId, reciept);
    }
}
