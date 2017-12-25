import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    final private double[] openSites;
    final private int trials;
    private Double mean;
    private Double stdDev;
    private final double CONFIDENCE_95=1.96;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n<=0 || trials<=0) throw new IllegalArgumentException();
        this.trials = trials;
        openSites = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation test = new Percolation(n);
            while (!test.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                test.open(row, col);
            }
            openSites[trial] = (double)(test.numberOfOpenSites()) / (n * n);
        }
    }

    public static void main(String[] args) {
        PercolationStats test = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println(test.mean());
        StdOut.println(test.stddev());
        StdOut.println(test.confidenceHi());
        StdOut.println(test.confidenceHi());
    }

    public double mean() {
        mean = StdStats.mean(openSites);
        return mean;
    }

    public double stddev() {
        stdDev = StdStats.stddev(openSites);
        return stdDev;
    }

    public double confidenceLo() {
        if (mean == null) mean();
        if (stdDev == null) stddev();
        return mean - CONFIDENCE_95 * (stdDev) / Math.sqrt(trials);

    }

    public double confidenceHi() {
        if (mean == null) mean();
        if (stdDev == null) stddev();
        return mean + CONFIDENCE_95 * (stdDev) / Math.sqrt(trials);
    }

}
