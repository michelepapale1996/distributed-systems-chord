package Test;

import chord.Item;
import chord.Node;
import chord.InfoNode;

import java.rmi.RemoteException;
import java.util.*;

public class TemporalComplexityLookUpTest {

    private static ArrayList <Node> nodesInTheNetwork = new ArrayList<>();
    private static ArrayList <Item> itemsInTheNetwork = new ArrayList<>();
    private static HashMap <Integer, Double> points = new HashMap<>();
    private static HashMap <Integer, Double> pointsAverage = new HashMap<>();
    private static HashMap <Integer, Integer> pointsAccess = new HashMap<>();
    private static HashMap <Integer, Integer> pointsAccessAverage = new HashMap<>();
    private static Parameters parameters = new Parameters(0);

    public static void main (String args[]) throws RemoteException, InterruptedException {
        //N = #lookUp M = #bits
        int N = 10000;
        int M = 10;
        int wait = 2;
        int bit;
        for (bit = 1; bit <= M; bit++){
            System.out.println("iteration for number of bits: " + bit);
            nodesInTheNetwork.clear();
            itemsInTheNetwork.clear();
            Node node0 = new Node();
            node0.setId(0);
            nodesInTheNetwork.add(node0);
            //create a chord full of nodes and items
            initialize(node0, bit, wait);
            //get the total time for do N lookup
            long totalTime = setOfLookUp(N);
            double averageTime = totalTime/N;

            points.put(bit,(double) totalTime);
            pointsAccess.put(bit, parameters.getCounter());
            //System.out.println("average time: " + averageTime);
            //System.out.println("average access: " + parameters.getCounter());
        }
        draw(points, pointsAccess);
    }

    private static void initialize (Node node, int bit, int wait) throws RemoteException, InterruptedException {
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
        long end = start + bit*wait*1000; //  *1000 ms
        while (System.currentTimeMillis() < end){}
        for (Node tmp : nodesInTheNetwork) {
            tmp.getHandler().stopTimer();
            new InfoNode(tmp);
        }
    }


    private static long setOfLookUp(int N) throws RemoteException {
        int i;
        long totalTime = 0;
        parameters.setCounter(0);
        for (i = 0; i < N; i++){
            Collections.shuffle(nodesInTheNetwork);
            Collections.shuffle(itemsInTheNetwork);
            Node node = nodesInTheNetwork.get(0);
            Item item = itemsInTheNetwork.get(0);
            //prints of Node that start the lookUp and Item searched.
            //System.out.println(node.print());
            //System.out.println(item.toString());

            long startTime = System.nanoTime();
            // TODO: 03/05/2019 fix try catch. and println
            try {
                node.lookUp(item.getKey());
                //System.out.println(parameters.getCounter());
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


    private static void draw(HashMap<Integer, Double> points, HashMap<Integer, Integer> pointsAccess) {
        int i;
        StringBuilder builder = new StringBuilder();
        builder.append("===================================================\n");
        int minNodes = 2;
        int maxNodes = (int) Math.pow(2,points.size());

        //used for draw average time points
        /*Collection<Double> times =  points.values();
        double maxTimes = Collections.max(times);
        double minTimes = Collections.min(times);
        for (i = 0; i < points.size(); i++){
            double size =  Math.pow(2, i+3);
            double time = points.get(i + 3);
            System.out.println("time : " + time + " nodes : " + size + "\n");
            builder.append("time : " + time + " nodes : " + size + "\n");
            StdDraw.setPenRadius(0.025);
            StdDraw.setPenColor(StdDraw.BLUE);
            double finalTime = (time - minTimes)/(maxTimes - minTimes);
            double x = (Math.pow(2, i+3) - minNodes)/(maxNodes - minNodes);
            builder.append("normalized nodes : " + x + " normalized time : " + finalTime + "\n");
            StdDraw.point(x, finalTime);
        }*/
        StdDraw.setPenRadius(0.025);
        StdDraw.setPenColor(StdDraw.BLUE);
        //StdDraw.setCanvasSize(800,800);

        builder.append("===================================================\n");
        Collection<Integer> accessList =  pointsAccess.values();
        int maxAccess = Collections.max(accessList);
        int minAccess = Collections.min(accessList);

        //StdDraw.setXscale(0, (double) maxNodes);
        //StdDraw.setYscale(0,(double) maxAccess);
        StdDraw.setScale(-.05, 1.05);

        //System.out.println("max access: " + maxAccess);
        //System.out.println("min access: " + minAccess);
        for (i = 0; i < pointsAccess.size(); i++){
            double size =  Math.pow(2, i+1);
            int access = pointsAccess.get(i + 1);
            System.out.println("access : " + access + " nodes : " + size + "\n");
            builder.append("access : " + access + " nodes : " + size + "\n");
            double tmp1 = (double) access - minAccess;
            System.out.println(tmp1);
            double tmp2 = (double) maxAccess - minAccess;
            System.out.println(tmp2);
            double normalizedAccess = tmp1/tmp2;
            System.out.println(normalizedAccess);
            double x = (Math.pow(2, i+1) - minNodes)/(maxNodes - minNodes);
            builder.append("normalized nodes : " + x + " normalized access : " + normalizedAccess + "\n");
            StdDraw.point(x, normalizedAccess);
            //StdDraw.point(size, (double) access);
        }
        System.out.println(builder);
    }
}
