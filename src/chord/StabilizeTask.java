package chord;

import Utilities.Debugger;

import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.*;

public class StabilizeTask extends TimerTask {
    private Node owner;
    private int bit;

    public StabilizeTask(Node node){
        this.owner = node;
        this.bit = 0;
    }

    //called periodically
    //Verifies n's immediate successor, and tells the successor about n
    private void stabilize() {
        try {
            NodeInterface new_successor = this.owner.getSuccessor().getPredecessor();
            NodeInterface old_successor = this.owner.getSuccessor();
            //if the new_successor is between me and my successor -> he becomes my successor
            if (new_successor != null && new_successor.getInstance() != null) {
                if (NodeLogic.isBetween(this.owner.getId(), new_successor.getId(), old_successor.getId(), this.owner.getRing().getNum_bits_identifiers())) {
                    Debugger.print("-Stabilization for [" + this.owner.print() + "]: " + this.owner.print() + "'s successor is " + new_successor.print());
                    this.owner.setSuccessor(new_successor);
                    //change the first line of the finger table when the successor change
                    this.owner.getFingerTable().updateSuccessor(new_successor);
                }

                if (this.owner.getId() == old_successor.getId()){
                    Debugger.print("-Stabilization for [" + this.owner.print() + "]: " + this.owner.print() + "'s successor is " + new_successor.print());
                    this.owner.setSuccessor(new_successor);
                    //change the first line of the finger table when the successor change
                    this.owner.getFingerTable().updateSuccessor(new_successor);
                }
            }
        }catch(RemoteException | NullPointerException e){
            //System.out.println(e);
        }
        this.notify(this.owner.getSuccessor(), this.owner);
    }
    //predecessor thinks it might be predecessor of node
    private void notify(NodeInterface node, NodeInterface predecessor){
        try{
            node.getPredecessor().getInstance().getClass();
            if(node.getPredecessor() == null || NodeLogic.isBetween(node.getPredecessor().getId(), predecessor.getId(), node.getId(), node.getRing().getNum_bits_identifiers())){
                Debugger.print("-Notify for [" + this.owner.print() + "]: " + node.print() + "'s predecessor is " + predecessor.print());
                node.setPredecessor(predecessor);
            }

            if(node.getId() == node.getPredecessor().getId()){
                Debugger.print("-Notify for [" + this.owner.print() + "]: " + node.print() + "'s predecessor is " + predecessor.print());
                node.setPredecessor(predecessor);
            }
        }catch(RemoteException | NullPointerException e){
            //if there is a failure -> node.getPredecessor().getId() will throw an exception -> set the predecessor
            try {
                Debugger.print("-Notify for [" + this.owner.print() + "]: " + node.print() + "'s predecessor is " + predecessor.print());
                node.setPredecessor(predecessor);
            } catch (RemoteException e1) {
                //System.out.println(e1);
            }
        }
    }

    //called periodically
    //Checks the validity of entry of the finger table.
    public void fixFingers(){
        this.bit = this.bit + 1;
        this.bit = this.bit % this.owner.getRing().getNum_bits_identifiers();
        try{
            int position = getPosition(this.bit);
            NodeInterface successor = this.owner.findSuccessor(position);
            this.owner.setEntryFingerTable(position,successor);
            Debugger.print("-FixFingers: update finger table for " + this.owner.print() + " with couple < " + position + ", " + successor.print() + " >");
            Debugger.print(this.owner.getFingerTable().print());
        }catch(RemoteException e){
            //e.printStackTrace();
            int position = getPosition(this.bit);
            this.owner.setEntryFingerTable(position, this.owner.getSuccessor());
        }
    }

