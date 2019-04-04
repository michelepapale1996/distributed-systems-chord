package chord;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.TimerTask;

public class StabilizeTask extends TimerTask {
    private Node owner;

    public StabilizeTask(Node node){
        this.owner = node;
    }

    //called periodically
    //Verifies n's immediate successor, and tells the successor about n
    private void stabilize(){
        Node new_successor = this.owner.getSuccessor().getPredecessor();
        Node old_successor = this.owner.getSuccessor();
        //if the new_successor is between me and my successor -> he becomes my successor
        if(new_successor != null){
            if ((this.owner.isBetween(new_successor.getId(), old_successor.getId())) || this.owner.getId() == old_successor.getId()) {
                System.out.println("-Stabilization: " + this.owner + "'s successor is " + new_successor);
                this.owner.setSuccessor(new_successor);
            }
        }
        this.notify(this.owner.getSuccessor(), this.owner);
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

    public void fixSuccessorList(){
        int size = this.owner.getNum_bits_identifiers();
        boolean findLivingSuccessor = false;
        int counter = 0;
        while(!findLivingSuccessor && counter < size){
            try{
                Node successor = this.owner.getSuccessorList().get(0);

                ArrayList<Node> newSuccessorList = successor.getSuccessorList();
                newSuccessorList.remove(size-1);
                newSuccessorList.add(0, successor);
                this.owner.setSuccessorList(newSuccessorList);
                this.owner.setSuccessor(newSuccessorList.get(0));

                findLivingSuccessor = true;
            }catch (Exception e){
                System.out.println("Error: " + e);

                //remove the failed nodes from successorList
                this.owner.getSuccessorList().remove(0); //remove shifts automatically any subsequent list

                counter++;
            }
        }
        if (counter > size){
            System.out.println("all nodes in successor list are failed");
        }
    }

    public void run() {
        try {
            this.stabilize();
            if (!this.owner.isSimpleLookupAlgorithm()) this.fixFingers();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
