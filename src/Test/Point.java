package Test;

public class Point {
    private Double x;
    private Double y;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    // draw point using standard draw
    public void draw() {
        StdDraw.point(x, y);
    }

    // return string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static void main (String args[]){
        int i;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        Double x;
        for (i = 0; i <= 10; i++){
            x =  new Double ((double) i/10);
            StdDraw.point(x,x);
        }
    }
}
