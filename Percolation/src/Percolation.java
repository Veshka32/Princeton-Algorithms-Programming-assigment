import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    final private WeightedQuickUnionUF percolation;
    final private WeightedQuickUnionUF full;
    final private int n;
    private boolean[] openSites;
    private int openSitesCount = 0;
    final private int top;
    final private int bottom;

    public Percolation(int n) {
        if (n<=0) throw new IllegalArgumentException();
        percolation = new WeightedQuickUnionUF(n * n + 2);
        full = new WeightedQuickUnionUF(n * n + 2);
        this.n = n;
        openSites = new boolean[n * n+1];
        top = 0;
        bottom = n*n+1;
    }

    private boolean isValid(int row, int col) {
        if (col <= 0 || col > n || row <= 0 || row > n) return false;
        return true;
    }

    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException();
        return openSites[(row-1) * n + col];
    }

    public void open(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException();
        if (isOpen(row, col)) return;
        int num = (row-1) * n + col;
        if (row == 1) {
            percolation.union(num, top);
            full.union(num, top);
        }
        if (row == n) percolation.union(num, bottom);
        int[][] neighbors = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
        for (int[] neighbor : neighbors) {
            if (!isValid(neighbor[0], neighbor[1])) continue;
            int num1=(neighbor[0]-1)*n+neighbor[1];
            if (isOpen(neighbor[0], neighbor[1])) {
                percolation.union(num1, num);
                full.union(num1, num);
            }
        }
        openSites[num] = true;
        openSitesCount++;
    }

    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException();
        return full.connected((row-1) * n + col, top);
    }

    public boolean percolates() {
        return percolation.connected(top, bottom);
    }

    public int numberOfOpenSites() {
        return openSitesCount;
    }

    public static void main(String[] args) {
        Percolation test=new Percolation(10);
        test.open(1,1);
        test.open(2,1);
        test.open(2,2);
        System.out.println(test.isFull(2,2));
    }
}







