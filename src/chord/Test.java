package chord;

import middleware.NodeInterface;

import java.rmi.RemoteException;

public class Test {
    private Node whoKeepsItem;

    public static void main(String args[]) throws RemoteException {
        Node node0 = new Node(3, true);
        Node node2 = new Node(3, true);

        node0.setId(0);
        node2.setId(2);

        node0.create();
        try {
            Thread.sleep(2900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        node2.join(node0);
        try {
            Thread.sleep(2800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //node6.join(node2);

        Item item1 = new Item("nodo1", 8);
        item1.setKey(1);
        System.out.println("Id item1: " + item1.getKey());
        node0.storeItem(item1);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //NodeInterface whoKeepsItem = node0.lookUp(item1.getKey());
        //System.out.println("Node found: " + whoKeepsItem + " keeps item5");

    }
}
