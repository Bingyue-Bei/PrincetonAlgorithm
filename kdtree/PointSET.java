/******************************************************************************
 *  Created by Bingyue Bei, 10/21/2020.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    /** Data structure that stores the points. */
    private final SET<Point2D> points;
    /** construct an empty set of points. */
    public PointSET() {
        points = new SET<Point2D>();
    }
    /** is the set empty? */
    public boolean isEmpty() {
        return points.isEmpty();
    }
    /** number of points in the set. */
    public int size() {
        return points.size();
    }
    /** add the point to the set (if it is not already in the set) */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }
    /** does the set contain point p? */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }
    /** draw all points to standard draw. */
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }
    /** all points that are inside the rectangle (or on the boundary). */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        SET<Point2D> contained = new SET<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                contained.add(point);
            }
        }
        return contained;
    }
    /** a nearest neighbor in the set to point p; null if the set is empty. */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D closest = null;
        for (Point2D point : points) {
            if (p.distanceSquaredTo(point) < minDistance) {
                minDistance = p.distanceSquaredTo(point);
                closest = point;
            }
        }
        return closest;
    }
    public static void main(String[] args) {
        //OPTIONAL UNIT TEST
    }
}
