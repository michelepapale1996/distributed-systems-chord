package chord;

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
            System.out.println(this.owner);
            Node new_successor = this.owner.getSuccessor().getPredecessor();
            Node old_successor = this.owner.getSuccessor();
            //if the new_successor is between me and my successor -> he becomes my successor
            if (new_successor != null && new_successor.getIp() != null) {
                if ((this.owner.isBetween(new_successor.getId(), old_successor.getId())) || this.owner.getId() == old_successor.getId()) {
                    System.out.println("-Stabilization: " + this.owner + "'s successor is " + new_successor);
                    this.owner.setSuccessor(new_successor);
                }
            }
            this.notify(this.owner.getSuccessor(), this.owner);
        } catch (NullPointerException e) {
        }
    }
    //predecessor thinks it might be predecessor of node
    private void notify(Node node, Node predecessor) {
        if(node.getPredecessor() == null || node.getPredecessor().isBetween(predecessor.getId(), node.getId()) || node.getId() == node.getPredecessor().getId()){
            System.out.println("-Notify: " + node + "'s predecessor is " + predecessor);
            node.setPredecessor(predecessor);
        }
    }

    //called periodically
    //Checks the validity of entry of the finger table.
    public void fixFingers(){
        int bit;
        int new_value;
        int old_value;
        int size = (int) Math.pow(2,this.owner.getNum_bits_identifiers());
        for (bit = 0; bit < this.owner.getNum_bits_identifiers(); bit++){
            int position = this.owner.getId();
            position = position + (int) Math.pow(2,bit);
            position = position % size;
            Node successor = this.owner.findSuccessor(position,true);
            Node old_successor = this.owner.getNode(position);
            old_value = old_successor.getId();
            if (old_successor.getId() < this.owner.getId()){
                old_value = old_value + size;
            }
            new_value = successor.getId();
            if (successor.getId() < this.owner.getId()){
                new_value = new_value + size;
            }
            if (new_value < old_value){
                this.owner.setEntryFingerTable(position,successor);
            }
            System.out.println("update finger table for " + this.owner.toString() + " with couple < " + position + ", " + successor.toString() + " >");
        }
    }

    private void fixSuccessorList(){
        if (this.owner.getSuccessor() == this.owner) {
            System.out.println("-Successor List : Network contains only " + this.owner);
            return;
        }
        int size = this.owner.getNum_bits_identifiers();
        Node newSuccessor = null;
        boolean foundLivingSuccessor = false;
        int i = 0;
        Node successor = this.owner.getSuccessor();
        ArrayList<Item> itemsToFix = new ArrayList<>();
        while(!foundLivingSuccessor){
            if(successor.getIp() != null){
                ArrayList<Node> newSuccessorList = new ArrayList<>();
                LinkedHashMap<Integer, ArrayList<Item>> newSuccesorItems = new LinkedHashMap<>();

                newSuccessorList.add(0, successor);

                for (Node n :successor.getSuccessorList()) {
                    if (n.getId() != this.owner.getId() && n.getIp() != null) {
                        newSuccessorList.add(n);
                    }
                }
                if (newSuccessorList.size() > size){
                    newSuccessorList.remove(size);
                }

                for (Node node :newSuccessorList ) {
                    newSuccesorItems.put(node.getId(), node.getItems());
                }

                this.owner.setSuccessorList(newSuccessorList);
                this.owner.setSuccessoreItems(newSuccesorItems);
                System.out.println("La successor list di " + this.owner + " è: " + newSuccessorList);
                System.out.println("La successor Items di " + this.owner + " è: " + newSuccesorItems);
                newSuccessor = newSuccessorList.get(0);
                this.owner.setSuccessor(newSuccessor);
                foundLivingSuccessor = true;
            }else{
                for (Item item: (ArrayList<Item>) this.owner.getSuccessoreItems().values().toArray()[i]) {
                    itemsToFix.add(item);
                }
                i++;
                successor = this.owner.getSuccessorList().get(i);
            }
        }
        if (itemsToFix.size() != 0) {
            this.fixSuccessorItems(itemsToFix);
        }
    }

    private void fixSuccessorItems(ArrayList<Item> items) {
        System.out.println("-----------fixSuccessorItems");
        System.out.println("items of the failed node were : " + items);

        //store the pending items
        System.out.println("-------------- " + items);
        //store Items in first alive node
        for (Item item : items) {
            //System.out.println("------ i'm storing " + item.getKey() + " in " + this.owner.findSuccessor(item.getKey(), true));
            this.owner.storeItem(item);
        }
        System.out.println("-----------fine fixSuccessorItems");
    }

    public void run() {
        try {
            this.stabilize();
            this.fixSuccessorList();
            if (!this.owner.isSimpleLookupAlgorithm()) this.fixFingers();
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
    }
}
