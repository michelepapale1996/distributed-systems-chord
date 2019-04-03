import java.sql.SQLOutput;
import java.util.TimerTask;

public class StabilizeTask extends TimerTask {
    private Node owner;

    public StabilizeTask(Node node){
        this.owner = node;
    }

    //called periodically
    //Verifies n's immediate successor, and tells the successor about n
    private void stabilize(){
        Node node = this.owner.getSuccessor().getPredecessor();
        //if the node is between me and my successor -> he becomes my successor
        if(node != null && node.isBetween(this.owner.getId(), this.owner.getSuccessor().getId())){

            System.out.println("-Stabilization: " + this.owner + "'s successor is " + node);
            this.owner.setSuccessor(node);
        }
        this.notify(this.owner.getSuccessor(), this.owner);
    }

    //predecessor thinks it might be predecessor of successor
    private void notify(Node successor, Node predecessor) {
        if(successor.getPredecessor() == null || predecessor.isBetween(successor.getPredecessor().getId(), successor.getId())){
            System.out.println("-Stabilization: " + successor + "'s predecessor is " + predecessor);
            successor.setPredecessor(predecessor);
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
            Node successor = this.owner.findSuccessorLinear(position);
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

    public void run() {
        try {
            this.stabilize();
            if (!this.owner.isSimpleLookupAlgorithm()) this.fixFingers();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
