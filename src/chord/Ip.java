public class Ip {
    private String ip;

    //todo
    Ip(){
        this.ip = "192.168.1.1";
    }

    public String getIp() {
        return this.ip;
    }

    @Override
    public String toString() {
        return this.ip;
    }
}
