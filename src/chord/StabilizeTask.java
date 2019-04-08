package chord;

import Test.Debugger;

import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.*;

public class StabilizeTask extends TimerTask {
    private Node owner;

    public StabilizeTask(Node node){
        this.owner = node;
    }

    //called periodically
    //Verifies n's immediate successor, and tells the successor about n
    private void stabilize() {
        try {
            NodeInterface new_successor = this.owner.getSuccessor().getPredecessor();
            NodeInterface old_successor = this.owner.getSuccessor();
            //if the new_successor is between me and my successor -> he becomes my successor
            if (new_successor != null) {
                if (NodeLogic.isBetween(this.owner.getId(), new_successor.getId(), old_successor.getId(), this.owner.getNum_bits_identifiers())) {
                    Debugger.print("-Stabilization: " + this.owner.print() + "'s successor is " + new_successor.print());
                    this.owner.setSuccessor(new_successor);
                }

                if (this.owner.getId() == old_successor.getId()) this.owner.setSuccessor(new_successor);
            }
        }catch(RemoteException | NullPointerException e){

        }
        this.notify(this.owner.getSuccessor(), this.owner);
    }
    //predecessor thinks it might be predecessor of node
    private void notify(NodeInterface node, NodeInterface predecessor){
        try{
            if(node.getPredecessor() == null || NodeLogic.isBetween(node.getPredecessor().getId(), predecessor.getId(), node.getId(), node.getNum_bits_identifiers())){
                Debugger.print("-Notify: " + node.print() + "'s predecessor is " + predecessor.print());
                node.setPredecessor(predecessor);
            }

            if(node.getId() == node.getPredecessor().getId()) node.setPredecessor(predecessor);
        }catch(RemoteException | NullPointerException e){
            //if there is a failure -> node.getPredecessor().getId() will throw an exception -> set the predecessor
            try {
                node.setPredecessor(predecessor);
            } catch (RemoteException e1) {
            }
        }
    }

    //called periodically
    //Checks the validity of entry of the finger table.
    public void fixFingers(){
        try{
            int bit;
            int new_value;
            int old_value;
            int size = (int) Math.pow(2,this.owner.getNum_bits_identifiers());
            for (bit = 0; bit < this.owner.getNum_bits_identifiers(); bit++){
                int position = this.owner.getId();
                position = position + (int) Math.pow(2,bit);
                position = position % size;
                NodeInterface successor = this.owner.findSuccessor(position,true);
                NodeInterface old_successor = this.owner.getNode(position);
                old_value = old_successor.getId();
                if (old_successor.getId() < this.owner.getId()){
                    old_value = old_value + size;
                }
                new_value = successor.getId();
                if (successor.getId() < this.owner.getId()){
                    new_value = new_value + size;
                }
                if (new_value < old_value || old_value == this.owner.getId()){
                    this.owner.setEntryFingerTable(position,successor);
                }
                Debugger.print("-FixFingers: update finger table for " + this.owner.toString() + " with couple < " + position + ", " + successor.toString() + " >");
            }

            Debugger.print(this.owner.getFingerTable().toString());
        }catch(RemoteException e){

        }
    }

    private void fixSuccessorList(){
        if (this.owner.getSuccessor() == this.owner) {
            Debugger.print("-SuccessorList : Network contains only " + this.owner);
            return;
        }
        int size = this.owner.getNum_bits_identifiers();
        NodeInterface newSuccessor = null;
        boolean foundLivingSuccessor = false;
        int i = 0;
        NodeInterface successor = this.owner.getSuccessor();
        ArrayList<Item> itemsToFix = new ArrayList<>();
        while(!foundLivingSuccessor){
            try{
                //used only to spawn the NullPointerException
                successor.getIp().toString();

                ArrayList<NodeInterface> newSuccessorList = new ArrayList<>();
                LinkedHashMap<Integer, ArrayList<Item>> newSuccesorItems = new LinkedHashMap<>();
                newSuccessorList.add(0, successor);
                for (NodeInterface n: successor.getSuccessorList()) {
                    if (n.getId() != this.owner.getId()) {
                        newSuccessorList.add(n);
                    }
                }
                if (newSuccessorList.size() > size){
                    newSuccessorList.remove(size);
                }

                for (NodeInterface node :newSuccessorList ) {
                    newSuccesorItems.put(node.getId(), node.getItems());
                }

                this.owner.setSuccessorList(newSuccessorList);
                this.owner.setSuccessorItems(newSuccesorItems);
                Debugger.print("-SuccessorList: successorList di " + this.owner + " è: " + newSuccessorList);
                Debugger.print("-SuccessorList: successorItems di " + this.owner + " è: " + newSuccesorItems);
                newSuccessor = newSuccessorList.get(0);
                this.owner.setSuccessor(newSuccessor);
                foundLivingSuccessor = true;
            }catch (NullPointerException | RemoteException e){
                for (Item item: (ArrayList<Item>) this.owner.getSuccessorItems().values().toArray()[i]) {
                    itemsToFix.add(item);
                }
                i++;
                //check if there is another successor
                if(i < this.owner.getSuccessorList().size()){
                    successor = this.owner.getSuccessorList().get(i);
                }else{
                    this.owner.setSuccessor(this.owner);
                    foundLivingSuccessor = true;
                }
            }
        }
        if (itemsToFix.size() != 0) {
            this.fixSuccessorItems(itemsToFix);
        }
    }

    private void fixSuccessorItems(ArrayList<Item> items) {
        try {
            Debugger.print("-SuccessorItems: A node has failed\nItems of the failed node were : " + items);
            //store Items in first alive node
            for (Item item : items) {
                this.owner.storeItem(item);
            }
        }catch(RemoteException e){

        }
    }


    public void fixItems(){
        try{
            for (Item item : this.owner.getItems()) {
                if (this.owner.findSuccessor(item.getKey(),true).getId() != this.owner.getId()){
                    this.owner.getItems().remove(item);
                    this.owner.storeItem(item);
                    Debugger.print("-FixItems: " + item + " moved FROM " + this.owner + " TO " + this.owner.findSuccessor(item.getKey(), true));
                }
            }
        }catch(RemoteException e){

        }
    }

    public void run() {
        this.stabilize();
        this.fixSuccessorList();
        this.fixItems();
        if (!this.owner.isSimpleLookupAlgorithm()) this.fixFingers();
    }
}
