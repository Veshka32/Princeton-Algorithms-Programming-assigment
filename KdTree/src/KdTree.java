import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    // construct an empty set of points
    private static final boolean VERTICAL = true; //horizontal = false;
    private Node root;
    private int treeSize = 0;

    private static class Node {
        Point2D point;      // the point
        RectHV rect;    // the axis-aligned rectangle corresponding to this node
        Node lb;        // the left/bottom subtree
        Node rt;        // the right/top subtree
        boolean orient; // vertical=true, horizontal=false;

        public Node(Point2D p) {
            this.point = p;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return treeSize;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) {
            root = new Node(p);
            root.orient = true;
            root.rect = new RectHV(0, 0, 1, 1);
            treeSize += 1;
        } else {
            if (!contains(p)) {
                root = insert(root, p);
                treeSize += 1;
            }
        }
    }

    private boolean isVertical(Node x) {
        return x.orient == VERTICAL;
    }

    private Node insert(Node x, Point2D point) {
        if (x == null) return new Node(point);

        if (isVertical(x)) {
            if (point.x() < x.point.x()) {
                x.lb = insert(x.lb, point);
                if (x.lb.rect == null) {
                    x.lb.orient = false;
                    x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.point.x(), x.rect.ymax());
                }
            } else {
                x.rt = insert(x.rt, point);
                if (x.rt.rect == null) {
                    x.rt.orient = false;
                    x.rt.rect = new RectHV(x.point.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
                }
            }
            return x;
        } else {
            if (point.y() < x.point.y()) {
                x.lb = insert(x.lb, point);
                if (x.lb.rect == null) {
                    x.lb.orient = true;
                    x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.point.y());
                }

            } else {
                x.rt = insert(x.rt, point);
                if (x.rt.rect == null) {
                    x.rt.orient = true;
                    x.rt.rect = new RectHV(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.rect.ymax());
                }
            }
            return x;
        }
    }

    //does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        Node x = root;
        while (x != null) {
            if (x.point.equals(point)) return true;
            if (isVertical(x)) {
                if (point.x() < x.point.x()) {
                    x = x.lb;
                } else x = x.rt;
            } else {
                if (point.y() < x.point.y()) {
                    x = x.lb;
                } else x = x.rt;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw();
        if (isVertical(x)) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());
        }
        draw(x.rt);
        draw(x.lb);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearP = root.point;
        return nearest(root, p, nearP);

    }

    private Point2D nearest(Node x, Point2D p, Point2D currentNearest) {
        if (x == null) return currentNearest;
        if (p.distanceSquaredTo(currentNearest) <= x.rect.distanceSquaredTo(p)) return currentNearest;
        //System.out.println("check " + x.point.toString());
        if (p.distanceSquaredTo(x.point) < p.distanceSquaredTo(currentNearest)) {
            currentNearest = x.point;
        }
        if (queryPointInLB(x, p)) {
            currentNearest = nearest(x.lb, p, currentNearest);
            currentNearest = nearest(x.rt, p, currentNearest);
        } else {

            currentNearest = nearest(x.rt, p, currentNearest);
            currentNearest = nearest(x.lb, p, currentNearest);
        }
        return currentNearest;
    }

    private boolean queryPointInLB(Node x, Point2D p) {

        if (x.orient && p.x() < x.point.x()) return true;
        if (!x.orient && p.y() < x.point.y()) return true;
        return false;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> pointsInRange = new Stack<>();
        range(root, rect, pointsInRange);
        return pointsInRange;
    }

//    private void range(Node x, RectHV rect, Stack<Point2D> stack) {
//        if (x == null) return;
//        if (!x.rect.intersects(rect))
//            return; //instead of rect.intersect(Rect) -> rect.intersect splitline; if does, check point; if not - check 1/hafl
//        if (rect.contains(x.point)) {
//            stack.push(x.point);
//        }
//        range(x.rt, rect, stack);
//        range(x.lb, rect, stack);
//    }

    //better implementation: check rect.intersects splitline; if does, check point and its full rect; if not - check 1/half
    private void range(Node x, RectHV rectangle, Stack<Point2D> answer) {
        if (x == null) return;
        if (rectIntersectLine(x, rectangle)) {
            if (rectangle.contains(x.point)) answer.push(x.point);
            range(x.rt, rectangle, answer);
            range(x.lb, rectangle, answer);
        } else {
            if (isRectLeftBottomFromLine(x, rectangle))
                range(x.lb, rectangle, answer);
            else
                range(x.rt, rectangle, answer);
        }
    }

    private boolean rectIntersectLine(Node x, RectHV rectangle) {
        if (x.orient) {
            if (rectangle.xmin() <= x.point.x() && x.point.x() <= rectangle.xmax()) return true;
            else return false;
        } else {
            if (rectangle.ymin() <= x.point.y() && x.point.y() <= rectangle.ymax()) return true;
            else return false;
        }
    }

    private boolean isRectLeftBottomFromLine(Node x, RectHV rectangle) {
        if (x.orient) {
            if (x.point.x() > rectangle.xmax()) return true;
            else return false;
        } else {
            if (x.point.y() > rectangle.ymax()) return true;
            else return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("OK");
    }
}

