import java.util.Timer;
import java.util.TimerTask;

public class Handler {
    private Node owner;

    public Handler(Node owner) {
        this.owner = owner;
    }

    public void start(){
        Timer time = new Timer();
        TimerTask stabilizeTask = new StabilizeTask(this.owner);
        time.scheduleAtFixedRate(stabilizeTask, 0, 200);
    }
}
