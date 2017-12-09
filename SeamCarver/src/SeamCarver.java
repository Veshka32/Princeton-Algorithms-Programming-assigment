import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    // create a seam carver object based on the given picture
    private int[][] colors;
    private double[][] energyOfPix;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        colors = new int[picture.height()][picture.width()];
        for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++) {
                colors[row][col] = picture.getRGB(col, row);
            }
        }
        energyOfPix = new double[height()][width()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                energyOfPix[row][col] = energy(col, row);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture image = new Picture(width(), height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                image.set(col, row, new Color(colors[row][col]));
            }
        }
        return image;
    }

    // width of current picture
    public int width() {
        return colors[0].length;
    }

    // height of current picture
    public int height() {
        return colors.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1) throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) return 1000.0;

        Color leftPix = new Color(colors[y][(x - 1)]);
        Color rightPix = new Color(colors[y][(x + 1)]);
        double xR = rightPix.getRed() - leftPix.getRed();
        double xG = rightPix.getGreen() - leftPix.getGreen();
        double xB = rightPix.getBlue() - leftPix.getBlue();
        double xEnergy = Math.pow((Math.pow(xR, 2) + Math.pow(xG, 2) + Math.pow(xB, 2)), 0.5);

        Color upPix = new Color(colors[(y + 1)][x]);
        Color downPix = new Color(colors[(y - 1)][x]);
        double yR = upPix.getRed() - downPix.getRed();
        double yG = upPix.getGreen() - downPix.getGreen();
        double yB = upPix.getBlue() - downPix.getBlue();
        double yEnergy = Math.pow((Math.pow(yR, 2) + Math.pow(yG, 2) + Math.pow(yB, 2)), 0.5);

        double dualGradient = Math.pow((Math.pow(xEnergy, 2) + Math.pow(yEnergy, 2)), 0.5);
        return dualGradient;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        traverse();
        int[] seam = findVerticalSeam();
        traverse();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()]; //x-coordinate of pxls in seam
        if (width() == 1) {
            for (int i = 0; i < seam.length; i++) {
                seam[i] = 0;
            }
            return seam;
        }
        int[][] pxlTo = new int[height()][width()];//x of previous pxl
        double[][] distTo = new double[height()][width()];

        for (int col = 0; col < width(); col++) {
            distTo[0][col] = energyOfPix[0][col];
        }
        for (int row = 1; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }

        int[] adj;
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                if (col == 0) adj = new int[]{col, col + 1};
                else if (col == width() - 1) adj = new int[]{col - 1, col};
                else adj = new int[]{col - 1, col, col + 1};
                for (int vertex : adj) relax(row, col, row + 1, vertex, distTo, pxlTo);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        int nextCol = 0;
        for (int col = 0; col < width(); col++) {
            if (distTo[height() - 1][col] < min) {
                min = distTo[height() - 1][col];
                nextCol = col;
            }
        }

        //fill seam[]
        for (int row = height() - 1; row >= 0; row--) {
            seam[row] = nextCol;
            nextCol = pxlTo[row][nextCol]; //must be up to down
        }
        return seam;
    }

    private void relax(int row, int col, int row2, int col2, double[][] dist, int[][] pxlTo) {
        energyOfPix[row2][col2] = energy(col2, row2);
        if (dist[row2][col2] > dist[row][col] + energyOfPix[row2][col2]) {
            dist[row2][col2] = dist[row][col] + energyOfPix[row2][col2];
            pxlTo[row2][col2] = col;
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() <= 1) throw new IllegalArgumentException();
        if (!isValid(seam)) throw new IllegalArgumentException();
        int newWidth = width() - 1;

        for (int row = 0; row < height(); row++) {
            int[] temp = new int[newWidth];
            System.arraycopy(colors[row], 0, temp, 0, seam[row]);
            System.arraycopy(colors[row], seam[row] + 1, temp, seam[row], temp.length - seam[row]);
            colors[row] = temp; // remove pxlx up to down

            double[] temp1 = new double[newWidth];
            System.arraycopy(energyOfPix[row], 0, temp1, 0, seam[row]);
            System.arraycopy(energyOfPix[row], seam[row] + 1, temp1, seam[row], temp1.length - seam[row]);
            energyOfPix[row] = temp1; // remove energy up to down
        }

        //update energy
        for (int row = 1; row < height() - 1; row++) {
            if (seam[row] > 0) energyOfPix[row][seam[row] - 1] = energy(seam[row] - 1, row); //update left pix from seam
            if (seam[row] < newWidth - 1)
                energyOfPix[row][seam[row]] = energy(seam[row], row); //update pix of seam if it's not on boundary
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() <= 1) throw new IllegalArgumentException();
        traverse();
        removeVerticalSeam(seam);
        traverse();
    }

    //check whether vertical seam has wrong length or entry is outside its prescribed range or two adjacent entries differ by more than 1).
    private boolean isValid(int[] seam) {
        if (seam.length != height()) return false;
        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1 || Math.abs(seam[i] - seam[i + 1]) > 1) return false;
        }
        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] > width() - 1) return false;
        return true;
    }

    private void traverse() {
        int newHeight = width();
        int newWidth = height();
        int[][] newColors = new int[newHeight][newWidth];
        double[][] newEnergy = new double[newHeight][newWidth];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                newColors[col][row] = colors[row][col];
                newEnergy[col][row] = energyOfPix[row][col];
            }
        }
        colors = newColors;
        energyOfPix = newEnergy;
    }

//    public void printEnergy() {
//        for (double[] row : energyOfPix) {
//            System.out.println(Arrays.toString(row));
//        }
//    }
//
//    public void energyOfVerticalSeam(int[] s) {
//        double sum = 0;
//        for (int i = 0; i < s.length; i++) {
//            sum += energyOfPix[i][s[i]];
//        }
//        System.out.println(sum);
//    }
//
//    public static void main(String[] args) {
//        SeamCarver test = new SeamCarver(new Picture(args[0]));
//        System.out.println("width= " + test.width());
//        System.out.println("height= " + test.height());
//        test.printEnergy();
//        int[] seam = test.findVerticalSeam();
//        System.out.println(Arrays.toString(seam));
//        test.energyOfVerticalSeam(seam);
//    }
}
