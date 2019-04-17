package chord;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SuccessorList implements Serializable{
    ArrayList<NodeInterface> successors;

    public SuccessorList(){
        successors = new ArrayList<>();
    }

    public void setSuccessors(ArrayList<NodeInterface> successors){
        this.successors = successors;
    }

    public void remove(NodeInterface node){
        successors.remove(node);
    }

    public ArrayList<NodeInterface> getSuccessors() {
        return successors;
    }

    public int size(){
        return successors.size();
    }

    public NodeInterface getNode(int i){
        return successors.get(i);
    }

    public String print() throws RemoteException {
        String toReturn = "[";
        for (NodeInterface n: successors) {
            toReturn = toReturn + " - " + n.print();
        }
        toReturn = toReturn + "]";
        return toReturn;
    }
}
