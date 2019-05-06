package Test;

import Utilities.Debugger;
import chord.InfoNode;
import chord.Node;

import java.rmi.RemoteException;
import java.util.NoSuchElementException;

public class Test {
    public static void main(String args[]) throws RemoteException {
        try {
            Debugger.setDebug(false);
            Node node0 = new Node();
            Node node1 = new Node();
            Node node2 = new Node();
            Node node3 = new Node();
            Node node4 = new Node();
            Node node5 = new Node();
            Node node6 = new Node();
            Node node7 = new Node();

            node0.create(10, false);
            node2.join(node0);
            node6.join(node2);
            node3.join(node6);
            node7.join(node3);
            node1.join(node7);
            node4.join(node1);
            node5.join(node4);
            Thread.sleep(3000);

            new InfoNode(node0);
            new InfoNode(node1);
            new InfoNode(node2);
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
            e.printStackTrace();
        }
    }
}
