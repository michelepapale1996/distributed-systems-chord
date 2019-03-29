import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Node {
    private Ip ip;
    private int id;
    private int num_bits_identifiers;
    private Node successor;
    private Node predecessor;
    private boolean simpleLookupAlgorithm;
    private FingerTable fingerTable;
    private ArrayList<Item> items;
    private StabilizeHandler handler;

    public Node(Ip ip, int num_bits_identifiers,Boolean simpleKeyLocation) {
        this.handler = new StabilizeHandler(this);
        this.successor = null;
        this.predecessor = null;
        this.num_bits_identifiers = num_bits_identifiers;
        this.ip = ip;
        this.items = new ArrayList<>();
        this.simpleLookupAlgorithm = simpleKeyLocation;
        try {
            this.id = Sha1.getSha1(this.ip.getIp(), Integer.toString(this.num_bits_identifiers));
        } catch (NoSuchAlgorithmException e){
            this.id = -1;
            //TODO: 29/03/2019 handle exception
        }
        if (!simpleKeyLocation) this.fingerTable = new FingerTable(this , this.num_bits_identifiers);
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
        //check if the current node has the item
        if (this.hasItem(key)) return this;

        //otherwise find the successor that has the item
        try {
            Node successorForKey = this.findSuccessor(key);
            return successorForKey;
        }catch (NoSuchElementException e){
            System.out.println("Given item does not exists.");
            return null;
        }
    }

    //key must the hash of the key of the item, in module 2^N
    private Node findSuccessor(int key) throws NoSuchElementException{
        Node successor;
        if(this.simpleLookupAlgorithm){
            successor = this.successor;
        }else{
            successor = this.fingerTable.getSuccessor(key);
        }
        System.out.println("Il successore per idKey " + key + " partendo da " + this + " è:\n" + successor);

        if (this.isBetweenMyIdAndMySuccessorId(key, successor)){
            if (successor.hasItem(key)){
                return successor;
            }else{
                throw new NoSuchElementException();
            }
        }
        else {
            return successor.findSuccessor(key);
        }
    }

    private boolean isBetweenMyIdAndMySuccessorId(int key, Node successor){
        int successorId = successor.getId();
        int max_nodes = (int) Math.pow(2, this.num_bits_identifiers);
        if(this.id > successorId){
            successorId = (successorId + max_nodes);
            if(this.id > key){
                key = (key + max_nodes);
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

    public void setSuccessor(Node successor){
        this.successor = successor;
    }

    //create a new Chord ring
    public void create(){
        this.successor = this;
        this.predecessor = null;
        this.handler.start();
    }

    //join a Chord ring containing node
    public void join(Node node){
        this.predecessor = null;
        int key = node.getId();
        this.successor = node.findSuccessor(key);
        this.handler.start();
    }

    @Override
    public String toString() {
        return "Nodo con id: " + this.id + ", indirizzo ip: " + this.ip;
    }
}
