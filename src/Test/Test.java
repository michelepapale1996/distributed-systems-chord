package Test;

import Graphic.PrintChordRing;
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
            Node node0 = new Node(3, true);
            Node node2 = new Node(3, true);
            Node node6 = new Node(3, true);
            Node node1 = new Node(3, true);
            Node node3 = new Node(3, true);

            node0.setId(0);
            node2.setId(2);
            node3.setId(3);
            node1.setId(1);
            node6.setId(6);

            node0.create();
            Thread.sleep(2900);

            Item item1 = new Item("nodo1", 8);
            item1.setKey(1);
            node0.storeItem(item1);
            Thread.sleep(3000);

            Item item3 = new Item("nodo1", 8);
            item3.setKey(3);
            node0.storeItem(item3);
            Thread.sleep(3000);

            Item item4 = new Item("nodo1", 8);
            item1.setKey(4);
            node0.storeItem(item4);
            Thread.sleep(3000);


            node2.join(node0);
            Thread.sleep(2800);

            node3.join(node2);
            Thread.sleep(2800);



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
}
