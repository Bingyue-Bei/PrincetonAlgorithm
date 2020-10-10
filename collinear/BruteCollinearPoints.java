/*******************************************************************************
 *  Created by Bingyue Bei, 2020/10/05
 *  Brute Force solution to collinear problem, takes n^4 runtime.
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {
    /** The initial size of the array. */
    private static final int INIT_SIZE = 4;
    /** The number of collinear line segment. */
    private int count = 0;
    /** The array of collinear line segment. */
    private LineSegment[] segment;
    /** finds all line segments containing 4 points. */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        for (int i = 1; i < copy.length; i++) {
            if (copy[i - 1].equals(copy[i])) {
                throw new IllegalArgumentException();
            }
        }
        segment = new LineSegment[INIT_SIZE];
        for (int p = 0; p < copy.length - 3; p++) {
            for (int q = p + 1; q < copy.length - 2; q++) {
                for (int r = q + 1; r < copy.length - 1; r++) {
                    for (int s = r + 1; s < copy.length; s++) {
                        if (copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[r])
                                && copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[s])) {
                            if (count == segment.length) {
                                resize(count * 2 + 1);
                            }
                            segment[count] = new LineSegment(copy[p], copy[s]);
                            count += 1;
                        }
                    }
                }
            }
        }
        resize(count);
    }
    /** resize the answer array. */
    private void resize(int length) {
        LineSegment[] newSegment = new LineSegment[length];
        for (int i = 0; i < segment.length && i < length; i++) {
            newSegment[i] = segment[i];
        }
        segment = newSegment;
    }
    /** the number of line segments. */
    public int numberOfSegments() {
        return count;
    }
    /** the line segments. */
    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[count];
        for (int i = 0; i < count; i++) {
            lines[i] = segment[i];
        }
        return lines;
    }
}
