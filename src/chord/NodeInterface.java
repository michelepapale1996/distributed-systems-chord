package chord;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NodeInterface extends Remote{
    NodeInterface findSuccessor(int key) throws RemoteException;
    NodeInterface getSuccessor() throws RemoteException;
    NodeInterface getPredecessor() throws RemoteException;
    void setPredecessor(NodeInterface predecessor) throws RemoteException;
    boolean hasItem(int key) throws RemoteException;
    String print() throws RemoteException;
    int getId() throws RemoteException;
    void addItem(Item item) throws RemoteException;
    ArrayList<Item> getItems() throws RemoteException;
    SuccessorList getSuccessorList() throws RemoteException;

    Item getInstance() throws RemoteException;
    void setSuccessor(NodeInterface successor) throws RemoteException;
    Handler getHandler() throws RemoteException;
    FingerTable getFingerTable() throws RemoteException;
    Ring getRing() throws RemoteException;
    void setRing(Boolean simpleKeyLocation, int num_bits_identifiers) throws RemoteException;
    void initializeId(NodeInterface knownNode) throws RemoteException;
    void initializeId() throws RemoteException;
}

