package chord;

import middleware.NodeInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Node extends UnicastRemoteObject implements NodeInterface, Serializable {
    private Ip ip;
    private int id;
    private int num_bits_identifiers;
    private NodeInterface successor;
    private NodeInterface predecessor;
    private boolean simpleLookupAlgorithm;
    private FingerTable fingerTable;
    private ArrayList<Item> items;
    private Handler handler;

    public Node(int num_bits_identifiers, Boolean simpleKeyLocation) throws RemoteException{
        this.handler = new Handler(this);
        this.successor = null;
        this.predecessor = null;
        this.num_bits_identifiers = num_bits_identifiers;
        this.ip = new Ip();
        this.items = new ArrayList<>();
        this.simpleLookupAlgorithm = simpleKeyLocation;
        try {
            this.id = Sha1.getSha1(this.ip.getAddress(), Integer.toString(this.num_bits_identifiers));
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        if (!simpleKeyLocation) this.fingerTable = new FingerTable(this , this.num_bits_identifiers);
    }

    //find the successor given the item's key
    //if the right node hasn't the item, lookup will return null
    public NodeInterface lookUp(int key) throws RemoteException{
        System.out.println("Finding key " + key + " starting from " + this);
        //check if the current node has the item
        if (this.hasItem(key)) return this;

        //otherwise find the successor that has the item
        NodeInterface successorForKey = this.findSuccessor(key, this.simpleLookupAlgorithm);
        if (successorForKey.hasItem(key)){
            return successorForKey;
        } else {
            System.out.println("Given item does not exists.");
            throw new NoSuchElementException();
        }
    }

    //key must the hash of the key of the item, in module 2^N
    //boolean linear define if the findSuccessor is made in linear or logarithmic time
    @Override
    public NodeInterface findSuccessor(int key, Boolean linear) throws RemoteException{
        NodeInterface successor;
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

    //search for successor of item and store the item there
    public void storeItem(Item item) throws RemoteException{
        NodeInterface node = findSuccessor(item.getKey(), true);
        node.addItem(item);
        System.out.println(node.print() + " now has the item " + item);
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
    public void join(NodeInterface node) throws RemoteException{
        System.out.println(this + " join ring");
        System.out.println(this + "'s predecessor is null");
        this.predecessor = null;

        //if node has as successor himself, he is the only one in the ring -> he becomes my successor
        if(node == node.getSuccessor()){
            System.out.println(this.print() + " joined and successor is: " + node.print());
            this.successor = node;
        }
        else{
            int key = this.getId();
            this.successor = node.findSuccessor(key, this.simpleLookupAlgorithm);
            System.out.println(this + " joined and successor is: " + this.successor.print());
        }
        if (!this.isSimpleLookupAlgorithm()) {
            this.fingerTable.initialize(successor);
        }
        //start tasks to stabilize node
        this.handler.start();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item){
        System.out.println(this.print() + "now has the item" + item);
        this.items.add(item);
    }

    public NodeInterface getSuccessor() {
        return successor;
    }

    public NodeInterface getPredecessor() {
        return predecessor;
    }

    @Override
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

    public boolean hasItem(int key) {
        for(Item i: this.items){
            if(i.getKey() == key) return true;
        }
        return false;
    }

    public void setSuccessor(NodeInterface successor){
        this.successor = successor;
    }

    @Override
    public void setPredecessor(NodeInterface predecessor) {
        this.predecessor = predecessor;
    }

    public void setEntryFingerTable (int key, NodeInterface node){
        this.fingerTable.setSuccessor(key,node);
    }

    @Override
    public String toString() {
        return "[Node with id: " + this.id + "]";
    }

    @Override
    public String print(){return "[Node with id: " + this.id + "]";}

    //TODO: poi verrà eliminato
    public void setId(int id) {
        this.id = id;
    }

    public NodeInterface getNode(int key){
        return this.fingerTable.getNode(key);
    }
}
