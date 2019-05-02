package chord;

import java.rmi.RemoteException;

public class InfoNode {
    public InfoNode(Node chosen) throws RemoteException {
        run(chosen);
    }

    public void run(Node myNode) throws RemoteException {
        System.out.println("- Node id: " + myNode.print());
        System.out.println("- Successor: " + myNode.getSuccessor().print());
        try{
            System.out.println("- Predecessor: " + myNode.getPredecessor().print());
        }catch (NullPointerException e){
            System.out.println("- Predecessor: null");
        }
        System.out.println("- SuccessorList: " + myNode.getSuccessorList().print());
        System.out.println("- Items of the node: " + myNode.getItems());
    }
}
