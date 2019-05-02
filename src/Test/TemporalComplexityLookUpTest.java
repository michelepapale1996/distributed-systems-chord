package Test;

import chord.Item;
import chord.Node;
import chord.InfoNode;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TemporalComplexityLookUpTest {

    private static ArrayList <Node> nodesInTheNetwork = new ArrayList<>();
    private static ArrayList <Item> itemsInTheNetwork = new ArrayList<>();
    private static HashMap <Integer, Long> points = new HashMap<>();

    public static void main (String args[]) throws RemoteException, InterruptedException {
        int bit;
        int N = 100;
        for (bit = 2; bit <= 5; bit++){
            nodesInTheNetwork.clear();
            itemsInTheNetwork.clear();
            Node node0 = new Node();
            node0.setId(0);
            nodesInTheNetwork.add(node0);
            //create a chord full of nodes and items
            initialize(node0, bit);
            //get the total time for do N lookup
            long totalTime = setOfLookUp(N);
            points.put(bit,totalTime);
        }
        draw(points);
    }

    private static void initialize (Node node, int bit) throws RemoteException, InterruptedException {
        node.create(bit, false);
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
            //Thread.sleep(200);
        }
        long start = System.currentTimeMillis();
        long end = start + 30*1000; //  *1000 ms
        while (System.currentTimeMillis() < end){}
        for (Node tmp : nodesInTheNetwork) {
            new InfoNode(tmp);
        }
    }


    private static long setOfLookUp(int N) throws RemoteException {
        int i;
        long totalTime = 0;
        for (i = 0; i < N; i++){
            Collections.shuffle(nodesInTheNetwork);
            Collections.shuffle(itemsInTheNetwork);
            Node node = nodesInTheNetwork.get(0);
            Item item = itemsInTheNetwork.get(0);
            System.out.println(node.print());
            System.out.println(item.toString());

            long startTime = System.currentTimeMillis();
            node.lookUp(item.getKey());
            long endTime = System.currentTimeMillis();
            totalTime = totalTime + endTime - startTime;
            System.out.println(totalTime);
        }
        return totalTime;
    }


    private static void draw(HashMap<Integer, Long> points) {
        StringBuilder builder = new StringBuilder();
        builder.append("===================================================\n");
        int i;
        for (i = 0; i < points.size(); i++){
            int size = (int) Math.pow(2, i+3);
            long time = points.get(i + 2);
            builder.append("time : " + time + " nodes : " + size + "\n");
        }
        System.out.println(builder);
    }
}