    private void fixSuccessorList() {
        try {
            if (this.owner.getSuccessor().getId() == this.owner.getId()) {
                Debugger.print("-SuccessorList for [" + this.owner.print() + "]: Network contains only " + this.owner.print());
                this.owner.getSuccessorList().setSuccessors(new ArrayList<>());
                return;
            }
        }catch (RemoteException e){}

        int size = this.owner.getRing().getNum_bits_identifiers();
        boolean foundLivingSuccessor = false;
        int i = 0;
        NodeInterface successor = this.owner.getSuccessor();
        ArrayList<Item> itemsToFix = new ArrayList<>();
        //iterate over fixSuccessorList until do not find the first successor alive
        while(!foundLivingSuccessor){
            try{
                //used only to spawn the NullPointerException
                successor.getInstance().getClass();

                //get new successorList and successorItems
                ArrayList<NodeInterface> newSuccessorList = SuccessorListHandler.getNewSuccessorsListFromSuccessor(successor, this.owner, size);
                LinkedHashMap<Integer, ArrayList<Item>> newSuccessorItems = SuccessorItemsHandler.getNewSuccessorItemsFromSuccessor(newSuccessorList);

                this.owner.getSuccessorList().setSuccessors(newSuccessorList);
                this.owner.getSuccessorItems().setItems(newSuccessorItems);

                Debugger.print("-SuccessorList for [" + this.owner.print() + "]: " + this.owner.getSuccessorList().print());
                this.owner.setSuccessor(newSuccessorList.get(0));
                foundLivingSuccessor = true;
            } catch(NullPointerException | RemoteException e){
                //arrived here, the current successor is faulty
                //add his items to itemsToFix
                try{
                if(i < this.owner.getSuccessorItems().getItems().values().toArray().length){
                    for (Item item: (ArrayList<Item>) this.owner.getSuccessorItems().getItems().values().toArray()[i]) {
                        itemsToFix.add(item);
                    }
                }
                i++;
                //check if there is another successor
                if(i < this.owner.getSuccessorList().size()){
                    successor = this.owner.getSuccessorList().getNode(i);
                }else{
                    this.owner.setSuccessor(this.owner);
                    foundLivingSuccessor = true;
                }}catch (NullPointerException exc){
                    //System.out.println("---------------------- " + this.owner);
                }
            }
        }

        if (itemsToFix.size() != 0) this.fixSuccessorItems(itemsToFix);
    }

    private void fixSuccessorItems(ArrayList<Item> items) {
        try {
            Debugger.print("-SuccessorItems for [" + this.owner.print() + "]: A node has failed\nItems of the failed node were : " + items);
            //store Items in first alive node
            for (Item item : items) {
                this.owner.storeItem(item);
            }
        }catch(RemoteException | IllegalArgumentException e){
            //System.out.println(e);
        }
    }


    public void fixItems(){
        try{
            ArrayList<Item> copy = new ArrayList<>(this.owner.getItems());
            for (Item item : copy) {
                if(item.getKey() != this.owner.getId()){
                    if (this.owner.findSuccessor(item.getKey()).getId() != this.owner.getId()){
                        this.owner.getItems().remove(item);
                        try{
                            this.owner.storeItem(item);
                            System.out.println("-FixItems for [" + this.owner.print() + "]: " + item + " moved FROM " + this.owner.print() + " TO " + this.owner.findSuccessor(item.getKey()).print());
                        }catch (IllegalArgumentException e){
                            System.out.println(e);
                        }
                        Debugger.print("-FixItems for [" + this.owner.print() + "]: " + item + " moved FROM " + this.owner.print() + " TO " + this.owner.findSuccessor(item.getKey()).print());
                    }
                }
            }
        }catch(RemoteException e){
            //System.out.println(e);
        }
    }

    //calculate id + 2^i
    public int getPosition(int i){
        int m = (int) Math.pow(2,this.owner.getRing().getNum_bits_identifiers());
        i = (int) Math.pow(2,i);
        i = i + this.owner.getId();
        i = i % m;
        return i;
    }

    public void run() {
        this.stabilize();
        this.fixSuccessorList();
        this.fixItems();
        if (!this.owner.getRing().isSimpleLookupAlgorithm()) this.fixFingers();
    }
}
