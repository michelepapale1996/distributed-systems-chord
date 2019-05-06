package chord;

import Utilities.Debugger;

import java.rmi.RemoteException;
import java.util.HashMap;

public class FingerTable {
    private Node owner;
    private int size;
    private HashMap<Integer,NodeInterface> map;

    public FingerTable(Node node, int size) {
        this.owner = node;
        this.size = size;
        this.map = new HashMap<>();
    }

    //given a key retrieve the successor
    public NodeInterface getClosestPrecedingNode(int key) throws RemoteException {
        int i;
        for (i = this.size - 1; i >= 0; i--){
            NodeInterface successor = this.map.get(getPosition(i));
            int finger = successor.getId();
            if (isBetween(finger, this.owner.getId(),key)){
                return successor;
            }
        }
        return this.owner;
    }

    private boolean isBetween(int key, int initial,int end){
        int maxNodes = (int) Math.pow(2,this.size);
        if (end < initial){
            end = end + maxNodes;
            if (key < initial){
                key = key + maxNodes;
            }
        }
        return key > initial && key < end;
    }

    public void setSuccessor(int key, NodeInterface successor){
        this.map.put(key, successor);
    }

    //initialize the finger table entry when a node create or join in a ring.
    public void initialize(NodeInterface node) {
        int bit;
        for (bit = 0; bit < this.size; bit++){
            this.map.put(getPosition(bit), node);
        }
    }

    //calculate id + 2^i
    public int getPosition(int i){
        int m = (int) Math.pow(2,this.size);
        i = (int) Math.pow(2,i);
        i = i + this.owner.getId();
        i = i % m;
        return i;
    }

    //given the key it returns the node of that position.
    public NodeInterface getNode(int key){
        return this.map.get(key);
    }

    public NodeInterface getOwner(){
        return this.owner;
    }

    public String print() throws RemoteException{
        StringBuilder builder = new StringBuilder();
        builder.append("==================== node " + this.owner.getId() + " ==================== \n"); //40
        int i;
        for (i = 0; i < this.size; i++){
            int index = this.getPosition(i);
            NodeInterface node = this.map.get(index);
            builder.append("          " + index + "                  " + node.print() + "\n");
        }
        return builder.toString();
    }

    //update the first line of the finger table due to the change of the successor.
    public void updateSuccessor(NodeInterface new_successor) {
        int i = 0;
        i = this.getPosition(i);
        Debugger.print("-Update finger table due to change of successor for " + this.owner.toString() + " with couple < " + i + ", " + new_successor.toString() + " >");
        this.map.put(i ,new_successor);
    }
}
