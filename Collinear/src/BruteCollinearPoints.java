import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segmentsFinal;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points;
        if (points == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null)
                    throw new IllegalArgumentException();
                if (isEgual(points[i], points[j]))
                    throw new IllegalArgumentException();
            }
        }
        segmentsFinal = findAllSegments(points);
    }

    private boolean isEgual(Point a, Point b) {
        return a.compareTo(b) == 0;
    }

    private LineSegment[] findAllSegments(Point[] points) {
        LineSegment[] segments4 = new LineSegment[points.length];
        int ind = 0;
        int length = 0;
        for (int i = 0; i < points.length - 3; i++) {
            Point start = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                double startSlope = start.slopeTo(points[j]);
                for (int k = j + 1; k < points.length - 1; k++) {
                    if (startSlope == start.slopeTo(points[k])) {
                        for (int m = k + 1; m < points.length; m++) {
                            if (startSlope == start.slopeTo(points[m])) {
                                Point[] collinearPoints = new Point[]{points[i], points[j], points[k], points[m]};
                                Arrays.sort(collinearPoints);
                                LineSegment newSegment = new LineSegment(collinearPoints[0], collinearPoints[3]);
                                segments4[ind++] = newSegment;
                                length += 1;
                            }
                        }
                    }
                }
            }
        }
        LineSegment[] finalSegments = Arrays.copyOf(segments4, length);
        System.out.print(finalSegments.length);
        return finalSegments;
    }

    public int numberOfSegments() {     // the number of line segments
        return segments().length;
    }

    public LineSegment[] segments() {                // the line segments
        LineSegment[] segments4 = Arrays.copyOf(this.segmentsFinal, this.segmentsFinal.length);
        return segments4;
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
        StdDraw.setPenRadius(0.02);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

