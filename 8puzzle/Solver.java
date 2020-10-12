/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    /** Private class SearchNode to construct a game tree.*/
    private static class SearchNode implements Comparable<SearchNode> {
        private final SearchNode parentNode;
        private final Board board;
        private final boolean isTwin;
        private int moves = 0;
        private final int distance;
        private final int priority;
        private SearchNode(SearchNode parent, Board tiles, boolean twin) {
            this.parentNode = parent;
            board = tiles;
            isTwin = twin;
            if (parentNode != null) {
                moves = parent.moves + 1;
            }
            distance = board.manhattan();
            priority = distance + moves;
        }
        private Board getBoard() {
            return board;
        }
        private SearchNode getParent() {
            return parentNode;
        }
        private boolean isTwin() {
            return isTwin;
        }
        @Override
        public int compareTo(SearchNode that) {
            if (priority == that.priority) return Integer.compare(distance, that.distance);
            return Integer.compare(priority, that.priority);
        }
    }
    private int moves;
    private boolean solvable;
    private Iterable<Board> solution;
    private final Board initialBoard;
    /** find a solution to the initial board (using the A* algorithm). */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        initialBoard = initial;
        cache();
    }
    /** is the initial board solvable? (see below). */
    public boolean isSolvable() {
        return solvable;
    }

    /** min number of moves to solve initial board; -1 if unsolvable. */
    public int moves() {
        return moves;
    }

    /** sequence of boards in a shortest solution; null if unsolvable. */
    public Iterable<Board> solution() {
        return solution;
    }
    /** run the A* Search Algorithm. */
    private void cache() {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(null, initialBoard, false));
        pq.insert(new SearchNode(null, initialBoard.twin(), true));

        SearchNode currentNode = pq.delMin();
        Board current = currentNode.getBoard();
        while (!current.isGoal()) {
            for (Board successor : current.neighbors()) {
                if (currentNode.getParent() == null
                        || !currentNode.getParent().getBoard().equals(successor)) {
                    pq.insert(new SearchNode(currentNode, successor, currentNode.isTwin()));
                }
            }
            currentNode = pq.delMin();
            current = currentNode.getBoard();
        }
        solvable = !currentNode.isTwin();
        if (!solvable) {
            moves = -1;
            solution = null;
        } else {
            ArrayList<Board> list = new ArrayList<>();
            while (currentNode != null) {
                list.add(currentNode.getBoard());
                currentNode = currentNode.getParent();
            }
            moves = list.size() - 1;
            Collections.reverse(list);
            solution = list;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
