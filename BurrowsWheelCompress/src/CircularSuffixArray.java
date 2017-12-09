import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    // circular suffix array of s
    private final String string;
    private final Integer[] indexI; //index[i] returns number of char in string which is start of suffix on i-th places in sorted suffixes order;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        indexI = new Integer[s.length()];
        string = s;
        for (int i = 0; i < s.length(); i++) {
            indexI[i] = i;
        }
        Arrays.sort(indexI, bySuffix());
    }

    private Comparator<Integer> bySuffix() {
        return new BySuffix();
    }

    private class BySuffix implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            for (int i = 0; i < string.length(); i++) {
                if (a >= string.length()) a -= string.length();
                if (b >= string.length()) b -= string.length();
                char charAtA = string.charAt(a);
                char charAtB = string.charAt(b);
                if (charAtA > charAtB) return +1;
                if (charAtA < charAtB) return -1;
                a++;
                b++;
            }
            return 0;
        }
    }

    public int length() {
        return indexI.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();
        return indexI[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray test=new CircularSuffixArray("kjdflsjfjlj");

    }
}
