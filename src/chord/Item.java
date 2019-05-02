package chord;

import java.io.Serializable;

public class Item implements Serializable {
    
    private String name;
    private int key;

    public Item(String name, int module) {
        this.name = name;
        this.key = Sha1.getSha1(name, "" + module);
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
