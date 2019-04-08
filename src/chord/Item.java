package chord;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class Item implements Serializable {
    
    private String name;
    private int key;

    public Item(String name, int module) {
        this.name = name;
        try{
            this.key = Sha1.getSha1(name, "" + module);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            this.key = -1;
        }

    }

    public int getKey(){
        return key;
    }

    @Override
    public String toString() {
        return "[Item with key: " + key + "]";
    }

    public void setKey(int key) {
        this.key = key;
    }
}
