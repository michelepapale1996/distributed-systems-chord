package Test;

import Utilities.CheckInput;
import Utilities.Debugger;
import chord.InfoNode;
import chord.Item;
import chord.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.*;


public class RandomTest{
    static Random random = new Random();
    public static void main(String args[]) throws RemoteException{
        Debugger.setDebug(false);
        ArrayList<Node> nodesInTheNetwork = new ArrayList<>();
        ArrayList<Item> itemsInTheNetwork = new ArrayList<>();

        Node node0 = new Node();

        int numBitsId = 2;
        Boolean simpleLookUpAlgorithm = false;
        node0.setId(0);
        node0.create(numBitsId, simpleLookUpAlgorithm);
        nodesInTheNetwork.add(node0);

        Timer timer1 = new Timer();

        long start = System.currentTimeMillis();
        long end = start + 20*1000; //  *1000 ms

        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                try {
                    random.setSeed(System.currentTimeMillis());
                    runAction(nodesInTheNetwork, itemsInTheNetwork);
                } catch (RemoteException | InterruptedException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        timer1.schedule(timerTask1, 0,1000);

        while (System.currentTimeMillis() < end) {}
        timer1.cancel();

        menu(nodesInTheNetwork, itemsInTheNetwork);
    }

    private static void runAction(ArrayList<Node> nodes, ArrayList<Item> items) throws RemoteException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {
        int i = RandomTest.random.nextInt(4);
        Collections.shuffle(nodes);
        switch (i) {
            case 1 : join(nodes.get(0), nodes, items); break;
            case 2 : storeItems(nodes.get(0), items, nodes); break;
            case 3 : exit(nodes, nodes.get(0), items); break;
        }
    }

    private static void join(Node node, ArrayList<Node> nodes, ArrayList<Item> items ) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, RemoteException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        int size = (int) Math.pow(2,node.getRing().getNum_bits_identifiers());

        Random random = new Random();
        int idRandomNode = random.nextInt(size);

        Class<?> clazz = Class.forName("chord.Node");
        Constructor<?> ctor = clazz.getConstructor();
        Node newNode = (Node) ctor.newInstance();
        newNode.setId(idRandomNode);
        System.out.println(ANSI_GREEN + newNode.print() + " is trying to join the network contacting " + node.print() + ANSI_RESET);
        try {
            newNode.join(node);
            nodes.add(newNode);
            System.out.println(ANSI_GREEN + newNode.print() + " join the network" + ANSI_RESET);
            print(nodes, items);
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    private static void storeItems(Node node, ArrayList<Item> items, ArrayList<Node> nodes) throws RemoteException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        Class<?> clazz = Class.forName("chord.Item");
        Class[] cArg = new Class[2];
        cArg[0] = String.class;
        cArg[1] = int.class;
        String s = randomAlphaNumeric(10);
        int i = node.getRing().getNum_bits_identifiers();
        Item item = (Item) clazz.getDeclaredConstructor(cArg).newInstance(s,i);
        try {
            /*for (Item it : items) {
                if (it.getKey() == item.getKey()) return;
            }*/
            node.storeItem(item);
            items.add(item);
            System.out.println(ANSI_GREEN + "Added " + item.toString() + ANSI_RESET);
            print(nodes, items);
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    private static void exit(ArrayList<Node> nodes, Node node0, ArrayList<Item> items) throws RemoteException, InterruptedException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        ArrayList<Node> copyOfNodes = new ArrayList<>(nodes);
        Collections.shuffle(copyOfNodes);
        Node node = copyOfNodes.get(0);
        if (nodes.size()>0 && node != node0){
            System.out.println(ANSI_GREEN + "Exit of " + node.print() + ANSI_RESET);
            node.exitFromRing();
            nodes.remove(node);
            print(nodes, items);
        }
    }

    private static void menu(ArrayList<Node> nodes, ArrayList<Item> items){
        boolean flag = true;
        while(flag){
            System.out.print("[");
            for (Node nod: nodes) {
                System.out.print(nod.print());
            }
            System.out.println("]");
            System.out.println(items);
            System.out.println("Insert the id of the node to check:");
            int id = CheckInput.getInt();
            Node chosen = null;
            for (Node nod : nodes) {
                if (nod.getId() == id) {
                    chosen = nod;
                }
            }
            try{
                InfoNode.show(chosen);
            }catch (NullPointerException e){
            }
        }
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private static void print(ArrayList<Node> nodes, ArrayList<Item> items){
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";
        System.out.print(ANSI_YELLOW);// + nodesInTheNetwork + ANSI_RESET);
        System.out.print("[");
        for (Node nod : nodes) {
            System.out.print(nod.print());
        }
        System.out.print("]");
        System.out.print("\n");
        System.out.println(ANSI_YELLOW + items + ANSI_RESET);
    }
}