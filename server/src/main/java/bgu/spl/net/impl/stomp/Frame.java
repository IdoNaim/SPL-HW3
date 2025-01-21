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
        while(!message[i].equals("")){
            if(message[i]!=":"){
                String[] header = message[i].split(":");
                headers.putIfAbsent(header[0], header[1]);
            }
            i++;
        }
        body = message[i+1];

     }
    // public static Frame createFrame(String message){
    //     Frame result = new Frame();
    //     try{
    //     result.editFrame(message);
    //     return result;

    //     }catch(Exception e){
    //         return null;
    //     }
    // }
    public abstract void process(Connections<String> connections);
}