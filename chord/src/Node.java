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

    private Ip getIp(){
        return this.ip;
    }

    public Ip lookUp(int key){
        Node successor = this.findSuccessor(key);
        return successor.getIp();
    }

    private Node findSuccessor(int key) {
        for (Item item : this.items) {
            if (item.getKey() == key){
                return this;
            }
        }
        Node successor = this.fingerTable.getSuccessor(key);
        return successor.findSuccessor(key);
    }
}
