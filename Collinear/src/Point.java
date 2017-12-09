/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    public Point(int x, int y) { //nitializes a new point.
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    public void draw() {
        /* Draws this point to standard draw*/
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        /* Draws the line segment between this point and the specified point to standard draw. */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) return Double.NEGATIVE_INFINITY;
        else if (this.x == that.x) return Double.POSITIVE_INFINITY;
        else if (this.y == that.y) return +0.0;
        else return  (double) (that.y - this.y) / (double) (that.x - this.x);
    }
    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        //if (that==null || this==null) throw new IllegalArgumentException();
        if (this.y > that.y) return 1;
        else if (this.y < that.y) return -1;
        else {
            if (this.x < that.x) return -1;
            else if (this.x > that.x) return 1;
            else return 0;
        }
    }

    /* Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     * @return the Comparator that defines this ordering on points*/
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point q1, Point q2) {
            if (Point.this.slopeTo(q1) < Point.this.slopeTo(q2)) return -1;   // fix Point.this - > this
            else if (Point.this.slopeTo(q1) > Point.this.slopeTo(q2)) return 1;
            else return 0;
        }
    }

    public Comparator<Point> slopeOrder() {
        Comparator<Point> SLOPE_ORDER = new SlopeOrder();
        return SLOPE_ORDER;
    }

    public String toString() {
        /* Returns a string representation of this point*/
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args) {
        Point a = new Point(0, 0);
        Point b = new Point(0, 0);
        Point c=new Point(2,5);
        Point[] set = new Point[]{a, b,c};
        Arrays.sort(set, a.slopeOrder());
        for (int i = 0; i < 3; i++) {
            System.out.println(set[i].toString());
        }
        System.out.print(a.slopeTo(b)==Double.NEGATIVE_INFINITY);
    }


}
