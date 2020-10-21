import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

/******************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Board {
    /** Size of the board. */
    private int size;
    /** number of tiles out of place. */
    private int hamming = 0;
    /** sum of Manhattan distances between tiles and goal. */
    private int manhattan = 0;
    /** Integer representation of the board.*/
    private int [][] board;
    /** create a board from an size-by-size array of tiles,
     * where tiles[row][col] = tile at (row, col). */
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] > 0 && board[i][j] != i * size + j + 1) {
                    hamming += 1;
                    manhattan += (Math.abs((board[i][j] - 1) / size - i) + Math.abs((board[i][j] - 1) % size - j));
                }
            }
        }
    }
    /** string representation of this board. */
    public String toString() {
        String answer = Integer.toString(size);
        for (int i = 0; i < size; i++) {
            answer += "\n";
            for (int j = 0; j < size; j++) {
                answer += " ";
                answer += Integer.toString(board[i][j]);
            }
        }
        return answer;
    }
    /** board dimension size. */
    public int dimension() {
        return size;
    }
    /** number of tiles out of place. */
    public int hamming() {
        return hamming;
    }
    /** sum of Manhattan distances between tiles and goal. */
    public int manhattan() {
        return manhattan;
    }
    /** is this board the goal board? */
    public boolean isGoal() {
        return hamming() == 0;
    }
    /** does this board equal y? */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        return Arrays.deepEquals(board, ((Board) y).board);
    }
    /** all neighboring boards. */
    public Iterable<Board> neighbors() {
        return () -> {
            ArrayList<Board> neighbors = new ArrayList<Board>();
            int row = 0, col = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == 0) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }
            int[][] directions = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];
                if (inBound(newRow, newCol)) {
                    neighbors.add(twin(row, col, newRow, newCol));
                }
            }
            return neighbors.iterator();
        };
    }
    private boolean inBound(int row, int col) {
        return 0 <= row && row < size && 0 <= col && col < size;
    }
    /** a board that is obtained by exchanging a given pair of tiles. */
    private Board twin(int row, int col, int newRol, int newCol) {
        int[][] copyBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copyBoard[i][j] = board[i][j];
            }
        }
        int temp = copyBoard[row][col];
        copyBoard[row][col] = copyBoard[newRol][newCol];
        copyBoard[newRol][newCol] = temp;
        return new Board(copyBoard);
    }

    // a board that is obtained by exchanging any pair of tiles. */
    public Board twin() {
        Board b = null;
        for (int i = 0; i < size * size - 1; i++) {
            int x = i / size;
            int y = i % size;
            int xx = (i + 1) / size;
            int yy = (i + 1) % size;
            if (board[x][y] != 0 && board[xx][yy] != 0) {
                b = twin(x, y, xx, yy);
                break;
            }
        }
        return b;
    }
    /** unit testing (not graded). */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int size = in.readInt();
        int[][] tiles = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                tiles[i][j] = in.readInt();
            Board initial = new Board(tiles);
            System.out.println(initial.toString());
            System.out.println(initial.hamming());
            System.out.println(initial.manhattan());
            for (Board neighbor : initial.neighbors()) {
                System.out.println(neighbor.toString());
            }
        }
    }