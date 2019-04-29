package chord;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class SuccessorListHandler {
    public static ArrayList<NodeInterface> getNewSuccessorsListFromSuccessor(NodeInterface successor, NodeInterface node, int ringSize) throws RemoteException{
        ArrayList<NodeInterface> list = new ArrayList<>();
        list.add(0, successor);
        for (NodeInterface n: successor.getSuccessorList().getSuccessors()) {
            try {
                n.getInstance().getClass();
                if (n.getId() != node.getId()) {
                    list.add(n);
                }
            }catch (RemoteException | NullPointerException e){
            }
        }
        if (list.size() > ringSize){
            list.remove(ringSize);
        }

        return list;
    }
}
