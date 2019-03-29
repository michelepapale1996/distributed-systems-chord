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

    public void run() {
        try {
            this.stabilize();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
