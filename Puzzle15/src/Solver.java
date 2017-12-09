import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private SearchNode answer = null;
    private Board twin;

    public Solver(Board initial) {                //find solution to the initial board
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> solution = new MinPQ<SearchNode>();
        SearchNode current = new SearchNode(initial, 0, null);
        SearchNode currentTwin = new SearchNode(initial.twin(), 0, null);
        twin = currentTwin.board;
        solution.insert(current);
        solution.insert(currentTwin);
        current = solution.delMin();

        while (!current.board.isGoal()) {
            for (Board b : current.board.neighbors()) {
                SearchNode newNode = new SearchNode(b, current.moves + 1, current);
                if (current.pred == null || !newNode.board.equals(current.pred.board)) {
                    solution.insert(newNode);
                }
            }
            current = solution.delMin();
        }
        answer = current;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves, manhat;
        private final SearchNode pred;

        public SearchNode(Board b, int mov, SearchNode pre) {
            board = b;
            moves = mov;
            pred = pre;
            manhat = b.manhattan();
        }

        public int compareTo(SearchNode that) {
            if (this.priority() > that.priority()) return 1;
            else if (this.priority() < that.priority()) return -1;
            else {
                if (this.manhat < that.manhat) return -1;
                else if (this.manhat > that.manhat) return 1;
                else return 0;
            }
        }

        public int priority() {
            return moves + manhat;
        }
    }

    public boolean isSolvable() {// is the initial board solvable?
        return (solution() != null);
    }

    public int moves() {// min number of moves to solve initial board; -1 if unsolvable
        if (!isSolvable()) return -1;
        else return answer.moves;
    }

    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        List<Board> solution = new ArrayList<>();
        SearchNode temp = answer;
        while (temp != null) {
            solution.add(temp.board);
            temp = temp.pred;
        }
        if (solution.isEmpty() || solution.get(solution.size() - 1).equals(twin)) {
            return null;
        } else return solution;

    }

    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}


