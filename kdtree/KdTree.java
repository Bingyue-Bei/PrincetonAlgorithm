/******************************************************************************
 *  Created by Bingyue Bei, 10/21/2020.
 *  Performace optimization: Don't construct a rectangle on every recursive call
 *  during the insertion, only construct when create a nod.
 **************************************************************************** */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {
    private static class Node {
        /** the point */
        private final Point2D p;
        /** the axis-aligned rectangle corresponding to this node */
        private final RectHV rect;
        /** the left/bottom subtree */
        private Node left = null;
        /** the right/top subtree */
        private Node right = null;
        /** private constructor */
        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
    private Node root = null;
    private int size = 0;
    /** construct an empty set of points. */
    public KdTree() {
        return;
    }
    /** is the set empty? */
    public boolean isEmpty() {
        return size == 0;
    }
    /** number of points in the set. */
    public int size() {
        return size;
    }
    /** add the point to the set (if it is not already in the set). */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        RectHV full = new RectHV(0, 0, 1, 1);
        root = insert(root, p, full, true);
    }

    /** Insertion helper method. */
    private Node insert(Node curr, Point2D p, RectHV rect, boolean isVerticle) {
        if (curr == null) {
            size += 1;
            return new Node(p, rect);
        } else if (curr.p.equals(p)) {
            return curr;
        } else if (isVerticle && p.x() < curr.p.x()) {
            if (curr.left == null) {
                size += 1;
                RectHV leftHalf = new RectHV(curr.rect.xmin(), curr.rect.ymin(), curr.p.x(), curr.rect.ymax());
                curr.left = new Node(p, leftHalf);
            } else {
                insert(curr.left, p, curr.left.rect, !isVerticle);
            }
        } else if (isVerticle) {
            if (curr.right == null) {
                size += 1;
                RectHV rightHalf = new RectHV(curr.p.x(), curr.rect.ymin(), curr.rect.xmax(), curr.rect.ymax());
                curr.right = new Node(p, rightHalf);
            } else {
                insert(curr.right, p, curr.right.rect, !isVerticle);
            }
        } else if (!isVerticle && p.y() < curr.p.y()) {
            if (curr.left == null) {
                size += 1;
                RectHV bottomHalf = new RectHV(curr.rect.xmin(), curr.rect.ymin(), curr.rect.xmax(), curr.p.y());
                curr.left = new Node(p, bottomHalf);
            } else {
                insert(curr.left, p, curr.left.rect, !isVerticle);
            }
        } else if (!isVerticle){
            if (curr.right == null) {
                size += 1;
                RectHV topHalf = new RectHV(curr.rect.xmin(), curr.p.y(), curr.rect.xmax(), curr.rect.ymax());
                curr.right = new Node(p, topHalf);
            } else {
                insert(curr.right, p, curr.right.rect, !isVerticle);
            }
        }
        return curr;
    }
    /** Does the set contain point p? */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node curr = root;
        boolean isVerticle = true;
        while (curr != null) {
            if (curr.p.equals(p)) {
                return true;
            } else if (isVerticle && p.x() < curr.p.x()) {
                isVerticle = !isVerticle;
                curr = curr.left;
            } else if (isVerticle) {
                isVerticle = !isVerticle;
                curr = curr.right;
            } else if (!isVerticle && p.y() < curr.p.y()) {
                isVerticle = !isVerticle;
                curr = curr.left;
            } else if (!isVerticle) {
                isVerticle = !isVerticle;
                curr = curr.right;
            }
        }
        return false;
    }
    /** Draw all points to standard draw */
    public void draw() {
        if (root == null) {
            return;
        }
        draw(root, true);
    }
    /** Helper method for drawing. */
    private void draw(Node curr, boolean isVerticle) {
        if (curr == null) return;
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.01);
        curr.p.draw();
        if (isVerticle) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(curr.p.x(), curr.rect.ymin(), curr.p.x(), curr.rect.ymax());
        } else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(curr.rect.xmin(), curr.p.y(), curr.rect.xmax(), curr.p.y());
        }
        draw(curr.left, !isVerticle);
        draw(curr.right, !isVerticle);
    }

    /** All points that are inside the rectangle (or on the boundary). */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> inRange = new ArrayList<Point2D>();
        inRange = findRange(root, rect, inRange);
        return inRange;
    }
    /** Search rectangle helper method. */
    private ArrayList<Point2D> findRange(Node curr, RectHV rect, ArrayList<Point2D> answer) {
        if (curr == null) {
            return answer;
        }
        if (curr.rect.intersects(rect)) {
            if (rect.contains(curr.p)) {
                answer.add(curr.p);
            }
            answer = findRange(curr.left, rect, answer);
            answer = findRange(curr.right, rect, answer);
        }
        return answer;
    }
    /** A nearest neighbor in the set to point p; null if the set is empty. */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) return null;
        return updateNearest(root, p, root.p);
    }
    /** Search Nearest Helper Method. */
    private Point2D updateNearest(Node curr, Point2D p, Point2D nearest) {
        if (curr == null) {
            return nearest;
        }
        double distance = nearest.distanceSquaredTo(p);
        if (distance < curr.rect.distanceSquaredTo(p)) {
            return nearest;
        } else {
            if (curr.p.distanceSquaredTo(p) < distance) {
                nearest = curr.p;
            }
            if (curr.left != null && curr.left.rect.contains(p)) {
                nearest = updateNearest(curr.left, p, nearest);
                nearest = updateNearest(curr.right, p, nearest);
            } else if (curr.right != null && curr.right.rect.contains(p)) {
                nearest = updateNearest(curr.right, p, nearest);
                nearest = updateNearest(curr.left, p, nearest);
            } else {
                double toLeft = curr.left != null ? curr.left.rect.distanceSquaredTo(p) : Double.POSITIVE_INFINITY;
                double toRight = curr.right != null ? curr.right.rect.distanceSquaredTo(p) : Double.POSITIVE_INFINITY;
                if (toLeft < toRight) {
                    nearest = updateNearest(curr.left, p, nearest);
                    nearest = updateNearest(curr.right, p, nearest);
                } else {
                    nearest = updateNearest(curr.right, p, nearest);
                    nearest = updateNearest(curr.left, p, nearest);
                }
            }
            return nearest;
        }
    }
    /** Unit testing of the methods (optional) */
    public static void main(String[] args) {
        KdTree testInsert = new KdTree();
        testInsert.insert(new Point2D(0.7, 0.2));
        testInsert.insert(new Point2D(0.5, 0.4));
        testInsert.insert(new Point2D(0.2, 0.3));
        testInsert.insert(new Point2D(0.4, 0.7));
        testInsert.insert(new Point2D(0.9, 0.6));
        testInsert.insert(new Point2D(0.3, 0.6));
        testInsert.insert(new Point2D(0.3, 0.1));
        System.out.println(testInsert.size());
        testInsert.insert(new Point2D(0.3, 0.1));
        System.out.println(testInsert.size());
        testInsert.insert(new Point2D(0.3, 0.1));
        System.out.println(testInsert.size());
        System.out.println("Test if it contains point " + (new Point2D(0.7, 0.2)).toString());
        System.out.println(testInsert.contains((new Point2D(0.7, 0.2))));
        System.out.println("Test if it contains point " + (new Point2D(0.3, 0.2)).toString());
        System.out.println(testInsert.contains((new Point2D(0.3, 0.2))));
        System.out.println("Test if it contains point " + (new Point2D(0.3, 0.6)).toString());
        System.out.println(testInsert.contains((new Point2D(0.3, 0.6))));
        System.out.println("Test if it contains point " + (new Point2D(0.1, 0.3)).toString());
        System.out.println(testInsert.contains((new Point2D(0.1, 0.3))));
        System.out.println("Test if it contains point " + (new Point2D(0.4, 0.7)).toString());
        System.out.println(testInsert.contains((new Point2D(0.4, 0.7))));

        String filename = args[0];
        In in = new In(filename);
        KdTree testDraw = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            testDraw.insert(p);
        }
        testDraw.draw();
        System.out.println(testDraw.size());
    }
}
