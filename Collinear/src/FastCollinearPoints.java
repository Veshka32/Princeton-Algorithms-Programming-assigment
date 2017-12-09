import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final LineSegment[] segmentsFinal;

    public FastCollinearPoints(Point[] points) {   // constructor
        if (points == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }
        Point[] check = Arrays.copyOf(points, points.length);
        Arrays.sort(check);
        for (int i = 0; i < points.length - 1; i++) {
            if (check[i].compareTo(check[i + 1]) == 0) throw new IllegalArgumentException();
        }
        segmentsFinal = findAllSegments(points);
    }

    private LineSegment[] findAllSegments(Point[] points) { //find all segments >=4
        if (points.length < 4) {
            return new LineSegment[0];
        }

        List<Point> startPoints = new ArrayList<Point>();
        List<Point> finishPoints = new ArrayList<Point>();
        List<Double> slopes = new ArrayList<>();

        Point[] sortedPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(sortedPoints);
        Point[] pointsForSort = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Point current = sortedPoints[i];
            Arrays.sort(pointsForSort, current.slopeOrder());
            if (pointsForSort[1]==pointsForSort[points.length-1]) return new LineSegment[]{new LineSegment(sortedPoints[0],sortedPoints[sortedPoints.length-1])};

            int num = 2; //current number of collinear points
            for (int j = 1; j < points.length - 1; j++) {
                if (current.slopeTo(pointsForSort[j]) == current.slopeTo(pointsForSort[j + 1])) {
                    if (j != points.length - 2) {
                        num += 1;
                        continue;
                    } else {
                        num += 1;
                        j += 1;
                    }
                }
                if (num >= 4) {
                    Point[] goodPoints = new Point[num];
                    for (int n = 0; n < num - 1; n++) {
                        goodPoints[n] = pointsForSort[j - n];
                    }
                    goodPoints[num - 1] = current;
                    Arrays.sort(goodPoints);

                    if (checkNotRepeat(startPoints, slopes, goodPoints[0], goodPoints[num - 1])) {
                        startPoints.add(goodPoints[0]);
                        finishPoints.add(goodPoints[num - 1]);
                        slopes.add(goodPoints[0].slopeTo(goodPoints[num - 1]));
                    }
                }
                num = 2;
            }
        }
        LineSegment[] segments4 = new LineSegment[startPoints.size()];
        for (int i = 0; i < startPoints.size(); i++) {
            segments4[i] = new LineSegment(startPoints.get(i), finishPoints.get(i));
        }
        return segments4;
    }

    private boolean checkNotRepeat(List<Point> startPoints, List<Double> slopes, Point start, Point finish) {
        if (startPoints.isEmpty()) return true;
        if (start.compareTo(startPoints.get(0)) < 0 || start.compareTo(startPoints.get(startPoints.size() - 1)) > 0)
            return true;
        int index = Arrays.binarySearch(startPoints.toArray(new Point[startPoints.size()]), start);
        if (index < 0) return true;
        double slope = start.slopeTo(finish);

        for (int i = index; i < startPoints.size(); i++) { //to right from index
            int compare = startPoints.get(i).compareTo(start);
            if (compare > 0 || slopes.get(i) > slope) break;
            if (compare == 0 && slopes.get(i) == slope) return false;
        }

        for (int i = index; i >= 0; i--) { //to left from index
            int compare = startPoints.get(i).compareTo(start);
            if (compare < 0 || slopes.get(i) < slope) return true;
            if (compare == 0 && slopes.get(i) == slope) return false;
        }
        return true;
    }

    public int numberOfSegments() {     // the number of line segments
        return segments().length;
    }

    public LineSegment[] segments() {                // the line segments
        LineSegment[] segments = Arrays.copyOf(this.segmentsFinal, this.segmentsFinal.length);
        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println(collinear.numberOfSegments());
        StdDraw.show();
    }

}
