import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {
    private final Digraph sap;

    public SAP(Digraph G) {
        sap = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(sap, w);
        int minDist = sap.V() + 1;
        int anc = -1;
        for (int i = 0; i < sap.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i)) {
                int dist = forV.distTo(i) + forW.distTo(i);
                if (dist < minDist) {
                    minDist = dist;
                    anc = i;
                }
                if (minDist == 0) break;
            }
        }
        if (anc == -1) minDist = -1;
        return minDist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(sap, w);
        int minDist = sap.V() + 1;
        int anc = -1;
        for (int i = 0; i < sap.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i)) {
                int dist = forV.distTo(i) + forW.distTo(i);
                if (dist < minDist) {
                    minDist = dist;
                    anc = i;
                }
                if (minDist == 0) break;
            }
        }
        return anc;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(sap, w);
        int minDist = sap.V() + 1;
        int anc = -1;
        for (int i = 0; i < sap.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i)) {
                int dist = forV.distTo(i) + forW.distTo(i);
                if (dist < minDist) {
                    minDist = dist;
                    anc = i;
                }
                if (minDist == 0) break;
            }
        }
        if (anc == -1) minDist = -1;
        return minDist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(sap, w);
        int minDist = sap.V() + 1;
        int anc = -1;
        for (int i = 0; i < sap.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i)) {
                int dist = forV.distTo(i) + forW.distTo(i);
                if (dist < minDist) {
                    minDist = dist;
                    anc = i;
                }
                if (minDist == 0) break;
            }
        }
        return anc;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        System.out.println(sap.ancestor(4, 6));
        System.out.println(sap.length(4, 6));
    }
}
