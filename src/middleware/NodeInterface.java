package middleware;

import chord.Node;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeInterface extends Remote{
    NodeInterface findSuccessor(int key, Boolean isLinear) throws RemoteException;
    NodeInterface getSuccessor() throws RemoteException;
    NodeInterface getPredecessor() throws RemoteException;
    void setPredecessor(NodeInterface predecessor) throws RemoteException;

    boolean isBetween(int item_searched, int end_of_interval) throws RemoteException;
    boolean hasItem(int key) throws RemoteException;

    String print() throws RemoteException;
    int getId() throws RemoteException;
}

