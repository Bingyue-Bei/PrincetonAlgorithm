/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WordTrie {
    /** Each word in the dictionary contains only the uppercase letters A through Z. */
    private static final int R = 26;
    /** Root of the trie. */
    private WordTrie.Node root;
    /** R-way trie node. */
    private class Node {
        private int val = -1;
        private WordTrie.Node[] next = new WordTrie.Node[R];
    }
    private String cache = "";
    private Node cacheNode = new Node();
    /** Initializes an empty string trie. */
    public WordTrie() {
    }
    /** Add a new string key to the trie data structure. */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }
    private WordTrie.Node add(WordTrie.Node x, String key, int d) {
        if (x == null) x = new WordTrie.Node();
        if (d == key.length()) {
            x.val = 0;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], key, d+1);
        }
        return x;
    }
    /** Return the root of the trie. */
    public WordTrie.Node root() {
        return root;
    }
    /** Return true if trie contains certain string word as the key.*/
    public int containWord(String key) {
        if (key == null) throw new IllegalArgumentException("word is null");
        WordTrie.Node x;
        if (cache.length() > 0 && cache.length() < key.length() && cache.equals(key.substring(0, cache.length()))) {
            x = getIterative(cacheNode, key, cache.length());
        } else {
            x = getIterative(root, key, 0);
        }
        if (x == null) return -1;
        if (x.val > -1)
            x.val += 1;
        return x.val;
    }
    /** Return true if trie contains certain prefix. */
    public boolean containPrefix(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("prefix is null");
        WordTrie.Node x;
        if (cache.length() > 0 && cache.length() < prefix.length() && cache.equals(prefix.substring(0, cache.length()))) {
            x = getIterative(cacheNode, prefix, cache.length());
        } else {
            x = getIterative(root, prefix, 0);
        }
        if (x != null) {
            cache = prefix;
            cacheNode = x;
        }
        return x != null;
    }
    /** Iterative method to reach a certain trie node.*/
    private WordTrie.Node getIterative(WordTrie.Node x, String key, int d) {
        WordTrie.Node retVal = x;
        while (d < key.length()) {
            if (retVal == null) return null;
            char c = key.charAt(d);
            retVal = retVal.next[c - 'A'];
            d += 1;
        }
        return retVal;
    }
    /** Unit Testing. */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        WordTrie testcase = new WordTrie();

        for (String word: dictionary) {
            testcase.add(word);
        }
        String[] tests = {"TOPOLOGICALLY", "EQUATION", "EQUATTTT", "EQUAT", "EQUATIONS", "STATE"};

        for (String word: tests) {
            StdOut.println(word);
            StdOut.println("Contain Prefix? " + testcase.containPrefix(word));
        }
    }
}
