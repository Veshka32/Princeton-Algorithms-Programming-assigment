import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray text = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            if (text.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < s.length(); i++) {
            int start = text.index(i);
            if (start == 0) start = s.length();
            BinaryStdOut.write(s.charAt(start - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String str = BinaryStdIn.readString();
        //int first=3;
        //String str="ARD!RCAAAABB";
        int[] frequency = new int[256 + 1];
        //key-index counting with offset+1
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            frequency[c + 1] += 1;
        }

        //cummulate counting
        for (int i = 0; i < frequency.length - 1; i++) {
            frequency[i + 1] += frequency[i];
        }

        //build next
        int[] next = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);
            int indexInSortedOrder = frequency[current]++;
            next[indexInSortedOrder] = i;
        }

        //decoding
        int currentIndex = next[first];
        for (int i = 0; i < str.length(); i++) {
            BinaryStdOut.write(str.charAt(currentIndex));
            //System.out.print(str.charAt(currentIndex));
            currentIndex = next[currentIndex];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
