package bgu.spl.net.impl.stomp;
import bgu.spl.net.srv.Connections;

import java.util.HashMap;

public abstract class Frame{
    private String command;
    private HashMap<String,String> headers;
    private String body;
    public Frame(){
        headers = new HashMap<>();
    }
    public void editFrame(String msg){
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

    // }
    // public static Frame createFrame(String message){
    //     Frame result = new Frame();
    //     try{
    //     result.editFrame(message);
    //     return result;

    //     }catch(Exception e){
    //         return null;
    //     }
    // }
    public abstract Frame creatFrame(String message);
    public abstract void process(Connections<String> connections);
}