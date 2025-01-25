package bgu.spl.net.impl.stomp;

public class Message<T> {
    T msg;
    public Message(T msg){
        this.msg = msg;
    }
    public T getMsg(){
        return msg;
    }
    
}
