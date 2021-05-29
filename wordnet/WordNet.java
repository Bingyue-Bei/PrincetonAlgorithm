/******************************************************************************
 *  Created by Bingyue Bei,
 *  Date: 01/15/2021
 *****************************************************************************/
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.HashMap;

public class WordNet {
    /** Look up the synset associate with a certain id.*/
    private final HashMap<Integer, String> synsetMap;
    /** Return (the set of) ids associate with a certain noun. */
    private final HashMap<String, Bag<Integer>> idMap;
    /** Shortest Ancestral Search Data Structure. */
    private SAP graph;
    private int size;
    /** Constructor takes the name of the two input files. */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Detect null inputs. ");
        synsetMap = new HashMap<Integer, String>();
        idMap = new HashMap<String, Bag<Integer>>();
        readSynSets(synsets);
        readHypernyms(hypernyms);
    }
    /** returns all WordNet nouns. */
    public Iterable<String> nouns() {
        return idMap.keySet();
    }
    /** is the word a WordNet noun? */
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Detect null inputs. ");
        return idMap.containsKey(word);
    }
    /** distance between nounA and nounB (defined below). */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Input is not a valid noun. ");
        return graph.length(idMap.get(nounA), idMap.get(nounB));
    }
    /** a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
      * in a shortest ancestral path (defined below). */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Input is not a valid noun. ");
        int id = graph.ancestor(idMap.get(nounA), idMap.get(nounB));
        return synsetMap.get(id);
    }
    /** private method which reads in Synsets. */
    private void readSynSets(String fileName) {
        In in = new In(fileName);
        String nextLine;
        while (in.hasNextLine()) {
            nextLine = in.readLine();
            String[] nodes = nextLine.split(",");
            if (nodes.length < 2) continue;
            int id = Integer.parseInt(nodes[0]);
            String synsets = nodes[1];
            synsetMap.put(id, synsets);
            String[] nouns = synsets.split(" ");
            for (String noun: nouns) {
                if (idMap.containsKey(noun)) {
                     idMap.get(noun).add(id);
                } else {
                    Bag<Integer> idSet = new Bag<Integer>();
                    idSet.add(id);
                    idMap.put(noun, idSet);
                }
            }
            size += 1;
        }
    }
    /** private method which reads in Hypernyms. */
    private void readHypernyms(String fileName) {
        In in = new In(fileName);
        Digraph digraph = new Digraph(size);
        String nextLine;
        int notRoot = 0;
        while (in.hasNextLine()) {
            nextLine = in.readLine();
            String[] edges = nextLine.split(",");
            if (edges.length < 2) continue;
            int start = Integer.parseInt(edges[0]);
            for (int i = 1; i < edges.length; i++) {
                digraph.addEdge(start, Integer.parseInt(edges[i]));
            }
            notRoot += 1;
        }
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("Detect cycle in the graph.");
        if (notRoot + 1 != size)
            throw new IllegalArgumentException("Detect wrong number of roots. ");

        graph = new SAP(digraph);
    }
    /** do unit testing of this class. */
    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Invalid testcase. ");
        WordNet testcase = new WordNet(args[0], args[1]);
        final int TESTNUM = 100;
        int counter = 0;
        for (String noun : testcase.nouns()) {
            System.out.println(noun);
            counter += 1;
            if (counter > TESTNUM) break;
        }
    }
}
