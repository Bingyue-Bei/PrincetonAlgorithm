import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/****************************
 * Created by Bingyue Bei
 * Finished on 05/24/2020
 * **************************/

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int size;
    private final int trial;
    private final int[] threshold;

    /** perform independent trials on an n-by-n grid. */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        trial = trials;
        threshold = new int[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(size);

            while (!perc.percolates()) {

                int row = StdRandom.uniform(size);
                int column = StdRandom.uniform(size);

                perc.open(row + 1, column + 1);
            }

            threshold[i] = perc.numberOfOpenSites();
        }
    }

    /** sample mean of percolation threshold. */
    public double mean() {
        return StdStats.mean(threshold) / (size * size);
    }

    /** sample standard deviation of percolation threshold. */
    public double stddev() {
        return StdStats.stddev(threshold) / (size * size);
    }

    /** low endpoint of 95% confidence interval. */
    public double confidenceLo() {
        double mean = mean();
        double std = stddev();
        return mean - (CONFIDENCE_95 * std / Math.sqrt(trial));
    }

    /** high endpoint of 95% confidence interval/ */
    public double confidenceHi() {
        double mean = mean();
        double std = stddev();
        return mean + (CONFIDENCE_95 * std / Math.sqrt(trial));
    }

    /** test client (see below). */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStat = new PercolationStats(n, trials);

        System.out.println("mean                    = " + percStat.mean());
        System.out.println("stddev                  = " + percStat.stddev());
        System.out.println("95% confidence interval = [" + percStat.confidenceLo() + ", " + percStat.confidenceHi() + "]");
    }
}
