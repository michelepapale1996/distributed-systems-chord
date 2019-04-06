package chord;

public class Debugger {
    private static boolean debug;

    public static void setDebug(boolean debug) {
        Debugger.debug = debug;
    }

    public static void print(String toPrint){
        if(debug) System.out.println(toPrint);
    }
}
