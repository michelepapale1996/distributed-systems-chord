package Test;

import chord.FingerTable;
import chord.Item;
import chord.Node;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Test {
    public static void main(String args[]) throws RemoteException {
        try {
            Debugger.setDebug(true);
            Node node0 = new Node();
            Node node1 = new Node();
            Node node2 = new Node();
            Node node3 = new Node();
            Node node4 = new Node();
            Node node5 = new Node();
            Node node6 = new Node();
            Node node7 = new Node();

            node0.setId(0);
            node1.setId(1);
            node2.setId(2);
            node3.setId(3);
            node4.setId(4);
            node5.setId(5);
            node6.setId(6);
            node7.setId(7);

            node0.create(3, false);
            Thread.sleep(3000);
            node2.join(node0);
            Thread.sleep(3000);
            node6.join(node2);
            Thread.sleep(3000);
            node3.join(node6);
            Thread.sleep(3000);
            node7.join(node3);
            Thread.sleep(3000);
            node1.join(node7);
            Thread.sleep(3000);
            node4.join(node1);
            Thread.sleep(3000);
            node5.join(node4);
            Thread.sleep(3000);





//            ArrayList<FingerTable> fingerTables = new ArrayList<>();
//            fingerTables.add(node0.getFingerTable());
//            fingerTables.add(node2.getFingerTable());
//            fingerTables.add(node3.getFingerTable());
//            try {
//                PrintChordRing.run(fingerTables);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //NodeInterface whoKeepsItem = node0.lookUp(item1.getKey());
            //NodeInterface whoKeepsItem = node0.lookUp(1);
            //System.out.println("Node found: " + whoKeepsItem + " keeps item5");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch(NoSuchElementException e){
            System.out.println("Given item does not exists.");
        } catch(IllegalArgumentException e){
            System.out.println(e);
        }
    }

    private static void info(Node myNode) throws RemoteException {
        System.out.println("[Info node]");
        System.out.println("- Node id: " + myNode.print());
        System.out.println("- Successor: " + myNode.getSuccessor().print());
        try{
            System.out.println("- Predecessor: " + myNode.getPredecessor().print());
        }catch (NullPointerException e){
            System.out.println("- Predecessor: null");
        }
        System.out.println("- Items of the node: " + myNode.getItems());
    }
}
