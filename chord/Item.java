import java.security.NoSuchAlgorithmException;

/**
 * Created by andrea on 28/03/2019.
 */
public class Item {
    
    private String name;
    private int key;

    public Item(String name, String module) {
        this.name = name;
        try{
            this.key = Sha1.getSha1(name, module);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            this.key = -1;
        }

    }

    public int getKey() {
        return key;
    }
}
