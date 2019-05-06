package Test;

import chord.Item;
import chord.Node;
import chord.InfoNode;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.*;

public class TemporalComplexityLookUpTest {

    private static ArrayList <Node> nodesInTheNetwork = new ArrayList<>();
    private static ArrayList <Item> itemsInTheNetwork = new ArrayList<>();
    private static HashMap <Integer, Integer> pointsAccess = new HashMap<>();
    private static Parameters parameters = new Parameters(0);

    public static void main (String args[]) throws RemoteException{
        //N = #lookUp; M = #bits; wait = amount of time in addition to wait for each additional bit;
        int N = 10000;
        int M = 10;
        int wait = 2;
        int bit;
        Boolean simpleLookUpAlgorithm = true;
        for (bit = 1; bit <= M; bit++){
            System.out.println("iteration for number of bits: " + bit);
            nodesInTheNetwork.clear();
            itemsInTheNetwork.clear();
            Node node0 = new Node();
            node0.setId(0);
            nodesInTheNetwork.add(node0);
            //create a chord full of nodes and items
            initialize(node0, bit, wait, simpleLookUpAlgorithm);
            //do N lookup in the ring.
            setOfLookUp(N);
            pointsAccess.put(bit, parameters.getCounter());
        }
        draw(pointsAccess);
    }

    //create a full ring with all items.
    private static void initialize (Node node, int bit, int wait, boolean simpleLookUpAlgorithm) throws RemoteException {
        node.create(bit, simpleLookUpAlgorithm);
        int size = (int) Math.pow(2,bit);
        int i;
        for (i = 1; i < size; i++){
            Node node1 = new Node();
            Item item = new Item("test", bit);
            node1.setId(i);
            item.setKey(i);
            nodesInTheNetwork.add(node1);
            itemsInTheNetwork.add(item);
            node1.join(node);
            node1.storeItem(item);
        }
        long start = System.currentTimeMillis();
        long end = start + bit*wait*1000; //  *1000 ms
        while (System.currentTimeMillis() < end){}
        for (Node tmp : nodesInTheNetwork) {
            tmp.getHandler().stopTimer();
            new InfoNode(tmp);
        }
    }

    //do N lookUp in the ring initialized in initialize method.
    private static void setOfLookUp(int N) throws RemoteException {
        int i;
        parameters.setCounter(0);
        for (i = 0; i < N; i++){
            Collections.shuffle(nodesInTheNetwork);
            Collections.shuffle(itemsInTheNetwork);
            Node node = nodesInTheNetwork.get(0);
            Item item = itemsInTheNetwork.get(0);
            try {
                node.lookUp(item.getKey());
            }catch (NoSuchElementException e){
                for (Node tmp : nodesInTheNetwork) {
                    new InfoNode(tmp);
                }
                throw new NoSuchElementException("Element not found. Restart the program.");
            }
        }
    }

    //plot the total number of visits of lookUp and the number of nodes in the ring.
    private static void draw(HashMap<Integer, Integer> pointsAccess) {
        int i;
        StringBuilder builder = new StringBuilder();
        builder.append("===================================================\n");
        int maxNodes = (int) Math.pow(2,pointsAccess.size());
        StdDraw.setPenRadius(0.020);
        Collection<Integer> accessList =  pointsAccess.values();
        int maxAccess = Collections.max(accessList);
        StdDraw.setXscale(-180, maxNodes + 180);
        StdDraw.setYscale(0, maxAccess + 10000);
        Font font = new Font("Arial", Font.BOLD, 12);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(maxNodes/2, 1000, "NUMBER OF NODES");
        StdDraw.text(-125, maxAccess/2 + 5000, "NUMBER OF VISITS", 90);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (i = 0; i < pointsAccess.size(); i++){
            int size = (int) Math.pow(2, i+1);
            int access = pointsAccess.get(i + 1);
            builder.append("access : " + access + " nodes : " + size + "\n");
            StdDraw.point(size, access);
            StdDraw.text(size,access + 2000, "( " + size + ", " + access + " )");
        }
        System.out.println(builder);
    }
}
