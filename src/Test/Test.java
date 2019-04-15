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
            Node node0 = new Node(3, false);
            Node node2 = new Node(3, false);
            Node node6 = new Node(3, false);
            Node node1 = new Node(3, false);
            Node node3 = new Node(3, false);

            node0.setId(0);
            node2.setId(2);
            node3.setId(3);
            node1.setId(1);
            node6.setId(6);

            node0.create();
            Thread.sleep(2900);
            node1.join(node0);
            Thread.sleep(2900);
            node6.join(node0);
            Thread.sleep(2900);
            node3.join(node1);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch(NoSuchElementException e){
            System.out.println("Given item does not exists.");
        } catch(IllegalArgumentException e){
            System.out.println(e);
        }
    }
}
