package bgu.spl.net.impl.stomp;

public class test {
    public static void main(String[] args){
        String message = "SUBSCRIBE\nid:1\ndestination:/dest\n\nHello topic nigga\n^@";
        Frame frame = new Frame();
        frame.editFrame(message);
    }   
}
