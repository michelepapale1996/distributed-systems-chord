package Test;

import chord.InfoNode;
import chord.Item;
import chord.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.*;


public class RandomTest {
    private static boolean b = true;

    public static void setB(boolean b) {
        RandomTest.b = b;
    }

    public static void main(String args[]) throws RemoteException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";

        Debugger.setDebug(false);
        ArrayList<Node> nodesInTheNetwork = new ArrayList<>();
        ArrayList<Item> itemsInTheNetwork = new ArrayList<>();

        start(nodesInTheNetwork, itemsInTheNetwork);

        Node node0 = new Node();
        node0.setId(0);
        node0.create(16, false);
        nodesInTheNetwork.add(node0);


        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= Math.pow(2, 4); i++) {
            list.add(i);
        }

        List<Integer> actions = new ArrayList<Integer>();
        for (int i = 1; i <= 3; i++) {
            actions.add(i);
        }

        while(b) {
            Collections.shuffle(actions);

            switch (actions.get(0)) {
                case 1 : join(list, node0, nodesInTheNetwork); break;
                case 2 : storeItems(list, node0, itemsInTheNetwork); break;
                case 3 : exit(nodesInTheNetwork, node0); break;
            }
            System.out.print(ANSI_YELLOW + "-----");// + nodesInTheNetwork + ANSI_RESET);
            System.out.print("[");
            for (Node nod: nodesInTheNetwork) {
                System.out.print(nod.print());
            }
            System.out.print("]");
            System.out.print("\n");
            System.out.println(ANSI_YELLOW + "-----" + itemsInTheNetwork + ANSI_RESET);
            Thread.sleep(1000);
        }
    }

    private static void join(List list, Node node, ArrayList<Node> nodes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, RemoteException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        Collections.shuffle(list);
        Class<?> clazz = Class.forName("chord.Node");
        Constructor<?> ctor = clazz.getConstructor();
        Node newNode = (Node) ctor.newInstance();
        try {
            Integer id = (Integer) list.get(0) % (node.getRing().getNum_bits_identifiers());
            newNode.setId(id);
            newNode.join(node);
            nodes.add(newNode);
            System.out.println(ANSI_GREEN + "-----" + newNode.print() + " join the network" + ANSI_RESET);
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    private static void storeItems(List list, Node node, ArrayList<Item> items) throws RemoteException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        Collections.shuffle(list);
        Class<?> clazz = Class.forName("chord.Item");
        Class[] cArg = new Class[2];
        cArg[0] = String.class;
        cArg[1] = int.class;
        String s = randomAlphaNumeric(10);
        int i = node.getRing().getNum_bits_identifiers();
        Item item = (Item) clazz.getDeclaredConstructor(cArg).newInstance(s,i);
        try {
            items.add(item);
            node.storeItem(item);
            System.out.println(ANSI_GREEN + "-----Added " + item.toString() + ANSI_RESET);
        }catch (IllegalArgumentException e){
            System.out.println(e);
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

    private static void exit(ArrayList<Node> nodes, Node node0) throws RemoteException {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";
        ArrayList<Node> copyOfNodes = new ArrayList<>(nodes);
        Collections.shuffle(copyOfNodes);
        Node node = copyOfNodes.get(0);
        if (nodes.size()>0 && node != node0 ) {
            System.out.println(ANSI_GREEN + "----------Exit of " + node.print() + ANSI_RESET);
            node.exitFromRing();
            nodes.remove(node);
        }
    }

    private static void start(ArrayList<Node> nodes, ArrayList<Item> items){
        Timer timer = new Timer();
        TimerTask periodic = new TimerTask() {
            Scanner scanner = new Scanner(System.in);

            @Override
            public void run() {
                String input = scanner.nextLine();
                if(input.equals("q")){
                    RandomTest.setB(false);
                    menu();
                }
            }
            private void menu(){
                boolean flag = true;
                while(flag){
                    System.out.print("[");
                    for (Node nod: nodes) {
                        System.out.print(nod.print());
                    }
                    System.out.println("]");
                    System.out.println(items);
                    System.out.println("Insert the id of the node to check:");
                    String id = scanner.nextLine();
                    Node chosen = null;
                    for (Node nod : nodes) {
                        if (nod.getId() == Integer.valueOf(id)) {
                            chosen = nod;
                        }
                    }
                    try{
                        new InfoNode(chosen);
                    }catch (NullPointerException e){
                        System.out.println("The choosen node is not present there isn't in the network. Try again: ");
                    }catch (RemoteException e){
                        System.out.println(e);
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(periodic, 0, 1000);
    }


}