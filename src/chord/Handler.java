package chord;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Handler implements Serializable {
    private Node owner;
    private Timer timer;

    public Handler(Node owner) {
        this.owner = owner;
    }

    public void start(){
        this.timer = new Timer();
        TimerTask stabilizeTask = new StabilizeTask(this.owner);

        timer.scheduleAtFixedRate(stabilizeTask, 0, 1000);
    }

    public void stopTimer(){
        this.timer.cancel();
    }
}
