package Test;

import chord.InfoNode;
import chord.Item;
import chord.Node;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    Node node1 = new Node();

    NodeTest() throws RemoteException {
    }

    @Test
    void lookUp() throws RemoteException, InterruptedException {
        node1.create(3, true);
        node1.setId(1);

        Node node7 = new Node();
        node7.setId(7);
        node7.join(node1);

        Node node5 = new Node();
        node5.setId(5);
        node5.join(node1);

        Item item0 = new Item("ABC", 3);
        item0.setKey(0);
        node5.storeItem(item0);

        Thread.sleep(2000);

        Item item3 = new Item("ABC", 3);
        item3.setKey(3);
        node7.storeItem(item3);

        Thread.sleep(2000);

        Item item6 = new Item("ABC", 3);
        item6.setKey(6);
        node5.storeItem(item6);

        Thread.sleep(3000);

        assertEquals(node5, node1.lookUp(3));
        assertEquals(node1, node5.lookUp(0));
        assertEquals(node7, node5.lookUp(6));
        assertNotEquals(node1, node5.lookUp(6));
    }

    @Test
    void findSuccessor() throws RemoteException, InterruptedException {
        node1.setId(1);
        node1.create(3, true);
        Thread.sleep(3000);
        Node node7 = new Node();
        node7.setId(7);
        node7.join(node1);

        Thread.sleep(3000);
        InfoNode.show(node1);
        assertEquals(node7.getId(), node1.findSuccessor(3).getId());
    }

    @Test
    void storeItem() throws RemoteException, InterruptedException {
        node1.create(3, true);
        node1.setId(1);

        Node node7 = new Node();
        node7.setId(7);
        node7.join(node1);

        Node node5 = new Node();
        node5.setId(5);
        node5.join(node1);

        Item item0 = new Item("ABC", 3);
        item0.setKey(0);
        node5.storeItem(item0);

        assertFalse(node5.hasItem(0));
        assertTrue(node1.hasItem(0));

        Item item3 = new Item("ABC", 3);
        item3.setKey(3);
        node7.storeItem(item3);

        Item item6 = new Item("ABC", 3);
        item6.setKey(6);
        node5.storeItem(item6);

        Thread.sleep(3000);

        assertTrue(node5.hasItem(3));
        assertTrue(node7.hasItem(6));

    }

    @Test
    void create() throws RemoteException {
        node1.create(3,true);
        node1.setId(1);
        assertEquals(1,node1.getId());
        assertEquals(1, node1.getSuccessor().getId());
    }

    @Test
    void join() throws RemoteException, InterruptedException {
        node1.create(3, true);
        node1.setId(1);

        Node node2 = new Node();
        node2.setId(2);
        node2.join(node1);

        Thread.sleep(3000);

        assertEquals(node2.getId(), node1.getSuccessor().getId());
        assertEquals(node1.getId(), node2.getPredecessor().getId());
        assertTrue(node1.getSuccessorList().getSuccessors().contains(node2));
    }

    @Test
    void exitFromRing() throws RemoteException, InterruptedException {
        //creation of the ring with node 1
        node1.create(3, true);
        node1.setId(1);
        //node2 join the ring
        Node node2 = new Node();
        node2.setId(2);
        node2.join(node1);
        //node3 join the ring
        Node node3 = new Node();
        node3.setId(3);
        node3.join(node2);

        Thread.sleep(5000);// give time to successfully update
        assertTrue(node1.getSuccessorList().getSuccessors().contains(node2));

        assertEquals(node3.getId(), node2.getSuccessor().getId());

        node2.exitFromRing();

        Thread.sleep(5000);

        assertEquals(node3.getId(), node1.getSuccessor().getId());
        assertEquals(node1.getId(), node3.getPredecessor().getId());

        assertFalse(node1.getSuccessorList().getSuccessors().contains(node2));
    }
}