/******************************************************************************
 *  Name: Bingyue Bei
 *  Date: 06/14/2021
 *  Description: implement the data structure circular suffix array.
 *****************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class CircularSuffixArray {
    /** length of the input string. */
    private int length;
    private int[] index;
    /** data structure representing the circular suffix arrays that takes O(1) time to construct. */
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int start;
        private int length;
        private char[] suffix;
        private CircularSuffix(int start, int length, char[] suffix) {
            if (start + length > suffix.length) {
                throw new IllegalArgumentException("Starting Index or Length is too large");
            }
            this.start = start;
            this.length = length;
            this.suffix = suffix;
        }
        public int compareTo(CircularSuffix a) {
            for (int i = 0; i < this.length && i < a.length; i++) {
                if (this.suffix[this.start + i] < a.suffix[a.start + i])
                    return -1;
                else if (this.suffix[this.start + i] > a.suffix[a.start + i])
                    return 1;
            }
            if (this.length < a.length)
                return -1;
            else if (this.length > a.length)
                return 1;
            else
                return 0;
        }
        public String toString() {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < length; i++) {
                res.append(suffix[start + i]);
                res.append("\t");
            }
            return String.valueOf(res);
        }
    }
    /** circular suffix array of s. */
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input String is null.");
        }
        length = s.length();
        index = new int[length];
        char[] suffix = new char[2 * length + 1];
        for (int i = 0; i < length; i++) {
            suffix[i] = s.charAt(i);
            suffix[length + i] = s.charAt(i);
        }
        CircularSuffix[] aux = new CircularSuffix[length];
        for (int i = 0; i < length; i++) {
            aux[i] = new CircularSuffix(i, length, suffix);
        }
        Arrays.sort(aux);
        for (int i = 0; i < length; i++) {
            index[i] = aux[i].start;
        }

    }
    /** length of s. */
    public int length() {
        return length;
    }
    /** returns index of ith sorted suffix. */
    public int index(int i) {
        if (i >= length || i < 0)
            throw new IllegalArgumentException("Array index out of bound.");
    return index[i];
    }

    /** unit testing (required). */
    public static void main(String[] args) {
        String test = "ARD!RCAAAABB";
        CircularSuffixArray test1 = new CircularSuffixArray(test);
        for (int i = 0; i < test.length(); i++) {
            StdOut.print(test1.index(i) + "\t");
        }
        StdOut.println();

    }
}
