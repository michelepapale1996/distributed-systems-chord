import java.util.ArrayList;

/**
 * Created by andrea on 28/03/2019.
 */
public class Node {
    private Ip ip;
    private int id;
    private FingerTable fingerTable;
    private ArrayList<Item> items;

    public Node(Ip ip) {
        this.ip = ip;
        // TODO: 28/03/2019 initialize id and handle size.
        int size = 1;
        this.fingerTable = new FingerTable(this.id,size);
        this.items = new ArrayList<>();
    }

    public void addItem (Item item){
        this.items.add(item);
    }

    public int getId() {
        return id;
    }

    private Ip getIp(){
        return this.ip;
    }

    public Ip lookUp(int key){
        if (this.hasItem(key)){
            return this.ip;
        }
        Node successor = null;
        try {
            successor = this.findSuccessor(key);
            return successor.getIp();
        } catch (Exception e) {
            // TODO: 28/03/2019 handle this exception of file not found
            e.printStackTrace();
            return null;
        }
    }

    private Node findSuccessor(int key) throws Exception{
        Node successor = this.fingerTable.getSuccessor(key);
        if (key > this.id && key <= successor.getId()){
            if (successor.hasItem(key)){
                return successor;
            }
            else {
                throw new Exception("Item not found");
            }
        }
        else {
            return successor.findSuccessor(key);
        }
    }

    private boolean hasItem(int key) {
        for (Item item : this.items) {
            if (item.getKey() == key){
                return true;
            }
        }
        return false;
    }
}
