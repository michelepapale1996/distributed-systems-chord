package chord;

import Utilities.Debugger;
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
        //InfoNode.show(successorForKey);
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
            //System.out.println("Il nodo è "+ initialNode.print() + " e il successore trovato per k " + key + " è " +  successor.print());
        }

        if (NodeLogic.isBetween(initialNode.getId(), key, successor.getId(), initialNode.getRing().getNum_bits_identifiers())
                || initialNode.getId() == successor.getId()){
            return initialNode.getSuccessor();
        }else{
            return successor.findSuccessor(key);
        }
    }

    public static void join(NodeInterface knownNode, NodeInterface incomingNode) throws RemoteException, IllegalArgumentException{
        //if the two nodes have the same id -> throw exception
        if(incomingNode.getId() == knownNode.getId()) throw new IllegalArgumentException("Node " + incomingNode.print() + " cannot join the ring because there is already a node with his id.");

        NodeInterface successor;
        incomingNode.setRing(knownNode.getRing().isSimpleLookupAlgorithm(), knownNode.getRing().getNum_bits_identifiers());
        //incomingNode.initializeId(knownNode);
        //if knownNode has as successor himself, he is the only one in the ring -> the node becomes incomingNode's successor
        if(knownNode == knownNode.getSuccessor()){
            if(incomingNode.getId() != knownNode.getId()){
                successor = knownNode;
                Debugger.print(incomingNode.print() + " joined and successor is: " + knownNode.print());
                incomingNode.setSuccessor(knownNode);
            }else{
                //arrived here, there are only 2 nodes and incomingNode has the same id of knownNode
                throw new IllegalArgumentException("Node " + incomingNode.print() + " cannot join the ring because there is already a node with his id.");
            }
        } else {
            successor = knownNode.findSuccessor(incomingNode.getId());
            if(incomingNode.getId() != successor.getId()){
                incomingNode.setSuccessor(successor);
                Debugger.print(incomingNode.print() + " joined and successor is: " + incomingNode.getSuccessor().print());
            }else{
                throw new IllegalArgumentException("Node " + incomingNode.print() +  " cannot join the ring because there is already a node with his id.");
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
            if(node.hasItem(item.getKey())) throw new IllegalArgumentException("It already exists an item with the given id " + item.getKey());
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

    public static void exitFromRing(Node node) throws RemoteException{
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
