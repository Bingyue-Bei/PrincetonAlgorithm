/******************************************************************************
 *  Name: Bingyue Bei
 *  Date: 06/06/2021
 *  Description:
 *****************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver {
    private int enrtyNum;
    private final WordTrie dict;
    private HashSet<String> validWords = new HashSet<>();
    /** Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.) */
    public BoggleSolver(String[] dictionary) {
        dict = new WordTrie();
        for (String word: dictionary) {
            if (word.contains("Q") && !word.contains("QU")) {
                continue;
            }
            dict.add(word);
        }
    }
    /** Check if a given row and column is a valid space on the boggle board. */
    private boolean validBoard(int row, int col, BoggleBoard board) {
        return 0 <= row && row < board.rows() && 0 <= col && col < board.cols();
    }
    /** Search for all the words and store them in the instance variable validWord(Type: ArrayList). */
    private void search(int xRow, int xCol, BoggleBoard board, StringBuilder word, boolean[][] visited) {
        visited[xRow][xCol] = true;
        char curLetter = board.getLetter(xRow, xCol);
        word.append(curLetter);
        if (curLetter == 'Q') {
            word.append('U');
        }
        String curr = String.valueOf(word);
        if (dict.containPrefix(curr)) {
            if (curr.length() > 2 && dict.containWord(curr) > 0) {
                validWords.add(curr);
            }
            for (int x = -1; x <= 1; x++) {
                if ((xRow == 0 && x < 0) || (xRow == board.rows() - 1 && x > 0))
                    continue;
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0)
                        continue;
                    int newRow = xRow + x;
                    int newCol = xCol + y;
                    if (validBoard(newRow, newCol, board) && !visited[newRow][newCol]) {
                        search(newRow, newCol, board, new StringBuilder(word), visited);
                    }
                }
            }
        }
        visited[xRow][xCol] = false;
    }
    /** Returns the set of all valid words in the given Boggle board, as an Iterable. */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int row = board.rows();
        int col = board.cols();
        boolean[][] visited = new boolean[row][col];
        validWords = new HashSet<>();
        for (int xRow = 0; xRow < row; xRow++) {
            for (int xCol = 0; xCol < col; xCol++) {
                for (int i = 0; i < row; i++) {
                    Arrays.fill(visited[i], false);
                }
                search(xRow, xCol, board, new StringBuilder(), visited);
            }
        }
        //StdOut.println("Number of valid words: " + validWords.size());
        return validWords;
    }
    /** Returns the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.) */
    public int scoreOf(String word) {
        int length = word.length();
        if (dict.containWord(word) < 0 || length < 3) {
            return 0;
        } else if (length <= 4) {
            return 1;
        } else if (length == 5) {
            return 2;
        } else if (length == 6) {
            return 3;
        } else if (length == 7) {
            return 5;
        } else {
            return 11;
        }
    }
    /** Unit Testing. */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

    }
}
