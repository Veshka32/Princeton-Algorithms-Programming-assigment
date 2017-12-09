import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;
//throw new IllegalArgumentException() if any arg==null
//insert() and contains() - > logN worst; nearest() and range() ->N;

public class PointSET {
    // construct an empty set of points
    private final SET<Point2D> pointsSet = new SET<>();

    // is the set empty?
    public boolean isEmpty() {
        return pointsSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointsSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        pointsSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointsSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        Iterator<Point2D> allPoints = pointsSet.iterator();
        while (allPoints.hasNext()) {
            allPoints.next().draw();
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointsSet.isEmpty()) return null;
        Iterator<Point2D> allPoints = pointsSet.iterator();
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = p;
        while (allPoints.hasNext()) {
            Point2D curPoint = allPoints.next();
            double curDist = p.distanceSquaredTo(curPoint);
            if (curDist < minDist) {
                minDist = curDist;
                nearest = curPoint;
            }
        }
        return nearest;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> pointsInRange = new Stack<>();
        Iterator<Point2D> allPoints = pointsSet.iterator();
        while (allPoints.hasNext()) {
            Point2D current = allPoints.next();
            if (rect.contains(current)) {
                pointsInRange.push(current);
            }
        }
        return pointsInRange;
    }

    public static void main(String[] args) {
        System.out.println("OK");
    }

}
