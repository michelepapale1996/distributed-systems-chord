import java.util.HashMap;
import java.util.Set;

public class FingerTable {
    private int id;
    private int size;
    private HashMap<Integer,Node> map;

    public FingerTable(int id, int size) {
        this.id = id;
        this.size = size;
        this.map = new HashMap<>();
    }

    public Node getSuccessor(int key){
        Set<Integer> keys = map.keySet();
        for(Integer k: keys){
            System.out.println(k);
            return map.get(k);
        }
        return null;
    }

    public void setSuccessor(int key, Node successor){
        this.map.put(key, successor);
    }
}
