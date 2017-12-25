import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> test = new RandomizedQueue<>();
        int count=0;
        double threshold;
        double prob;
        while (!StdIn.isEmpty()) {
            String s=StdIn.readString();
            count++;
            //queue size never become >k;
            if (k>0 && test.size() == k ) {
                prob = StdRandom.uniform();
                threshold=(double)k/(count);
                if (prob < threshold) {
                    test.dequeue();
                    test.enqueue(s);
                }
            } else test.enqueue(s);
        }

            Iterator<String> iterator = test.iterator();

        while (k > 0) {
            StdOut.println(iterator.next());
            k--;
        }

}}
