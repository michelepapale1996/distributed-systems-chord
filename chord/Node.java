import java.util.ArrayList;

public class Node {
    private Ip ip;
    private int id;
    private int num_bits_identifier;
    private FingerTable fingerTable;
    private ArrayList<Item> items;

    public Node(Ip ip, int num_bits_identifier) {
        this.num_bits_identifier = num_bits_identifier;
        this.num_bits_identifier = 4;
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

    //key is the hash of the key of the item, in module 2^N
    private Node findSuccessor(int key) throws Exception{
        Node successor = this.fingerTable.getSuccessor(key);

        if (this.isBetweenMyIdAndMySuccessorId(key, successor)){
            if (successor.hasItem(key)){
                return successor;
            }else{
                throw new Exception("Item not found");
            }
        }
        else {
            return successor.findSuccessor(key);
        }
    }

    private boolean isBetweenMyIdAndMySuccessorId(int key, Node successor){
        int successorId = successor.getId();
        if(this.id > successorId){
            successorId = (int) (successorId + Math.pow(2, this.num_bits_identifier));
            if(this.id > key){
                key = (int) (key + Math.pow(2, this.num_bits_identifier));
            }
        }
        return (key > this.id && key <= successorId);
    }

    private boolean hasItem(int key) {
        for (Item item : this.items) {
            if (item.getKey() == key){
                return true;
            }
        }
        return false;
    }

    public void setSuccessor(int key, Node successor){
        this.fingerTable.setSuccessor(key, successor);
    }
}
