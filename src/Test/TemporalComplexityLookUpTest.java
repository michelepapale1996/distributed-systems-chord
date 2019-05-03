package Test;

import chord.Item;
import chord.Node;
import chord.InfoNode;

import java.rmi.RemoteException;
import java.util.*;

public class TemporalComplexityLookUpTest {

    private static ArrayList <Node> nodesInTheNetwork = new ArrayList<>();
    private static ArrayList <Item> itemsInTheNetwork = new ArrayList<>();
    private static HashMap <Integer, Long> points = new HashMap<>();

    public static void main (String args[]) throws RemoteException, InterruptedException {
        int N = 1000;
        int M = 8;
        int bit;
        for (bit = 3; bit <= M; bit++){
            nodesInTheNetwork.clear();
            itemsInTheNetwork.clear();
            Node node0 = new Node();
            node0.setId(0);
            nodesInTheNetwork.add(node0);
            //create a chord full of nodes and items
            initialize(node0, bit);
            //get the total time for do N lookup
            long totalTime = setOfLookUp(N);
            points.put(bit,totalTime/N);
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
        long end = start + 20*1000; //  *1000 ms
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
            //prints of Node that start the lookUp and Item searched.
            //System.out.println(node.print());
            //System.out.println(item.toString());

            long startTime = System.nanoTime();
            try {
                node.lookUp(item.getKey());
            }catch (NoSuchElementException e){
                for (Node tmp : nodesInTheNetwork) {
                    new InfoNode(tmp);
                }
                throw new NoSuchElementException("Element not found");
            }
            long endTime = System.nanoTime();
            totalTime = totalTime + endTime - startTime;
            //System.out.println(totalTime);
        }
        return totalTime;
    }


    private static void draw(HashMap<Integer, Long> points) {
        StringBuilder builder = new StringBuilder();
        builder.append("===================================================\n");
        int i;
        int maxNodes = (int) Math.pow(2,points.size() + 2);
        Collection<Long> times =  points.values();
        long maxTimes = Collections.max(times);
        for (i = 0; i < points.size(); i++){
            Double size =  Math.pow(2, i+2);
            Long time = points.get(i + 3);
            System.out.println("time : " + time + " nodes : " + size + "\n");
            builder.append("time : " + time + " nodes : " + size + "\n");
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLUE);
            time = time / maxTimes;
            StdDraw.point(time.doubleValue(), size/maxNodes);
        }
        System.out.println(builder);
    }
}
