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
        //if the node is between me and my successor -> he becomes my successor
        if(node != null && node.isBetween(this.owner.getId(), this.owner.getSuccessor().getId())) this.owner.setSuccessor(node);

        this.notify(this.owner.getSuccessor(), this.owner);
    }

    //predecessor thinks it might be predecessor of successor
    private void notify(Node successor, Node predecessor) {
        if(successor.getPredecessor() == null || predecessor.isBetween(successor.getPredecessor().getId(), successor.getId())){
            successor.setPredecessor(predecessor);
        }
    }

    public void run() {
        try {
            System.out.println("Begin stabilization protocol for " + this.owner);
            this.stabilize();
            System.out.println("End stabilization protocol for " + this.owner);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
