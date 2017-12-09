import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {          // construct a board from an n-by-n array of blocks(where blocks[i][j] = block in row i, column j)
    private final int[] board;
    private final int dim;
    private int empty = -1; //unknown index of empty cell;

    public Board(int[][] blocks) {
        int[] boardArray = new int[blocks.length * blocks.length];
        int n = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                boardArray[n++] = blocks[i][j];
            }
        }
        board = boardArray;
        dim = blocks.length;
    }

    public int dimension() {               // board dimension n
        return dim;
    }

    public int hamming() {// number of blocks out of place
        int num = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) continue;
            if (board[i] - 1 != i) {
                num += 1;
            }
        }
        return num;
    }

    private void getEmpty() { //find empty cell
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                empty = i;
                break;
            }
        }
    }

    private int manhattanDist(int x, int y) { // x - index of right position, y - given item
        int right = y - 1;
        if (right == x) {
            return 0;
        } else {
            int dist1 = right / dimension() - x / dimension();
            if (dist1 < 0) {
                dist1 = dist1 * (-1);
            }
            int dist2 = right % dimension() - x % dimension();
            if (dist2 < 0) {
                dist2 = dist2 * (-1);
            }
            return dist1 + dist2;
        }
    }

    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) continue;
            if (board[i] - 1 != i) {
                sum += manhattanDist(i, board[i]);
            }
        }
        return sum;
    }

    public boolean isGoal() {                   // is this board the goal board?
        return (hamming() == 0);
    }

    private Board swap(int b, int c) {
        int[] swapped = Arrays.copyOf(board, board.length);
        int swap = swapped[b];
        swapped[b] = swapped[c];
        swapped[c] = swap;

        int[][] swapBoard = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                swapBoard[i][j] = swapped[i * dimension() + j];
            }
        }
        return new Board(swapBoard);
    }

    public Board twin() {                       // a board that is obtained by exchanging any pair of blocks
        if (board[0] != 0) {
            if (board[1] != 0) {
                return swap(0, 1);
            } else return swap(0, 2);
        } else return swap(1, 2);
    }

    public boolean equals(Object y) {// does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        return Arrays.equals(this.board,that.board);
    }

    public Iterable<Board> neighbors() {// all neighboring boards
        Stack<Board> allNeibs = new Stack<Board>();
        Stack<Integer> neiborsIndex = new Stack<Integer>();
        if (empty == -1) {
            getEmpty();
        }
        if (empty % dimension() == 0) {
            neiborsIndex.push(empty + 1);
        } else if (empty % dimension() == dimension() - 1) {
            neiborsIndex.push(empty - 1);
        } else {
            neiborsIndex.push(empty - 1);
            neiborsIndex.push(empty + 1);
        }

        if (empty / dimension() == 0) {
            neiborsIndex.push(empty + dimension());
        } else if (empty / dimension() == dimension() - 1) {
            neiborsIndex.push(empty - dimension());
        } else {
            neiborsIndex.push(empty - dimension());
            neiborsIndex.push(empty + dimension());
        }

        while (!neiborsIndex.isEmpty()) {
            int index = neiborsIndex.pop();
            Board neib = swap(empty, index);
            neib.empty = index;
            allNeibs.push(neib);
        }
        return allNeibs;
    }

    public String toString() {// string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        int n = this.dimension();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.board[i * n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
    }
}