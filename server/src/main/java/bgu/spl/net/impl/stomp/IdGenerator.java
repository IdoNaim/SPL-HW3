package bgu.spl.net.impl.stomp;

public class IdGenerator {
    int i;
    private static class IdGeneratorHolder{
        private static IdGenerator instance = new IdGenerator();
    }
    private IdGenerator(){
        i = 0;
    }
    public static IdGenerator getInstance(){
        return IdGeneratorHolder.instance;
    }
    public int getId(){
        int k = this.i;
        i++;
        return k;
    }
}
