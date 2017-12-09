import java.util.HashSet;

public class TrieSET26 {
    private static final int R = 26;        // 26 Uppercase english letter
    private Node root;      // root of trie
    private int n;          // number of keys in trie
    private Node currentPrefix;

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
        private boolean isPrefix = false;
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieSET26() {
    }

    /**
     * Does the set contain the given key?
     *
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 'A'], key, d + 1);
    }

    /**
     * Adds the key to the set if it is not already present.
     *
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
        } else {
            char c = key.charAt(d);
            x.isPrefix = true;
            x.next[c - 'A'] = add(x.next[c - 'A'], key, d + 1);
        }
        return x;
    }

    /**
     * Returns the number of strings in the set.
     *
     * @return the number of strings in the set
     */
    public int size() {
        return n;
    }

    /**
     * Is the set empty?
     *
     * @return {@code true} if the set is empty, and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns true if there is at least one key in the set that starts with {@code prefix}. If prefix is a string - put prefix to goodWords
     *
     * @param prefix the prefix
     * @return true if there is at least one key in the set that starts with {@code prefix}, else false;
     */
    public boolean checkPrefix(String prefix, String prevPrefix, HashSet<String> wordsInboard) {
        Node x;
        if (prefix.equals("QU")) {
            Node y = currentPrefix.next['Q' - 'A'];
            if (y == null) return false;
            x = y.next['U' - 'A'];
        }
        else{
            x = currentPrefix.next[prefix.charAt(0) - 'A'];
        }
        if (x == null) return false;
        if (x.isString) {
            wordsInboard.add(prevPrefix + prefix);
        }
        if (!x.isPrefix) return false;
        return true;
    }

    public boolean updatePrevPrefix(String prevPrefix) {
        currentPrefix = get(root, prevPrefix, 0);
        if (currentPrefix==null || !currentPrefix.isPrefix) return false;
        return true;
    }


}