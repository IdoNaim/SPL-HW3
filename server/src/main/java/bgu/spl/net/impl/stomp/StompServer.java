package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        //TODO: implement
        args = new String[]{"7778","reactor"};
        int port;
        try{
            port = Integer.parseInt(args[0]);
            if (args[1].equals("tpc")){
                Server.threadPerClient(
                    port, //port
                    () -> new StompMessagingProtocolImpl(), //protocol factory
                    LineMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();
            }
            else if(args[1].equals("reactor")){
                Server.reactor(
                 Runtime.getRuntime().availableProcessors(),
                 port, //port
                 () -> new StompMessagingProtocolImpl(), //protocol factory
                 LineMessageEncoderDecoder::new //message encoder decoder factory
         ).serve();
            }
            else{
                System.out.println("you must supply on the second argument: <type_of_server> - tpc / reactor");
            }
        }catch(Exception e){
            System.out.println("you must supply two arguments: <port>, <type_of_server> - tpc / reactor");
        }
    }
}
