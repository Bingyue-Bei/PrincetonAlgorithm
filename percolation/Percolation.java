/****************************
 * Created by Bingyue Bei
 * Finished on 05/24/2020
 * **************************
 * Solving the backwash problem:
 * create an array that keeps track of the virtual bottom connectivity.
 * Before an union operation,check if any of the root is connected to bottom.
 * If so, update the root after union.
 * To check whether percolates, find the root of the virtual top node,
 * and check the bottom connectivity of that root.
 * **************************
 * Comparing to the method that creates two WeightedQuickUnion Data structure,
 * this solution uses less memories.
 * **************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    /** represent the grid. */
    private final WeightedQuickUnionUF grid;

    /** represent the status of each site. */
    private boolean[] status;

    /** Whether a root is connected to the virtual bottom. */
    private boolean[] bottomConnectivity;

    /** represent the size of the grid; */
    private final int n;

    /** creates n-by-n grid, with all sites initially blocked. */
    public Percolation(int size) {
        if (size <= 0)
            throw new IllegalArgumentException();

        n = size;
        int length = n * n + 1;
        grid = new WeightedQuickUnionUF(length);

        status = new boolean[length];
        bottomConnectivity = new boolean[length];

        for (int i = 0; i < status.length; i++) {
            status[i] = false;
            bottomConnectivity[i] = false;
        }

    }

    /** Opens the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (!inRange(row, col))
            throw new IllegalArgumentException();

        if (isOpen(row, col))
            return;

        int index = convert(row, col);
        status[index] = true;
        int root = grid.find(index);
        if (row == n) {
            bottomConnectivity[root] = true;
        }

        if (inRange(row - 1, col) && isOpen(row - 1, col)) {
            int rootQ1 = grid.find(convert(row - 1, col));
            grid.union(convert(row - 1, col), index);
            if (bottomConnectivity[rootQ1] || bottomConnectivity[root]) {
                root = grid.find(index);
                bottomConnectivity[root] = true;
            }
        }

        if (inRange(row + 1, col) && isOpen(row + 1, col)) {
            int rootQ2 = grid.find(convert(row + 1, col));
            grid.union(convert(row + 1, col), index);
            if (bottomConnectivity[rootQ2] || bottomConnectivity[root]) {
                root = grid.find(index);
                bottomConnectivity[root] = true;
            }
        }

        if (inRange(row, col - 1) && isOpen(row, col - 1)) {
            int rootQ3 = grid.find(convert(row, col - 1));
            grid.union(convert(row, col - 1), index);
            if (bottomConnectivity[rootQ3] || bottomConnectivity[root]) {
                root = grid.find(index);
                bottomConnectivity[root] = true;
            }
        }

        if (inRange(row, col + 1) && isOpen(row, col + 1)) {
            int rootQ4 = grid.find(convert(row, col + 1));
            grid.union(convert(row, col + 1), index);
            if (bottomConnectivity[rootQ4] || bottomConnectivity[root]) {
                root = grid.find(index);
                bottomConnectivity[root] = true;
            }
        }

        if (row == 1) {
            int rootTop = grid.find(0);
            grid.union(0, convert(row, col));
            if (bottomConnectivity[rootTop] || bottomConnectivity[root]) {
                root = grid.find(index);
                bottomConnectivity[root] = true;
            }
        }
    }

    /** Is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        if (!inRange(row, col))
            throw new IllegalArgumentException();

        return status[convert(row, col)];
    }

    /** Is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        if (!inRange(row, col))
            throw new IllegalArgumentException();

        if (!isOpen(row, col))
            return false;

        return grid.find(0) == grid.find(convert(row, col));

    }

    /** Returns the number of open sites. */
    public int numberOfOpenSites() {
        int sum = 0;
        for (int i = 0; i < status.length; i++) {
            if (status[i]) {
                sum++;
            }
        }
        return sum;
    }

    /** does the system percolate? */
    public boolean percolates() {
        if (n == 1)
            return isOpen(1, 1);

        int rootTop = grid.find(0);
        return bottomConnectivity[rootTop];
    }

    /** test client (optional)*/
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 1);
        perc.open(2, 1);
        perc.open(3, 3);
        perc.open(3, 1);
        System.out.println(perc.numberOfOpenSites());
        System.out.println(perc.isFull(3, 3));
    }

    /** Convert the Cartesian coordinates to array index. */
    private int convert(int row, int col) {
        return (row - 1) * n + col;
    }

    /** Return if a coordinate is a valid array index.*/
    private boolean inRange(int row, int col) {
        return (0 < row) && (row <= n) && (0 < col) && (col <= n);
    }
}

/**
 * index 0 represent the virtual top;
 * index length represent the virtual bottom;
 * grid index start at 1.
 * Conversion between 2-dimensional grid and 1-dimensional array goes like this:
 * (row - 1) * n + column = index
 * Need an extra array to indicate if a site is open.
 * Virtual Top and Virtual Bottom sites are always marked as closed in this array.
 * The sum of the this array is the number of open sites.
 * */