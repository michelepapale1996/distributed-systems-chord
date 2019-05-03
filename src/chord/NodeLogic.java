package chord;

import Test.Debugger;
import Test.Parameters;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class NodeLogic {
    public static NodeInterface lookUp(int key, NodeInterface initialNode) throws RemoteException, NoSuchElementException {
        Debugger.print("Finding key " + key + " starting from " + initialNode);
        //check if the current node has the item
        if (initialNode.hasItem(key)) return initialNode;

        //otherwise find the successor that has the item
        NodeInterface successorForKey = initialNode.findSuccessor(key);
        //System.out.println("successor for key: " + key + " successor node: " + successorForKey.print());
        //new InfoNode(successorForKey);
        if (successorForKey.hasItem(key)){
            return successorForKey;
        } else {
            throw new NoSuchElementException();
        }
    }

    public static NodeInterface findSuccessor(int key, NodeInterface initialNode) throws RemoteException{
        NodeInterface successor;
        Parameters.add();
        if(initialNode.getRing().isSimpleLookupAlgorithm()){
            successor = initialNode.getSuccessor();
        }else{
            successor = initialNode.getFingerTable().getClosestPrecedingNode(key);
        }

        if (NodeLogic.isBetween(initialNode.getId(), key, initialNode.getSuccessor().getId(), initialNode.getRing().getNum_bits_identifiers()) || initialNode.getId() == successor.getId()){
            return initialNode.getSuccessor();
        }else{
            return successor.findSuccessor(key);
        }
    }

    public static void join(NodeInterface knownNode, NodeInterface incomingNode) throws RemoteException, IllegalArgumentException{
        NodeInterface successor;
        incomingNode.setRing(knownNode.getRing().isSimpleLookupAlgorithm(),knownNode.getRing().getNum_bits_identifiers());
        // TODO: 17/04/2019 this line is used to set the id
        //incomingNode.initializeId();
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
            successor = knownNode.findSuccessor(incomingNode.getId());
            if(incomingNode.getId() != successor.getId()){
                incomingNode.setSuccessor(successor);
                Debugger.print(incomingNode.print() + " joined and successor is: " + incomingNode.getSuccessor().print());
            }else{
                throw new IllegalArgumentException("Node cannot join the ring because there is already a node with his id.");
            }
        }

        Debugger.print(incomingNode.print() + " join the ring");
        Debugger.print(incomingNode.print() + "'s predecessor is null");
        incomingNode.setPredecessor(null);
        if (!incomingNode.getRing().isSimpleLookupAlgorithm()) {
            incomingNode.getFingerTable().initialize(successor);
        }
        //start tasks to stabilize node
        incomingNode.getHandler().start();
    }

    public static void create(NodeInterface initialNode, int numBitsIdentifier, Boolean simpleLookUpAlgorithm) throws RemoteException{
        initialNode.setSuccessor(initialNode);
        initialNode.setPredecessor(null);
        initialNode.setRing(simpleLookUpAlgorithm,numBitsIdentifier);
        // TODO: 17/04/2019 this line is used to set the id
        //initialNode.initializeId();
        Debugger.print(initialNode.print() + "'s successor is " + initialNode.print());
        Debugger.print(initialNode.print() + "'s predecessor is null");
        if (!initialNode.getRing().isSimpleLookupAlgorithm()) {
            initialNode.getFingerTable().initialize(initialNode);
        }
        initialNode.getHandler().start();
    }

    public static void storeItem(Item item, NodeInterface initialNode) throws RemoteException, IllegalArgumentException{
        if (item.getKey() == initialNode.getId()){
            if(initialNode.hasItem(item.getKey())) throw new IllegalArgumentException("It already exists an item with the given id");
            initialNode.addItem(item);
        }else{
            NodeInterface node = NodeLogic.findSuccessor(item.getKey(), initialNode);
            if(node.hasItem(item.getKey())) throw new IllegalArgumentException("It already exists an item with the given id");
            node.addItem(item);
        }
    }

    public static boolean isBetween(int startInterval, int itemSearched, int endInterval, int num_bits_identifiers){
        int max_nodes = (int) Math.pow(2, num_bits_identifiers);
        if (startInterval > endInterval) {
            endInterval = endInterval + max_nodes;
            if (itemSearched < startInterval){
                itemSearched = itemSearched + max_nodes;
            }
        }
        return (itemSearched > startInterval && itemSearched <= endInterval);
    }

    public static void exitFromRing(Node node) throws  RemoteException{
        //add the leaving node items to its successor
        ArrayList<Item> items = new ArrayList<>(node.getItems());
        NodeInterface successor = node.getSuccessor();

        for (Item i: items) {
            successor.addItem(i);
        }
        //set the successor's predecessor to leaving node predecessor
        //set the predecessor's successor to leaving node successor
        node.getSuccessor().getSuccessorList().remove(node);
        try{
            node.getPredecessor().getSuccessorList().remove(node);
            node.getPredecessor().setSuccessor(node.getSuccessor());
            node.getSuccessor().setPredecessor(node.getPredecessor());
            node.getHandler().stopTimer();
            node.setInstance(null);
        }catch (NullPointerException e){

        }
    }
}
