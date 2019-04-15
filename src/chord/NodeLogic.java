package chord;

import Test.Debugger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class NodeLogic {
    public static NodeInterface lookUp(int key, NodeInterface initialNode) throws RemoteException, NoSuchElementException {
        Debugger.print("Finding key " + key + " starting from " + initialNode);
        //check if the current node has the item
        if (initialNode.hasItem(key)) return initialNode;

        //otherwise find the successor that has the item
        NodeInterface successorForKey = initialNode.findSuccessor(key, initialNode.isSimpleLookupAlgorithm());
        if (successorForKey.hasItem(key)){
            return successorForKey;
        } else {
            throw new NoSuchElementException();
        }
    }

    public static NodeInterface findSuccessor(int key, Boolean linear, NodeInterface initialNode) throws RemoteException{
        NodeInterface successor;
        if(linear){
            successor = initialNode.getSuccessor();
        }else{
            successor = initialNode.getFingerTable().getSuccessor(key);
        }

        if (NodeLogic.isBetween(initialNode.getId(), key, successor.getId(), initialNode.getNum_bits_identifiers()) || initialNode.getId() == successor.getId()){
            return successor;
        }else{
            return successor.findSuccessor(key, linear);
        }
    }

    public static void join(NodeInterface knownNode, NodeInterface incomingNode) throws RemoteException, IllegalArgumentException{
        NodeInterface successor;
        //if node has as successor himself, he is the only one in the ring -> he becomes my successor
        if(knownNode == knownNode.getSuccessor()){
            if(incomingNode.getId() != knownNode.getId()){
                successor = knownNode;
                Debugger.print(incomingNode.print() + " joined and successor is: " + knownNode.print());
                incomingNode.setSuccessor(knownNode);
            }else{
                throw new IllegalArgumentException("node cannot join the ring because there is already a node with his id.");
            }
        } else {
            successor = knownNode.findSuccessor(incomingNode.getId(), incomingNode.isSimpleLookupAlgorithm());
            if(incomingNode.getId() != successor.getId()){
                incomingNode.setSuccessor(successor);
                Debugger.print(incomingNode.print() + " joined and successor is: " + incomingNode.getSuccessor().print());
            }else{
                throw new IllegalArgumentException("Node cannot join the ring because there is already a node with his id.");
            }
        }

        Debugger.print(incomingNode + " join the ring");
        Debugger.print(incomingNode + "'s predecessor is null");
        incomingNode.setPredecessor(null);
        if (!incomingNode.isSimpleLookupAlgorithm()) {
            incomingNode.getFingerTable().initialize(successor);
        }
        //start tasks to stabilize node
        incomingNode.getHandler().start();
    }

    public static void create(NodeInterface initialNode) throws RemoteException{
        initialNode.setSuccessor(initialNode);
        initialNode.setPredecessor(null);
        Debugger.print(initialNode + "'s successor is " + initialNode);
        Debugger.print(initialNode + "'s predecessor is null");
        if (!initialNode.isSimpleLookupAlgorithm()) {
            initialNode.getFingerTable().initialize(initialNode);
        }
        initialNode.getHandler().start();
    }

    public static void storeItem(Item item, NodeInterface initialNode) throws RemoteException, IllegalArgumentException{
        NodeInterface node = NodeLogic.findSuccessor(item.getKey(), true, initialNode);
        if(node.hasItem(item.getKey())) throw new IllegalArgumentException("It already exists an item with the given id");
        node.addItem(item);
    }

    public static boolean isBetween(int startInterval, int item_searched, int endInterval, int num_bits_identifiers){
        int max_nodes = (int) Math.pow(2, num_bits_identifiers);
        if (startInterval > endInterval) {

            if (item_searched <= endInterval) item_searched = item_searched + max_nodes;
            endInterval = endInterval + max_nodes;
        }
        return (item_searched > startInterval && item_searched <= endInterval);
    }

    public static void exitFromRing(Node node) throws  RemoteException{
        //add the leaving node items to its successor
        ArrayList<Item> items = new ArrayList<>(node.getItems());
        NodeInterface successor = node.getSuccessor();

        for (Item i: items) {
            successor.addItem(i);
        }
        System.out.println(successor.getItems());
        //set the successor's predecessor to leaving node predecessor
        //set the predecessor's successor to leaving node successor
        node.getSuccessor().getSuccessorList().remove(node);
        node.getPredecessor().getSuccessorList().remove(node);
        node.getPredecessor().setSuccessor(node.getSuccessor());
        node.getSuccessor().setPredecessor(node.getPredecessor());
        node.getHandler().stopTimer();
        node.setInstance(null);
    }
}
