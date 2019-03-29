import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Node {
    private Ip ip;
    private int id;
    private int num_bits_identifiers;
    private Node successor;
    private Node predecessor;
    private FingerTable fingerTable;
    private ArrayList<Item> items;

    public Node(Ip ip, int num_bits_identifiers,Boolean simpleKeyLocation) {
        this.successor = null;
        this.predecessor = null;
        this.num_bits_identifiers = num_bits_identifiers;
        this.ip = ip;
        try {
            this.id = Sha1.getSha1(this.ip.getIp(), Integer.toString(this.num_bits_identifiers));
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            this.id = -1;
            //TODO: 29/03/2019 handle exception
        }
        if (simpleKeyLocation){
            this.fingerTable = new FingerTable(this,1);
        }
        else {
            //todo: log of max_num_of_nodes
            this.fingerTable = new FingerTable(this ,this.num_bits_identifiers);
        }
        this.items = new ArrayList<>();
    }

    public void addItem (Item item){
        this.items.add(item);
    }

    public int getId() {
        return this.id;
    }

    public Ip getIp(){
        return this.ip;
    }

    //to find the successor given the item's key
    //if the right node has not the item, lookup will return null
    public Node lookUp(int key){
        System.out.println("Ricerco chiave " + key + " partendo da " + this);
        if (this.hasItem(key)){
            return this;
        }
        Node successor = null;
        try {
            successor = this.findSuccessor(key);
            return successor;
        } catch (Exception e) {
            // TODO: 28/03/2019 handle this exception of file not found
            return null;
        }
    }

    //key is the hash of the key of the item, in module 2^N
    private Node findSuccessor(int key) throws Exception{
        Node successor = this.fingerTable.getSuccessor(key);
        System.out.println("Il successore per idKey " + key + " partendo da " + this + " è:\n" + successor);

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
            successorId = (int) (successorId + this.num_bits_identifiers);
            if(this.id > key){
                key = (int) (key + this.num_bits_identifiers);
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

    public void create(){

    }

    @Override
    public String toString() {
        return "Nodo con id: " + this.id + ", indirizzo ip: " + this.ip;
    }
}
