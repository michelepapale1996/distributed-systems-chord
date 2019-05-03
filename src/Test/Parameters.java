package Test;

public class Parameters {

    private static int counter;

    public Parameters(int counter) {
        Parameters.counter = counter;
    }

    public int getCounter() {
        return Parameters.counter;
    }

    public void setCounter(int counter) {
        Parameters.counter = counter;
    }

    public static void add(){
        Parameters.counter ++;
    }

}
