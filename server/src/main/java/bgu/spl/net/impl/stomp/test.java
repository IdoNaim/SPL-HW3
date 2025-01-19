package bgu.spl.net.impl.stomp;

public class test {
    public static void main(String[] args){
        String thing = "/hello/sigma";
        String[] arr = thing.split("/");
        for (String string : arr) {
            System.out.println(string);
        }
    }   
}
