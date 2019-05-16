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
            Node node10 = new Node();
            Node node15 = new Node();

            node10.setId(10);
            node15.setId(15);

            node0.create(4, false);
            Thread.sleep(3000);
            System.out.println("Node10 join");
            node10.join(node0);
            Thread.sleep(3000);
            System.out.println("Node15 join");
            node15.join(node10);
            Thread.sleep(500);
            InfoNode.show(node0);
            InfoNode.show(node10);
            InfoNode.show(node15);


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
