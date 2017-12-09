import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // construct an empty deque
    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node {
        Item item;
        Node nextNode;
        Node prevNode;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item==null) throw new IllegalArgumentException();
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            last = first;
        } else {
            first.prevNode = new Node();
            first.prevNode.nextNode = first;
            first.prevNode.item = item;
            first = first.prevNode;

        }
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item==null) throw new IllegalArgumentException();
        if (isEmpty()) {
            last = new Node();
            last.item = item;
            last = first;
        } else {
            last.nextNode = new Node();
            last.nextNode.prevNode = last;
            last.nextNode.item = item;
            last = last.nextNode;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        if (size != 1) {
            first = first.nextNode;
            first.prevNode = null;
        } else {
            first = null;
            last = null;

        }
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        if (size != 1) {
            last = last.prevNode;
            last.nextNode = null;
        } else {
            first = null;
            last = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {

        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.nextNode;
            return item;
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> test = new Deque<>();
        test.addFirst(1);
        test.addFirst(2);
        test.addLast(3);
        test.addLast(4);
        System.out.println(test.removeFirst());
        System.out.println(test.removeLast());
        System.out.println(test.removeFirst());
        System.out.println(test.removeLast());
        System.out.println(test.size());
    }
}