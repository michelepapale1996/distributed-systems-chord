package chord;

import middleware.NodeInterface;

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
    public NodeInterface getSuccessor(int key){
        int i;
        for (i = 0; i < this.size; i++){
            int tmp = this.getPosition(i);
            if (tmp < this.owner.getId()) {
                int a = tmp + (int) Math.pow(2,this.size);
                if (isBetween(key,this.owner.getId(),a)){
                    return this.map.get(tmp);
                }
            }
            if (isBetween(key,this.owner.getId(),tmp)){
                return this.map.get(tmp);
            }
        }
        return this.owner;
    }

    private boolean isBetween(int key, int n,int tmp){
        return key >= n && key <= tmp;
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
    private int getPosition(int i){
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
}
