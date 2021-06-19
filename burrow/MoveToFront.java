/******************************************************************************
 *  Name: Bingyue Bei
 *  Date: 06/14/2021
 *  Description:
 *****************************************************************************/


import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int ASCII_SIZE = 256;
    private static final int OUTPUT_SIZE = 8;
    /** apply move-to-front encoding, reading from standard input and writing to standard output. */
    public static void encode() {
        LinkedList<Integer> ascii = new LinkedList<>();
        for (int i = 0; i < ASCII_SIZE; i++) {
            ascii.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int curr = BinaryStdIn.readChar();
            int index = ascii.indexOf(curr);
            BinaryStdOut.write(index, OUTPUT_SIZE);
            ascii.remove(index);
            ascii.addFirst(curr);
        }
        BinaryStdOut.flush();
    }
    /** apply move-to-front decoding, reading from standard input and writing to standard output. */
    public static void decode() {
        LinkedList<Character> ascii = new LinkedList<>();
        for (int i = 0; i < ASCII_SIZE; i++) {
            ascii.add((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(OUTPUT_SIZE);
            char curr = ascii.get(index);
            BinaryStdOut.write(curr);
            ascii.remove(index);
            ascii.addFirst(curr);
        }
        BinaryStdOut.flush();
    }

    /** if args[0] is "-", apply move-to-front encoding.
     * if args[0] is "+", apply move-to-front decoding. */
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
