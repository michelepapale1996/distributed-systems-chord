package chord;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ip implements Serializable{
    private String ip;

    //todo
    Ip(){
        try{
            InetAddress address = InetAddress.getLocalHost();
            this.ip = address.getHostAddress() ;
        }catch(UnknownHostException e ){
            e.printStackTrace();
        }
    }

    public String getIp() {
        return this.ip;
    }

    @Override
    public String toString() {
        return this.ip;
    }
}
