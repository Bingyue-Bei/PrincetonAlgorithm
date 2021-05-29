/******************************************************************************
 *  Created by Bingyue Bei,
 *  Date: 01/15/2021
 *****************************************************************************/
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    /** Class Attributes. */
    private final Digraph graph;
    /** Cache for the SAP, represent length, ancestor. */
    private Object vv = -1;
    private Object ww = -1;
    private int[] cache = {-1, -1};
    /** constructor takes a digraph (not necessarily a DAG) */
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        graph = new Digraph(G);
    }
    /** Test if a vertex is valid in the given edges. */
    private boolean validVertex(int v) {
        return 0 <= v && v < graph.V();
    }
    /** Test if a set of vertices is valid in the given edges. */
    private boolean validateVertices(Iterable<Integer> vs) {
        if (vs == null)
            throw new IllegalArgumentException();
        for (Integer v: vs) {
            if (v == null || 0 > v || v >= graph.V()) return false;
        }
        return true;
    }
    /** length of shortest ancestral path between v and w; -1 if no such path. */
    public int length(int v, int w) {
        if (!validVertex(v) || !validVertex(w))
            throw new IllegalArgumentException();
        if (vv.equals(v) && ww.equals(w))
            return cache[0];
        else {
            vv = v;
            ww = w;
            search(v, w);
            return cache[0];
        }
    }
    /** a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path. */
    public int ancestor(int v, int w) {
        if (!validVertex(v) || !validVertex(w))
            throw new IllegalArgumentException();
        if (vv.equals(v) && ww.equals(w))
            return cache[1];
        else {
            vv = v;
            ww = w;
            search(v, w);
            return cache[1];
        }
    }
    /** Helper function that updates the cache for shortest ancestor and length. */
    private void search(int v, int w) {
        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int curr = pathV.distTo(i) + pathW.distTo(i);
                if (curr < length) {
                    length = curr;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) {
            cache[0] = -1;
            cache[1] = -1;
        } else {
            cache[0] = length;
            cache[1] = ancestor;
        }
    }
    /** length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path. */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || !validateVertices(v) || !validateVertices(w))
            throw new IllegalArgumentException();
        if (vv == v && ww == w)
            return cache[0];
        else {
            vv = v;
            ww = w;
            search(v, w);
            return cache[0];
        }
    }
    /** a common ancestor that participates in shortest ancestral path; -1 if no such path. */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || !validateVertices(v) || !validateVertices(w))
            throw new IllegalArgumentException();
        if (vv == v && ww == w)
            return cache[1];
        else {
            vv = v;
            ww = w;
            search(v, w);
            return cache[1];
        }
    }
    /** Helper function that updates the cache for shortest ancestor and length. */
    private void search(Iterable<Integer> v, Iterable<Integer> w) {
        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            cache[0] = -1;
            cache[1] = -1;
            return;
        }
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int curr = pathV.distTo(i) + pathW.distTo(i);
                if (curr < length) {
                    length = curr;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) {
            cache[0] = -1;
            cache[1] = -1;
           } else {
            cache[0] = length;
            cache[1] = ancestor;
        }
    }
    /** Unit testing for this class. */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
