package chord;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Node extends ArrayList<Node> {
    private Ip ip;
    private int id;
    private int num_bits_identifiers;
    private Node successor;
    private Node predecessor;
    private boolean simpleLookupAlgorithm;
    private FingerTable fingerTable;
    private ArrayList<Item> items;
    private Handler handler;
    private ArrayList<Node> successorList;
    private LinkedHashMap<Integer, ArrayList<Item>> successoreItems;

    public Node(int num_bits_identifiers, Boolean simpleKeyLocation) {
        this.handler = new Handler(this);
        this.successor = null;
        this.predecessor = null;
        this.num_bits_identifiers = num_bits_identifiers;
        this.ip = new Ip();
        this.items = new ArrayList<>();
        this.simpleLookupAlgorithm = simpleKeyLocation;
        this.successorList = new ArrayList<>(); //at the creatz2qion of the node is initialized an immediate successor list
        this.successoreItems = new LinkedHashMap<>();
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
        Node successorForKey = this.findSuccessor(key, this.simpleLookupAlgorithm);
        if (successorForKey.hasItem(key)){
            return successorForKey;
        } else {
            System.out.println("Given item does not exists.");
            throw new NoSuchElementException();
        }
    }

    //key must the hash of the key of the item, in module 2^N
    //boolean linear define if the findSuccessor is made in linear or logarithmic time
    public Node findSuccessor(int key, Boolean linear){
        Node successor;
        if(linear){
            successor = this.successor;
        }else{
            successor = this.fingerTable.getSuccessor(key);
        }
        if (this.isBetween(key, successor.getId()) || this.getId() == successor.getId()){
            return successor;
        }else{
            return successor.findSuccessor(key, linear);
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
        System.out.println(this + "'s successor is " + this);
        System.out.println(this + "'s predecessor is null");
        if (!this.isSimpleLookupAlgorithm()) {
            this.fingerTable.initialize(this);
        }
        this.handler.start();
    }

    //join a Chord ring containing node
    public void join(Node node){
        System.out.println(this + " join ring");
        System.out.println(this + "'s predecessor is null");
        this.predecessor = null;

        //if node has as successor himself, he is the only one in the ring -> he becomes my successor
        if(node == node.getSuccessor()){
            System.out.println(this + " joined and successor is: " + node);
            this.successor = node;
            this.getSuccessorList().add(node);
            node.getSuccessorList().add(this);
        }
        else{
            int key = this.getId();
            System.out.println(this + " joined and successor is: " + node.findSuccessor(key, this.simpleLookupAlgorithm));
            Node successor = node.findSuccessor(key, this.simpleLookupAlgorithm);
            this.successor = successor;
            this.getSuccessorList().add(successor);
            System.out.println("Added " + successor + " to successor list of " + this);
        }
        if (!this.isSimpleLookupAlgorithm()) {
            this.fingerTable.initialize(successor);
        }
        //start tasks to stabilize node
        this.handler.start();
    }

    public void leave(){
        System.out.println(this + "crashed");
        this.handler.stopTimer();
        this.handler = null;
        this.successor = null;
        this.predecessor = null;
        this.ip = null;
        this.items = null;
        this.successorList = null;
        this.successoreItems = null;
    }
    //search for successor of item and store the item there
    public void storeItem(Item item){
        Node node = findSuccessor(item.getKey(), true);
        node.getItems().add(item);
        System.out.println("Stored Item at node " + node);
    }

    public void addItem (Item item){
        this.items.add(item);
    }

    public ArrayList<Node> getSuccessorList() {
        return successorList;
    }

    public LinkedHashMap<Integer, ArrayList<Item>> getSuccessoreItems() {
        return successoreItems;
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

    public ArrayList<Item> getItems() {
        return items;
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

    public void setSuccessorList(ArrayList<Node> successorList) {
        this.successorList = successorList;
    }

    public void setSuccessoreItems(LinkedHashMap<Integer, ArrayList<Item>> successoreItems) {
        this.successoreItems = successoreItems;
    }

    //TODO: poi verrà eliminato
    public void setId(int id) {
        this.id = id;
    }

    public Node getNode(int key){
        return this.fingerTable.getNode(key);
    }

    public FingerTable getFingerTable() {
        return fingerTable;
    }
}
