/******************************************************************************
 *  Name: Bingyue Bei
 *  Date: 06/18/2021
 *  Description: Apply the Burrows-Wheeler transformation.
 *****************************************************************************/
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int OUTPUT_SIZE = 8;
    /** apply Burrows-Wheeler transform,
      * reading from standard input and writing to standard output. */
    public static void transform() {
        StringBuilder aux = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            aux.append(BinaryStdIn.readChar());
        }
        String input = String.valueOf(aux);
        CircularSuffixArray circular = new CircularSuffixArray(input);
        for (int i = 0; i < circular.length(); i++) {
            if (circular.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < circular.length(); i++) {
            int index = (circular.index(i) + circular.length() - 1) % circular.length();
            BinaryStdOut.write(input.charAt(index), OUTPUT_SIZE);
        }
        BinaryStdOut.flush();
    }

    /** apply Burrows-Wheeler inverse transform,
      * reading from standard input and writing to standard output. */
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        StringBuilder t = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            t.append(BinaryStdIn.readChar());
        }
        int length = t.length();
        final int EXTENDED_ASCII_INDEX_MAX = 256;
        int[] count = new int[EXTENDED_ASCII_INDEX_MAX + 1];
        for (int i = 0; i < length; i++) {
            count[t.charAt(i) + 1]++;
        }
        for (int i = 0; i < EXTENDED_ASCII_INDEX_MAX; i++) {
            count[i + 1] += count[i];
        }
        int[] next = new int[length];
        for (int i = 0; i < length; i++) {
            char c = t.charAt(i);
            next[count[c]] = i;
            count[c]++;
        }
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(t.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOut.flush();
    }

    /** if args[0] is "-", apply Burrows-Wheeler transform.
      * if args[0] is "+", apply Burrows-Wheeler inverse transform. */
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
