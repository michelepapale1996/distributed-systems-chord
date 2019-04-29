package Test;

import chord.Item;
import chord.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RandomTest {
    public static void main(String args[]) throws RemoteException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";

        Debugger.setDebug(true);
        ArrayList<Node> nodesInTheNetwork = new ArrayList<>();
        ArrayList<Item> itemsInTheNetwork = new ArrayList<>();

        Node node0 = new Node();
        node0.setId(0);
        node0.create(256, true);


        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= Math.pow(2, 16); i++) {
            list.add(i);
        }

        List<Integer> actions = new ArrayList<Integer>();
        for (int i = 1; i <= 3; i++) {
            actions.add(i);
        }

        boolean b = true;
        while(b) {
            Collections.shuffle(actions);
            switch (actions.get(0)) {

                case 1 : join(list, node0, nodesInTheNetwork); break;
                case 2 : storeItems(list, node0, itemsInTheNetwork); break;
                //case 3 : join(list, node0, nodesInTheNetwork); break;
                case 4 : exit(nodesInTheNetwork); break;
            }
            System.out.println(ANSI_YELLOW + "-----" + nodesInTheNetwork + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "-----" + itemsInTheNetwork + ANSI_RESET);
            Thread.sleep(500);
        }
    }

    private static void join(List list, Node node, ArrayList<Node> nodes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, RemoteException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        Collections.shuffle(list);
        Class<?> clazz = Class.forName("chord.Node");
        Constructor<?> ctor = clazz.getConstructor();
        Node newNode = (Node) ctor.newInstance();
        newNode.setId((Integer) list.get(0));
        for (Node n: nodes) {
            if (n.getId() == newNode.getId()) return;
        }
        nodes.add(newNode);
        newNode.join(node);
        System.out.println(ANSI_GREEN + "-----" + newNode + " join the network" + ANSI_RESET);
    }

    private static void storeItems(List list, Node node, ArrayList<Item> items) throws RemoteException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        Collections.shuffle(list);
        Class<?> clazz = Class.forName("chord.Item");
        Class[] cArg = new Class[2];
        cArg[0] = String.class;
        cArg[1] = int.class;
        String s = "prova";
        int i = node.getNum_bits_identifiers();
        Item item = (Item) clazz.getDeclaredConstructor(cArg).newInstance(s,i);
        item.setKey((Integer) list.get(0));
        for (Item it: items) {
            if (it.getKey() == item.getKey()) return;
        }
            items.add(item);
            node.storeItem(item);
            System.out.println(ANSI_GREEN + "-----Added " + item + ANSI_RESET);
    }

    private static void exit(ArrayList<Node> nodes) throws RemoteException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        if (nodes.size()>0) {
            ArrayList<Node> copyOfNodes = new ArrayList<>(nodes);
            Collections.shuffle(copyOfNodes);
            Node node = copyOfNodes.get(0);
            System.out.println(ANSI_GREEN + "----------Exit of " + node + ANSI_RESET);
            node.exitFromRing();
            nodes.remove(node);
        }
    }
}