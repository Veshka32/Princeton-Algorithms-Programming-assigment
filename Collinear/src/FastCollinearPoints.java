import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private final LineSegment[] segmentsFinal;
    private Point[] sortedPoints;

    public FastCollinearPoints(Point[] points) {   // constructor
        if (points == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }
        sortedPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(sortedPoints);
        for (int i = 0; i < points.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) throw new IllegalArgumentException();
        }
        segmentsFinal = findAllSegments(points);
    }

    private LineSegment[] findAllSegments(Point[] points) { //find all segments >=4
        if (points.length < 4) {
            return new LineSegment[0];
        }

        List<Point> startPoints = new ArrayList<Point>();
        List<Point> finishPoints = new ArrayList<Point>();

        Point[] slopeSortingPoints = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Point current = sortedPoints[i];
            Arrays.sort(slopeSortingPoints, current.slopeOrder());

            int num = 2; //current number of collinear points
            for (int j = 1; j < points.length - 1; j++) {
                if (current.slopeTo(slopeSortingPoints[j]) == current.slopeTo(slopeSortingPoints[j + 1])) {
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
                        goodPoints[n] = slopeSortingPoints[j - n];
                    }
                    goodPoints[num - 1] = current;
                    Arrays.sort(goodPoints);
                    startPoints.add(goodPoints[0]);
                    finishPoints.add(goodPoints[num - 1]);
                }
                num = 2;
            }
        }
        Integer[] indexes=new Integer[finishPoints.size()];
        for (int i=0;i<finishPoints.size();i++){
            indexes[i]=i;
        }
        //sort index of finish point by comparing start point: for fast finding duplicate line segments
        //
        Arrays.sort(indexes,new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                int c=startPoints.get(o1).compareTo(startPoints.get(o2));
                if (c==0) return finishPoints.get(o1).compareTo(finishPoints.get(o2));
                else return c;
            }
        });
        Collections.sort(startPoints);
        if (startPoints.isEmpty()) return new LineSegment[0];
        ArrayList<LineSegment> segments4 = new ArrayList<>();
        segments4.add(new LineSegment(startPoints.get(0), finishPoints.get(indexes[0])));
        for (int i = 1; i < startPoints.size(); i++) {
            if (startPoints.get(i).equals(startPoints.get(i-1)) && finishPoints.get(indexes[i]).equals(finishPoints.get(indexes[i-1]))) continue;
            segments4.add(new LineSegment(startPoints.get(i), finishPoints.get(indexes[i])));
        }
        return segments4.toArray(new LineSegment[segments4.size()]);
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
