import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver2 {
    private SearchNode answer = null;

    public Solver2(Board initial) {                //find solution to the initial board

        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> solution = new MinPQ<SearchNode>();
        MinPQ<SearchNode> solutionTwin = new MinPQ<SearchNode>();
        SearchNode current = new SearchNode(initial, 0, null);
        SearchNode currentTwin = new SearchNode(initial.twin(), 0, null);
        solution.insert(current);
        solutionTwin.insert(currentTwin);
        current = solution.delMin();
        currentTwin = solutionTwin.delMin();

        while (!(current.board.isGoal() || currentTwin.board.isGoal())) {
            for (Board b : current.board.neighbors()) {
                SearchNode newNode = new SearchNode(b, current.moves + 1, current);
                if (current.pred==null||!newNode.board.equals(current.pred.board)) {
                    solution.insert(newNode);
                }
            }
            for (Board b : currentTwin.board.neighbors()) {
                SearchNode newNode = new SearchNode(b, currentTwin.moves + 1, currentTwin);
                if (currentTwin.pred==null || !newNode.board.equals(currentTwin.pred.board)) {
                    solutionTwin.insert(newNode);
                }
            }

            current = solution.delMin();
            currentTwin = solutionTwin.delMin();
        }
        if (current.board.isGoal()) {
            answer = current;
        } else {
            answer = null;
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode pred;
        private final int manhat;

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
                if (this.manhat<that.manhat) return -1;
                else if (this.manhat>that.manhat) return 1;
                else return 0;
            }
        }

        public int priority() {
            return moves + manhat;
        }
    }

    public boolean isSolvable() {// is the initial board solvable?
        return (answer != null);
    }

    public int moves() {// min number of moves to solve initial board; -1 if unsolvable
        if (!isSolvable()) return -1;
        else return answer.moves;
    }

    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) return null;
        Stack<Board> solution = new Stack<Board>();
        SearchNode temp = answer;
        while (temp != null) {
            solution.push(temp.board);
            temp = temp.pred;
        }
        return solution;

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
        Solver2 solver = new Solver2(initial);

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


