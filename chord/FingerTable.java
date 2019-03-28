import java.util.HashMap;

public class FingerTable {

    private int id;
    private int size;
    private HashMap<Integer,Node> map;

    public FingerTable(int id, int size) {
        this.id = id;
        this.size = size;
        this.map = new HashMap<>();
    }

    public Node getSuccessor(int key) {
        return map.get(key);
    }
}
