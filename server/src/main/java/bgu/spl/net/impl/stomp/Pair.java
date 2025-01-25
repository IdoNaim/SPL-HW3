package bgu.spl.net.impl.stomp;

public class Pair<T,P> {
    T first;
    P second;
    public Pair(T first, P second){
        this.first = first;
        this.second = second;
    }
    public T getFirst(){
        return first;
    }
    public P getSecond(){
        return second;
    }
    
}
