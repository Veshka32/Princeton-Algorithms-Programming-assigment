import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // constructor takes a WordNet object
    private final WordNet out;

    public Outcast(WordNet wordnet) {
        out = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        ST<Integer, String> distances = new ST<Integer, String>();
        for (String s : nouns) {
            distances.put(distToNouns(nouns, s), s);
        }
        int max = distances.max();
        return distances.get(max);
    }

    private int distToNouns(String[] nouns, String noun) {
        int sumDistances = 0;
        for (int i = 0; i < nouns.length; i++) {
            int dist = out.distance(noun, nouns[i]);
            sumDistances += dist;
        }
        return sumDistances;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
