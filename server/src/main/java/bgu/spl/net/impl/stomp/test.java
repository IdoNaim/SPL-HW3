package bgu.spl.net.impl.stomp;

public class test {
    public static void main(String[] args){
        String body ="";
        body = body +'\n';
        System.out.print(body.length());
        String connect = 
        "CONNECT" +'\n'+
        "accept-version:1.2"+ '\n'+
        "host:stomp.cs.bgu.ac.il" +'\n'+
        "login:meni" +'\n'+
        "passcode:films"+'\n'+
        '\n'+
        '\u0000';
        String send = 
        "SEND"+'\n'+
        "destination:/police"+'\n'+
        ""+'\n'+
        "Hello topic a"+'\n'+
        '\u0000';
        String subscribe =
        "SUBSCRIBE"+'\n'+
        "destination:/topic/a"+'\n'+
        "id:78"+'\n'+
        ""+'\n'+
        '\u0000';
        String unsubscribe =
        "UNSUBSCRIBE"+'\n'+
        "id:78"+'\n'+
        ""+'\n'+
        '\u0000';
        String disconnect =
        "DISCONNECT"+'\n'+
        "receipt:77"+'\n'+
        ""+'\n'+
        '\u0000';
        String[] commandArr = connect.split("\n");
        String command = commandArr[0];
        if(command.equals("CONNECT")){

        }
        String check = "police";
        String checking[] = check.split("/");
        ConnectFrame connectFrame = new ConnectFrame(0);
        SendFrame sendFrame = new SendFrame(0);
        SubscribeFrame subscribeFrame = new SubscribeFrame(0);
        UnsubscribeFrame unsubscribeFrame = new UnsubscribeFrame(0);
        DisconnectFrame disconnectFrame = new DisconnectFrame(0);
        String[] message = disconnect.split("\n");
        connectFrame.initFrame(connect);
        sendFrame.initFrame(send);
        subscribeFrame.initFrame(subscribe);
        unsubscribeFrame.initFrame(unsubscribe);
        disconnectFrame.initFrame(disconnect);

        System.out.print("debug");
        
    }   
}
