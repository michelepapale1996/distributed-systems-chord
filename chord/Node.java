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
    private Handler handler;

    public Node(int num_bits_identifiers, Boolean simpleKeyLocation) {
        this.handler = new Handler(this);
        this.successor = null;
        this.predecessor = null;
        this.num_bits_identifiers = num_bits_identifiers;
        this.ip = new Ip();
        this.items = new ArrayList<>();
        this.simpleLookupAlgorithm = simpleKeyLocation;
        try {
            this.id = Sha1.getSha1(this.ip.getIp(), Integer.toString(this.num_bits_identifiers));
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        if (!simpleKeyLocation) this.fingerTable = new FingerTable(this , this.num_bits_identifiers);
    }

    //find the successor given the item's key
    //if the right node hasn't the item, lookup will return null
    public Node lookUp(int key){
        System.out.println("Finding key " + key + " starting from " + this);
        //check if the current node has the item
        if (this.hasItem(key)) return this;

        //otherwise find the successor that has the item
        Node successorForKey = this.findSuccessor(key);
        if (successorForKey.hasItem(key)){
            return successorForKey;
        } else {
            System.out.println("Given item does not exists.");
            throw new NoSuchElementException();
        }
    }

    //key must the hash of the key of the item, in module 2^N
    public Node findSuccessor(int key){
        Node successor;
        if(this.simpleLookupAlgorithm){
            successor = this.successor;
        }else{
            successor = this.fingerTable.getSuccessor(key);
        }
        if (this.isBetween(key, successor.getId()) || this.getId() == successor.getId()){
            return successor;
        }else{
            return successor.findSuccessor(key);
        }
    }

    public boolean isBetween(int item_searched, int end_of_interval){
        int start_interval = this.id;
        int max_nodes = (int) Math.pow(2, this.num_bits_identifiers);
        /*if(this.id > end_of_interval){
            end_of_interval = end_of_interval + max_nodes;
            if(this.id > item_searched){
                item_searched = item_searched + max_nodes;
            }
        }*/
        if (start_interval > end_of_interval) {
            if (item_searched <= end_of_interval){
                item_searched = item_searched + max_nodes;
            }
            end_of_interval = end_of_interval + max_nodes;
        }
        return (item_searched > start_interval && item_searched <= end_of_interval);
    }

    //create a new Chord ring
    public void create(){
        this.successor = this;
        this.predecessor = null;
        if (!this.isSimpleLookupAlgorithm()) {
            this.fingerTable.initialize(this);
        }
        this.handler.start();
    }

    //join a Chord ring containing node
    public void join(Node node){
        System.out.println(this + "'s predecessor is null");
        this.predecessor = null;

        //if node has as successor himself, he is the only one in the ring -> he becomes my successor
        if(node == node.getSuccessor()){
            System.out.println(this + " joined and successor is: " + node);
            this.successor = node;
        }
        else{
            int key = this.getId();
            System.out.println(this + " joined and successor is: " + node.findSuccessor(key));
            this.successor = node.findSuccessor(key);
        }
        if (!this.isSimpleLookupAlgorithm()) {
            this.fingerTable.initialize(successor);
        }
        //start tasks to stabilize node
        this.handler.start();
    }

    public void addItem (Item item){
        this.items.add(item);
    }

    public Node getSuccessor() {
        return successor;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public int getId() {
        return this.id;
    }

    public boolean isSimpleLookupAlgorithm() {
        return simpleLookupAlgorithm;
    }

    public int getNum_bits_identifiers() {
        return num_bits_identifiers;
    }

    public Ip getIp(){
        return this.ip;
    }

    private boolean hasItem(int key) {
        for(Item i: this.items){
            if(i.getKey() == key) return true;
        }
        return false;
    }

    public void setSuccessor(Node successor){
        this.successor = successor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public void setHandler() {
        this.handler = new Handler(this);
    }

    public void setEntryFingerTable (int key, Node node){
        this.fingerTable.setSuccessor(key,node);
    }

    @Override
    public String toString() {
        return "[Node with id: " + this.id + "]";
    }

    //TODO: poi verrà eliminato
    public void setId(int id) {
        this.id = id;
    }

    public Node getNode(int key){
        return this.fingerTable.getNode(key);
    }

    // TODO: 02/04/2019 sistemare le funzioni di findSuccessor
    public Node findSuccessorLinear(int key) {
        Node successor;
        successor = this.successor;
        if (this.isBetween(key, successor.getId()) || key == successor.getId()){
            return successor;
        }else{
            if (successor.findSuccessor(key) == null){
                return successor;
            }
            else {
                return successor.findSuccessor(key);
            }
        }
    }
}
