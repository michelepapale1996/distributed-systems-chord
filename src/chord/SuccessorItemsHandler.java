package chord;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SuccessorItemsHandler {
    public static LinkedHashMap<Integer, ArrayList<Item>> getNewSuccessorItemsFromSuccessor(ArrayList<NodeInterface> newSuccessorList) throws RemoteException{
        LinkedHashMap<Integer, ArrayList<Item>> newSuccessorItems = new LinkedHashMap<>();

        for (NodeInterface node: newSuccessorList) {
            newSuccessorItems.put(node.getId(), node.getItems());
        }

        return newSuccessorItems;
    }
}
