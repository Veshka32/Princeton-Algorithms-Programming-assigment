import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private final ST<String, Integer> teamsNames = new ST<>();
    private final String[] teamsNumbers;
    private final int[] wins, loss, left;
    private final int[][] games;

    private boolean check; //true if is eliminated
    private ArrayList<String> certificate;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int numberOfTeams = in.readInt();
        teamsNumbers = new String[numberOfTeams];
        wins = new int[numberOfTeams];
        loss = new int[numberOfTeams];
        left = new int[numberOfTeams];
        games = new int[numberOfTeams][numberOfTeams];
        for (int current = 0; current < numberOfTeams; current++) {
            String name = in.readString();
            teamsNames.put(name, current);
            teamsNumbers[current] = name;
            wins[current] = in.readInt();
            loss[current] = in.readInt();
            left[current] = in.readInt();
            for (int i = 0; i < numberOfTeams; i++) {
                int g = in.readInt();
                games[current][i] = g;
                games[i][current] = g;
            }
        }
    }

    public int numberOfTeams() {
        return teamsNumbers.length;
    }

    public Iterable<String> teams() {
        return teamsNames.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teamsNames.contains(team)) throw new IllegalArgumentException();
        return wins[teamsNames.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teamsNames.contains(team)) throw new IllegalArgumentException();
        return loss[teamsNames.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamsNames.contains(team)) throw new IllegalArgumentException();
        return left[teamsNames.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!(teamsNames.contains(team1) && teamsNames.contains(team2))) throw new IllegalArgumentException();
        return games[teamsNames.get(team1)][teamsNames.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        maxflow(team);
        return check;
    }

    //subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamsNames.contains(team)) throw new IllegalArgumentException();
        maxflow(team);
        if (certificate.isEmpty()) return null;
        else return certificate;
    }

    private void maxflow(String team) {
        certificate = new ArrayList<>();
        check = false;
        if (!teamsNames.contains(team)) throw new IllegalArgumentException();
        int x = teamsNames.get(team); //index of team of interest
        int xBest = wins[x] + left[x]; //max number of wins for team x
        int size = 2 + numberOfTeams() + ((numberOfTeams()-1) * (numberOfTeams()-1) - (numberOfTeams()-1)) / 2; //size of the graph
        int source = size - 2;
        int target = size - 1;
        int gameNode = numberOfTeams(); //number of current game-node
        FlowNetwork flownetwork = new FlowNetwork(size);
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == x) continue; //ignore team x
            if (xBest < wins[i]) {
                check = true;
                certificate.add(teamsNumbers[i]);
                return;
            }
            flownetwork.addEdge(new FlowEdge(i, target, xBest - wins[i])); //add edge from team-node to target
            for (int j = i + 1; j < numberOfTeams(); j++) {
                if (j == x) continue; //ignore team x
                flownetwork.addEdge(new FlowEdge(source, gameNode, games[i][j])); //add edge from source to game-node
                flownetwork.addEdge(new FlowEdge(gameNode, i, Double.POSITIVE_INFINITY)); //add edge from game-node to team node
                flownetwork.addEdge(new FlowEdge(gameNode, j, Double.POSITIVE_INFINITY));
                gameNode++;
            }
        }
        System.out.println(flownetwork.toString());
        FordFulkerson maxFlow = new FordFulkerson(flownetwork, source, target); //compute max-flow and min-cut
        for (FlowEdge e : flownetwork.adj(source)) {
            if (e.flow() < e.capacity()) {
                check = true; //check if any edge from source is not full
                break;
            }
        }
        if (check) {
            for (int i = 0; i < numberOfTeams(); i++) {
                if (i == x) continue;
                if (maxFlow.inCut(i)) certificate.add(teamsNumbers[i]);
            }
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
