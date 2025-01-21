package bgu.spl.net.impl.stomp;

public class test {
    public static void main(String[] args){
        String thing = "MESSAGE" +'\n'+
        "subscription:78"+ '\n'+
        "message-id:20" +'\n'+
        "destination:/topic/a" +'\n'
        +""+'\n'+
        "Hello Topic a\n"
        +'\u0000';
        String[] arr = thing.split("\n");
        for (String string : arr) {
            System.out.println(string);
        }
    }   
}
