/******************************************************************************
 *  Created by Bingyue Bei, 2020/10/09
 *  Solution to collinear problem based on sorting, takes n^2 * log(n) run time.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {
    /** The initial size of the array. */
    private static final int INIT_SIZE = 4;
    /** The number of collinear line segment. */
    private int count = 0;
    /** The array of collinear line segment. */
    private LineSegment[] segment;
    /** finds all line segments containing 4 or more points. */
    public FastCollinearPoints(Point[] points) {
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
        for (int i = 0; i < copy.length; i++) {
            Point origin = copy[i];
            Point[] destination = Arrays.copyOf(copy, copy.length);
            Arrays.sort(destination, origin.slopeOrder());
            int start = 0, end = 1;
            while (end < destination.length) {
                if (origin.slopeTo(destination[start]) != origin.slopeTo(destination[end])) {
                    if (end - start > 2) {
                        if (checkUnique(destination, origin, start, end)) {
                            insert(origin, destination[end - 1]);
                        }
                    }
                    start = end;
                }
                end += 1;
            }
            if ((end - start > 2) &&
                    (origin.slopeTo(destination[start]) == origin.slopeTo(destination[end - 1]))) {
                if (checkUnique(destination, origin, start, end)) {
                    insert(origin, destination[end - 1]);
                }
            }
        }
        resize(count);
    }
    private boolean checkUnique(Point[] destination, Point origin, int start, int end) {
        ArrayList<Point> collinear = new ArrayList<>();
        collinear.add(origin);
        for (int j = start; j < end; j++) {
            collinear.add(destination[j]);
        }
        Collections.sort(collinear);
        return collinear.get(0).equals(origin);
    }
    /** insert in the answer array. */
    private void insert(Point origin, Point dst) {
        if (count == segment.length) {
            resize(count * 2 + 1);
        }
        segment[count] = new LineSegment(origin, dst);
        count += 1;
    }
    /** resize the answer array. */
    private void resize(int length) {
        LineSegment[] newSegment = new LineSegment[length];
        for (int i = 0; i < segment.length && i < length; i++) {
            newSegment[i] = segment[i];
        }
        segment = newSegment;
    }
    /** the number of line segments.*/
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
        StdDraw.show();
    }
}
