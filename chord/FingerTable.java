import java.util.HashMap;

public class FingerTable {
    private Node owner;
    private int size;
    private HashMap<Integer,Node> map;

    public FingerTable(Node node, int size) {
        this.owner = node;
        this.size = size;
        this.map = new HashMap<>();
    }

    public Node getSuccessor(int key){
        int i;
        int m = (int) Math.pow(2,this.size);
        for (i = this.size; i >= 1; i--){
            int tmp = (int) Math.pow(2,i);
            tmp = tmp + this.owner.getId();
            tmp = tmp % m;
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

    public void setSuccessor(int key, Node successor){
        this.map.put(key, successor);
    }

    private boolean isBetween(int key, int n,int tmp){
        return key > n && key < tmp;
    }
}
