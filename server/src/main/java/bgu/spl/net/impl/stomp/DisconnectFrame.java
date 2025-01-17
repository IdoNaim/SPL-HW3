package bgu.spl.net.impl.stomp;
import bgu.spl.net.srv.Connections;

public class DisconnectFrame extends Frame{


    public Frame createFrame(String message){
        Frame frame = new DisconnectFrame();
        try{
        frame.editFrame(message);
        return frame;}
        catch(Exception e){
            return null;
        }

    }
    public void process(Connections<String> connections){

    }
}
