import java.util.TimerTask;

public class StabilizeTask extends TimerTask {
    private Node owner;

    public StabilizeTask(Node node){
        this.owner = node;
    }

    //called periodically
    // verifies n's immediate successor, and tells the successor about n
    private void stabilize(){
        Node node = this.owner.getSuccessor().getPredecessor();
        if(node.isBetweenMyIdAndMySuccessorId(this.owner.getId(), this.owner.getSuccessor())) this.owner.setSuccessor(node);

        this.notify(this.owner.getSuccessor(), this.owner);
    }

    //predecessor thinks it might be predecessor of successor
    private void notify(Node successor, Node predecessor) {
        if(successor.getPredecessor() == null || predecessor.isBetweenMyIdAndMySuccessorId(successor.getPredecessor().getId(), successor)){
            successor.setPredecessor(predecessor);
        }
    }

    //it checks periodically the validity of entry of the finger table.
    public void fixFingers(){
        int m;
        int n = this.owner.getId();
        int size = (int) Math.pow(2,this.owner.getNum_bits_identifiers());
        for (m = 0; m < this.owner.getNum_bits_identifiers(); m++){
            n = n + (int) Math.pow(2,m);
            n = n % size;
            this.owner.setEntryFingerTable(n,this.owner.findSuccessor(n));
        }
    }

    public void run() {
        try {
            this.stabilize();
            if (!this.owner.isSimpleLookupAlgorithm()){
                this.fixFingers();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


}
