import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.HashSet;

public class BoggleSolver {
    private final TrieSET26 dictionary = new TrieSET26();

    // Initializes the data structure using the given array of strings as the dictionary.
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            this.dictionary.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> wordsInBoard = new HashSet<>();
        HashMap<Integer, String> boardMap = new HashMap<>();
        int size = board.rows() * board.cols();
        if (size==1) return wordsInBoard;
        char letter;
        for (int i = 0; i < size; i++) {
            letter = board.getLetter(i / board.cols(), i % board.cols());
            if (letter == 'Q') boardMap.put(i, "QU");
            else boardMap.put(i, letter + "");
        }

        Queue<int[]> numbers = new Queue<>();
        Queue<String> words = new Queue<>();
        for (int i = 0; i < size; i++) {
            numbers.enqueue(new int[]{i});
            words.enqueue(boardMap.get(i));
            while (!numbers.isEmpty()) {
                int[] lastNumber = numbers.dequeue();
                String lastWord = words.dequeue();
                if (!dictionary.updatePrevPrefix(lastWord)) continue;
                for (int next : getNeibours(lastNumber[lastNumber.length - 1], board)) {
                    if (contains(lastNumber, next)) continue;
                    String nextChar=boardMap.get(next);
                    if (lastWord.length() > 1 && !dictionary.checkPrefix(nextChar,lastWord,wordsInBoard)) continue;
                    words.enqueue(lastWord+nextChar);
                    int[] newNumbers = new int[lastNumber.length + 1];
                    System.arraycopy(lastNumber, 0, newNumbers, 0, lastNumber.length);
                    newNumbers[lastNumber.length] = next;
                    numbers.enqueue(newNumbers);
                }
            }
        }
        return wordsInBoard;
    }

    private boolean contains(int[] array, int n) {
        for (int i : array) {
            if (i == n) return true;
        }
        return false;
    }

    public int scoreOf(String word) {
        if (!dictionary.contains(word) || word.length() <= 2) return 0;
        if (word.length() == 3 || word.length() == 4) return 1;
        if (word.length() == 5) return 2;
        if (word.length() == 6) return 3;
        if (word.length() == 7) return 5;
        else return 11;
    }

    private int[] getNeibours(int current, BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();
        if (rows == 1) {
            if (current == 0) return new int[]{1};
            if (current == cols - 1) return new int[]{current - 1};
            else return new int[]{current + 1, current - 1};
        }
        if (cols == 1) {
            if (current == 0) return new int[]{1};
            if (current == rows - 1) return new int[]{current - 1};
            else return new int[]{current + 1, current - 1};
        }

        if (current == 0) return new int[]{current + 1, current + cols, current + cols + 1};
        if (current < cols - 1)
            return new int[]{current - 1, current + 1, current + cols, current + cols + 1, current + cols - 1};
        if (current == cols - 1)
            return new int[]{current - 1, current + cols, current + cols - 1};
        if (current % cols == 0 && current / cols > 0 && current / cols < rows - 1)
            return new int[]{current - cols, current + cols, current + 1, current + cols + 1, current - cols + 1};
        if (current % cols == cols - 1 && current / cols > 0 && current / cols < rows - 1)
            return new int[]{current - cols, current + cols, current - 1, current - cols - 1, current + cols - 1};
        if (current == cols * (rows - 1))
            return new int[]{current + 1, current - cols, current - cols + 1};
        if (current > cols * (board.rows() - 1) && current < cols * board.rows() - 1)
            return new int[]{current - 1, current + 1, current - cols, current - cols - 1, current - cols + 1};
        if (current == cols * rows - 1)
            return new int[]{current - 1, current - cols, current - cols - 1};
        if (current >= rows * cols) throw new IllegalArgumentException();
        else
            return new int[]{current + 1, current - 1, current + cols, current - cols, current - cols - 1, current + cols + 1, current - cols + 1, current + cols - 1};
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
//        BoggleBoard board = new BoggleBoard(args[1]);
//        int score = 0;
//        for (String word : solver.getAllValidWords(board)) {
//            score += solver.scoreOf(word);
//        }
//        StdOut.println("Score = " + score);

        int n = 30000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            BoggleBoard board = new BoggleBoard();
            solver.getAllValidWords(board);
        }
        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - startTime) / 1000;
        System.out.println("total time= " + totalTime + ", oper per sec= " + n / totalTime);
    }
}
