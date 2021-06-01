/******************************************************************************
 *  Name: Bingyue Bei
 *  Date: 06/01/2021
 *  Description: Weekly Project #3: Baseball Eliminator.
 *****************************************************************************/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int teamNum;
    private final HashMap<String, Integer> teamNames;
    private final int[] wins;
    private final int[] losses;
    private final int[] remainings;
    private final int[][] games;
    private boolean isEliminated;
    private Bag<String> eliminators;
    /** create a baseball division from given filename in format specified below. */
    public BaseballElimination(String filename) {
        In input = new In(filename);
        if (input.isEmpty())
            throw new IllegalArgumentException("The input file is empty");
        teamNum = input.readInt();
        teamNames = new HashMap<>();
        wins = new int[teamNum];
        losses = new int[teamNum];
        remainings = new int[teamNum];
        games = new int[teamNum][teamNum];
        for (int i = 0; i < teamNum; i++) {
            if (!input.hasNextLine())
                throw new IllegalArgumentException("File too short. ");
            String teamName = input.readString();
            teamNames.put(teamName, i);
            wins[i] = input.readInt();
            losses[i] = input.readInt();
            remainings[i] = input.readInt();
            for (int j = 0; j < teamNum; j++)
                games[i][j] = input.readInt();
        }
        for (int i = 0; i < teamNum; i++) {
            for (int j = i; j < teamNum; j++) {
                if (i >= j && games[i][j] != games[j][i])
                    throw new IllegalArgumentException(
                            "Number of remaining games between two teams should be equal despite orders.");
                    if (i == j && games[i][j] != 0)
                    throw new IllegalArgumentException(
                            "Number of remaining games between a team and itself should be 0.");
            }
        }
    }
    /** number of teams. */
    public int numberOfTeams() {
        return teamNum;
    }
    /** all teams. */
    public Iterable<String> teams() {
        return teamNames.keySet();
    }
    /** check if a given team name string is valid. */
    private void checkValidGame(String team) {
        if (team == null)
            throw new IllegalArgumentException("Input String cannot be null;");
        if (!teamNames.containsKey(team))
            throw new IllegalArgumentException("Input team name not found");
    }
    /** number of wins for given team. */
    public int wins(String team) {
        checkValidGame(team);
        return wins[teamNames.get(team)];
    }
    /** number of losses for given team. */
    public int losses(String team) {
        checkValidGame(team);
        return losses[teamNames.get(team)];
    }
    /** number of remaining games for given team. */
    public int remaining(String team) {
        checkValidGame(team);
        return remainings[teamNames.get(team)];
    }
    /** number of remaining games between team1 and team2. */
    public int against(String team1, String team2) {
        checkValidGame(team1);
        checkValidGame(team2);
        return games[teamNames.get(team1)][teamNames.get(team2)];
    }
    /** construct a flow network of games and teams, use Ford-Fulkerson algorithm to calculate the max flow.*/
    private void maxFlowSolver(String team) {
        int flow = 0;
        isEliminated = false;
        eliminators = new Bag<>();
        checkValidGame(team);
        int teamIndex = teamNames.get(team);
        int capacity = wins[teamIndex] + remainings[teamIndex];
        for (String teamName: teamNames.keySet()) {
            if (wins(teamName) > capacity) {
                isEliminated = true;
                eliminators.add(teamName);
                return;
            }
        }
        int gameNum = teamNum * (teamNum - 1) / 2;
        int vertexNum = teamNum + gameNum + 2;
        FlowNetwork gameNet = new FlowNetwork(vertexNum);
        int gameIndex = 0;
        for (int i = 0; i < teamNum; i++) {
            if (i == teamIndex)
                continue;
            for (int j = i + 1; j < teamNum; j++) {
                if (j == teamIndex)
                    continue;
                int gameLeft = games[i][j];
                gameIndex += 1;
                FlowEdge sStart = new FlowEdge(0, gameIndex, gameLeft);
                FlowEdge gameToTeam1 = new FlowEdge(gameIndex, gameNum + i + 1, Double.POSITIVE_INFINITY);
                FlowEdge gameToTeam2 = new FlowEdge(gameIndex, gameNum + j + 1, Double.POSITIVE_INFINITY);
                gameNet.addEdge(sStart);
                gameNet.addEdge(gameToTeam1);
                gameNet.addEdge(gameToTeam2);
                flow += gameLeft;
            }
            FlowEdge tEnd = new FlowEdge(gameNum + i + 1, vertexNum - 1, capacity - wins[i]);
            gameNet.addEdge(tEnd);
        }
        FordFulkerson solver = new FordFulkerson(gameNet, 0, vertexNum - 1);
        isEliminated = flow > solver.value();
        for (String teamName: teamNames.keySet()) {
            if (teamName.equals(team))
                continue;
            int index = teamNames.get(teamName);
            if (solver.inCut(index + gameNum + 1)) {
                eliminators.add(teamName);
            }
        }
    }
    /** is given team eliminated?. */
    public boolean isEliminated(String team) {
        maxFlowSolver(team);
        return isEliminated;
    }
    /** subset R of teams that eliminates given team; null if not eliminated. */
    public Iterable<String> certificateOfElimination(String team) {
        maxFlowSolver(team);
        if (!isEliminated)
            return null;
        return eliminators;
    }
    /** unit testing client. */
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
