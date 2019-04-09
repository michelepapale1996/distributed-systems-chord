package chord;

import Test.Debugger;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class Node extends UnicastRemoteObject implements NodeInterface, Serializable {
    private Item instance;
    private boolean simpleLookupAlgorithm;
    private int num_bits_identifiers;

    private int id;
    private NodeInterface successor;
    private NodeInterface predecessor;
    private FingerTable fingerTable;
    private ArrayList<Item> items;
    private Handler handler;
    private ArrayList<NodeInterface> successorList;
    private LinkedHashMap<Integer, ArrayList<Item>> successorItems;

    public Node(int num_bits_identifiers, Boolean simpleKeyLocation) throws RemoteException{
        this.handler = new Handler(this);
        this.successor = null;
        this.predecessor = null;
        this.num_bits_identifiers = num_bits_identifiers;
        this.instance = new Item("",1);
        this.items = new ArrayList<>();
        this.simpleLookupAlgorithm = simpleKeyLocation;
        this.successorList = new ArrayList<>(); //at the creatz2qion of the node is initialized an immediate successor list
        this.successorItems = new LinkedHashMap<>();
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ipAddress = address.getHostAddress();
            this.id = Sha1.getSha1(ipAddress, Integer.toString(this.num_bits_identifiers));
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (!simpleKeyLocation) this.fingerTable = new FingerTable(this , this.num_bits_identifiers);
    }

    //find the successor given the item's key
    //if the right node hasn't the item, lookup will return null
    public NodeInterface lookUp(int key) throws RemoteException, NoSuchElementException{
        return NodeLogic.lookUp(key, this);
    }

    //key must the hash of the key of the item, in module 2^N
    //boolean linear define if the findSuccessor is made in linear or logarithmic time
    @Override
    public NodeInterface findSuccessor(int key, Boolean linear) throws RemoteException{
        return NodeLogic.findSuccessor(key, linear, this);
    }

    //search for successor of item and store the item there
    public void storeItem(Item item) throws RemoteException, IllegalArgumentException{
        NodeLogic.storeItem(item, this);
    }

    //create a new Chord ring
    public void create() throws RemoteException {
        NodeLogic.create(this);
    }

    //join a Chord ring containing node
    //throw IllegalArgumentException if the ring of node already contains a node with the id of who wants to join
    public void join(NodeInterface node) throws RemoteException, IllegalArgumentException{
        NodeLogic.join(node, this);
    }

    public void addItem(Item item){
        Debugger.print("[Added item] -> " + this.print() + " now has the item " + item);
        this.items.add(item);
    }

    public NodeInterface getSuccessor() {
        return this.successor;
    }

    public void leave(){
        Debugger.print(this + "crashed");
        this.handler.stopTimer();
        this.instance = null;
    }

    public ArrayList<NodeInterface> getSuccessorList() {
        return successorList;
    }

    public LinkedHashMap<Integer, ArrayList<Item>> getSuccessorItems() {
        return successorItems;
    }

    public NodeInterface getPredecessor() {
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

    public Object getInstance(){
        return this.instance;
    }


    public ArrayList<Item> getItems() {
        return items;
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

    public FingerTable getFingerTable() {
        return fingerTable;
    }

    @Override
    public void setPredecessor(NodeInterface predecessor) {
        this.predecessor = predecessor;
    }

    public void setEntryFingerTable (int key, NodeInterface node){
        this.fingerTable.setSuccessor(key,node);
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public String toString() {
        return "[Node with id: " + this.id + "]";
    }

    @Override
    public String print(){return "[Node with id: " + this.id + "]";}

    public void setSuccessorList(ArrayList<NodeInterface> successorList) {
        this.successorList = successorList;
    }

    public void setSuccessorItems(LinkedHashMap<Integer, ArrayList<Item>> successorItems) {
        this.successorItems = successorItems;
    }

    //TODO: poi verrà eliminato
    public void setId(int id) {
        this.id = id;
    }

    public NodeInterface getNode(int key){
        return this.fingerTable.getNode(key);
    }
}
