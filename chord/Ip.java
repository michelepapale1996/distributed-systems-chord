/**
 * Created by andrea on 28/03/2019.
 */
public class Ip {
    private String ip;

    Ip(String prova){
        this.ip = prova;
    }

    public String getIp() {
        return this.ip;
    }

    @Override
    public String toString() {
        return this.ip;
    }
}
