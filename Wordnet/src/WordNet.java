import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

import java.util.HashMap;

public class WordNet {
    // constructor takes the name of the two input files
    private final HashMap<Integer, String> synsetTable = new HashMap<>();
    private Digraph wordDigraph;
    private final ST<String, SET<Integer>> allNouns = new ST<String, SET<Integer>>();
    private SAP oneSap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        readSynsets(synsets);
        constructDigraph(hypernyms);
    }

    private void readSynsets(String synsets) {
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");

            synsetTable.put(Integer.valueOf((line[0])), line[1]);
            String[] nouns = line[1].split(" ");
            for (String s : nouns) {
                if (!allNouns.contains(s)) allNouns.put(s, new SET<Integer>());
                allNouns.get(s).add(Integer.valueOf(line[0]));
            }
        }
    }

    private void constructDigraph(String hypernyms) {
        wordDigraph = new Digraph(synsetTable.size());
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            for (int i = 1; i < line.length; i++) {
                wordDigraph.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
            }
        }
        // detect cycles;
        DirectedCycle a = new DirectedCycle(wordDigraph);
        if (a.hasCycle()) throw new IllegalArgumentException();

        //detect rooted or not
        int numRoots = 0;
        for (int i = 0; i < wordDigraph.V(); i++) {
            if (wordDigraph.outdegree(i) == 0) numRoots += 1;
            if (numRoots > 1) throw new IllegalArgumentException();
        }
        if (numRoots == 0) throw new IllegalArgumentException();
        oneSap = new SAP(wordDigraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return allNouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return allNouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB))) throw new IllegalArgumentException();
        SET<Integer> synsetsA = allNouns.get(nounA);
        SET<Integer> synsetsB = allNouns.get(nounB);
        return oneSap.length(synsetsA, synsetsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB))) throw new IllegalArgumentException();
        SET<Integer> synsetsA = allNouns.get(nounA);
        SET<Integer> synsetsB = allNouns.get(nounB);
        int anc = oneSap.ancestor(synsetsA, synsetsB);
        return String.valueOf(synsetTable.get(anc));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        System.out.println("sap for bird  and worm -> " + wordnet.sap("bird", "worm"));
        System.out.println("distance between bird  and worm ->" + wordnet.distance("bird", "worm"));

        System.out.println("sap for municipality  and region -> " + wordnet.sap("municipality", "region"));
        System.out.println("distance between municipality  and region ->" + wordnet.distance("municipality", "region"));
        System.out.println("sap for individual  and edible_fruit  ->" + wordnet.sap("individual", "edible_fruit"));
        System.out.println("dist for individual  and edible_fruit  ->" + wordnet.distance("individual", "edible_fruit"));

        System.out.println("distance between white_marlin and mileage" + wordnet.distance("mileage", "white_marlin"));
        System.out.println("distance between Black_Plague and black_marlin" + wordnet.distance("Black_Plague", "black_marlin"));
        System.out.println("distance between American_water_spaniel and histology" + wordnet.distance("American_water_spaniel", "histology"));
        System.out.println("distance between Brown_Swiss and barrel_roll" + wordnet.distance("Brown_Swiss", "barrel_roll"));
    }
}
