package chord;

import java.rmi.RemoteException;

public class InfoNode {
    public static void show(Node myNode){
        try {
            System.out.println("- Node id: " + myNode.print());
            System.out.println("- Successor: " + myNode.getSuccessor().print());
            try {
                System.out.println("- Predecessor: " + myNode.getPredecessor().print());
            } catch (NullPointerException e) {
                System.out.println("- Predecessor: null");
            }
            System.out.println("- SuccessorList: " + myNode.getSuccessorList().print());
            System.out.println("- Items of the node: " + myNode.getItems() + '\n');
            if (!myNode.getRing().isSimpleLookupAlgorithm()) System.out.println(myNode.getFingerTable().print());
        }catch(RemoteException e){

        }
    }
}
