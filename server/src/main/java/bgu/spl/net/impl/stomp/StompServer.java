package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        //TODO: implement
        if (args[1].equals("tpc")){
            Server.threadPerClient(
                7777, //port
                () -> new StompMessagingProtocolImpl(), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
        }
        else if(args[1].equals("reactor")){
        
        }
        else{
            System.out.println("reactor or tpc");
        }
    }
}
