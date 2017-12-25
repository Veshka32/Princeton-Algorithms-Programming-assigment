import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {
        return (n == 0);
    }

    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity >= n;
        a = java.util.Arrays.copyOf(a, capacity);
    }

    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (n == a.length) resize(2 * a.length);    // double size of array if necessary
        a[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, n);
        Item item = a[index];
        a[index] = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, n);
        return a[index];
    }

    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        int i = 0;
        int[] indexes = new int[n];

        RandomIterator() {
            for (int j = 0; j < n; j++)
                indexes[j] = j;
            StdRandom.shuffle(indexes);
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int index = indexes[i++];
            return a[index];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}