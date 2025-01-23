package bgu.spl.net.impl.stomp;
import bgu.spl.net.srv.Connections;

import java.util.HashMap;

public abstract class Frame{
    protected String ogMessage;
    protected String command;
    protected HashMap<String,String> headers;
    protected String body;
    protected int connectionId;
    public Frame(int connectionId){
        this.connectionId = connectionId;
        headers = new HashMap<>();
    }
    public void initFrame(String msg){
        ogMessage= msg.substring(0,msg.length()-1);
        System.out.println(msg);
        String[] message = msg.split("\n");
        command = message[0];
        int i = 1;
        // while(!message[i].equals("")){
        while(message[i].contains(":")){
            if(message[i]!=":"){
                String[] header = message[i].split(":");
                headers.putIfAbsent(header[0], header[1]);
            }
            i++;
        }
        //i here is "" so we need to add 1 to it
        body = "";
        while(!message[i].equals("\u0000")){
            body = body + message[i];
            i++;
        }

     }
    public abstract void process(Connections<String> connections);
}