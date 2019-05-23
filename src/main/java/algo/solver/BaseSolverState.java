package algo.solver;

import java.util.ArrayList;
import java.util.List;

import algo.graph.Graph;


public class BaseSolverState {
    public int cyclesCount = 0;
    public List<Graph.Edge> resultMatching;

    public BaseSolverState(int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        this.cyclesCount = cyclesCount;
        this.resultMatching = resultMatching;
    }
}
