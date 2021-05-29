/******************************************************************************
 *  Name: Bingyue Bei
 *  Date: 2021/05/29
 *  Description: Weekly Project #2: Seam
 *****************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {
    private static final double BORDER_ENERGY = 1000;
    private Picture picture;
    private int width, height;
    /** create a seam carver object based on the given picture. */
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Argument to the Seam Carver Constructor is null.");
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }
    /** return current picture. */
    public Picture picture() {
        return new Picture(picture);
    }
    /** return the width of current picture. */
    public int width() {
        return width;
    }
    /** return the height of current picture. */
    public int height() {
        return height;
    }
    /** energy of pixel at column x and row y. */
    public double energy(int x, int y) {
        if (x >= width || y >= height || x < 0 || y < 0)
            throw new IllegalArgumentException("Column or row exceed possible range.");
        Color leftP, rightP, upperP, lowerP;
        if (0 < x && x < width - 1) {
            lowerP = picture.get(x - 1, y);
            upperP = picture.get(x + 1, y);
        } else {
            return BORDER_ENERGY;
        }
        if (0 < y && y < height - 1) {
            leftP = picture.get(x, y - 1);
            rightP = picture.get(x, y + 1);
        } else {
            return BORDER_ENERGY;
        }
        double dx = calculateDx(leftP, rightP);
        double dy = calculateDx(lowerP, upperP);

        return Math.sqrt(dx + dy);
    }
    /** calculate the square difference of RGB along the x and y axis. */
    private double calculateDx(Color c1, Color c2) {
        return Math.pow((c1.getRed() - c2.getRed()), 2) +
                Math.pow((c1.getGreen() - c2.getGreen()), 2) +
                Math.pow((c1.getBlue() - c2.getBlue()), 2);
    }
    /** sequence of indices for horizontal seam. */
    public int[] findHorizontalSeam() {
        double[][] energy = new double[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy[j][i] = energy(j, i);
            }
        }
        return topologicalSP(energy, height, width);
    }
    /** sequence of indices for vertical seam. */
    public int[] findVerticalSeam() {
        double[][] energy = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy[i][j] = energy(j, i);
            }
        }
        return topologicalSP(energy, width, height);
    }
    /** find a shortest path through topological sort. */
    private int[] topologicalSP(double[][] energy, int col, int row) {
        assert (energy.length == row);
        assert (energy[0].length == col);

        int[][] pixelTo = new int[row][col];
        double[][] distTo = new double[row][col];
        for (int j = 0; j < col; j++) {
            distTo[0][j] = energy[0][j];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 0; j < col; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 0; i < row - 1; i++) {
            for (int j = 0; j < col; j++) {
                if (j - 1 >= 0 && distTo[i + 1][j - 1] > distTo[i][j]+ energy[i + 1][j - 1]) {
                    distTo[i + 1][j - 1] = distTo[i][j]+ energy[i + 1][j - 1];
                    pixelTo[i + 1][j - 1] = j;
                }
                if (j + 1 < col && distTo[i + 1][j + 1] > distTo[i][j]+ energy[i + 1][j + 1]) {
                    distTo[i + 1][j + 1] = distTo[i][j]+ energy[i + 1][j + 1];
                    pixelTo[i + 1][j + 1] = j;
                }
                if (distTo[i + 1][j] > distTo[i][j]+ energy[i + 1][j]) {
                    distTo[i + 1][j] = distTo[i][j]+ energy[i + 1][j];
                    pixelTo[i + 1][j] = j;
                }
            }
        }
        int[] seam = new int[row];
        double minD = Double.POSITIVE_INFINITY;
        for (int i = 0; i < col; i++) {
            if (minD > distTo[row - 1][i])  {
                minD = distTo[row - 1][i];
                seam[row - 1] = i;
            }
        }
        for (int i = row - 1; i > 0; i--) {
            seam[i - 1] = pixelTo[i][seam[i]];
        }
        return seam;
    }
    /** remove horizontal seam from current picture.*/
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width)
            throw new IllegalArgumentException("Input horizontal seam is null or has wrong size");
        if (height <= 1)
            throw new IllegalArgumentException("Picture too narrow to remove horizontal seam");
        for (int i = 1; i < seam.length; i++) {
            if (seam[i - 1] < seam[i] - 1 || seam[i - 1] > seam[i] + 1)
                throw new IllegalArgumentException("successive entries in seam[] must differ by -1, 0, or +1");
        }
        Picture newPict = new Picture(width, height - 1);
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                if (i < seam[j]) {
                    newPict.setRGB(j, i, picture.getRGB(j, i));
                } else if (i > seam[j]) {
                    newPict.setRGB(j, i - 1, picture.getRGB(j, i));
                }
            }
        }
        height -= 1;
        picture = newPict;
    }
    /** remove vertical seam from current picture.*/
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height)
            throw new IllegalArgumentException("Input vertical seam is null or has wrong size");
        if (width <= 1)
            throw new IllegalArgumentException("Picture too narrow to remove vertical seam");
        for (int i = 1; i < seam.length; i++) {
            if (seam[i - 1] < seam[i] - 1 || seam[i - 1] > seam[i] + 1)
                throw new IllegalArgumentException("successive entries in seam[] must differ by -1, 0, or +1");
        }
        Picture newPict = new Picture(width - 1, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j < seam[i]) {
                    newPict.setRGB(j, i, picture.getRGB(j, i));
                } else if (j > seam[i]) {
                    newPict.setRGB(j -1, i, picture.getRGB(j, i));
                }
            }
        }
        width -= 1;
        picture = newPict;
    }
    /**  unit testing (optional). */
    public static void main(String[] args) {
        Picture picture1 = new Picture("3x4.png");
        SeamCarver test1 = new SeamCarver(picture1);
        StdOut.println("Test #1, Image 3x4.png");
        for (int j = 0; j < test1.height; j++) {
            for (int i = 0; i < test1.width; i++) {
                StdOut.printf("%f\t", test1.energy(i, j));
            }
            StdOut.println();
        }
        StdOut.print("The vertical Seam array is: ");
        for (int i: test1.findVerticalSeam()) {
            StdOut.printf("%d ", i);
        }
        StdOut.println();
        StdOut.print("The horizontal Seam array is: ");
        for (int i: test1.findHorizontalSeam()) {
            StdOut.printf("%d ", i);
        }
        StdOut.println("\n");
        test1.removeHorizontalSeam(new int[]{0, 1, 0});

        Picture picture2 = new Picture("6x5.png");
        SeamCarver test2 = new SeamCarver(picture2);
        StdOut.println("Test #2, Image 6x5.png");
        for (int j = 0; j < test2.height; j++) {
            for (int i = 0; i < test2.width; i++) {
                StdOut.printf("%f\t", test2.energy(i, j));
            }
            StdOut.println();
        }
        StdOut.print("The vertical Seam array is: ");
        for (int i: test2.findVerticalSeam()) {
            StdOut.printf("%d ", i);
        }
        StdOut.println();
        StdOut.print("The horizontal Seam array is: ");
        for (int i: test2.findHorizontalSeam()) {
            StdOut.printf("%d ", i);
        }
        test2.removeVerticalSeam(new int[]{1, 2, 3, 2, 3});
    }
}
