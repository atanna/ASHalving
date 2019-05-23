package algo.solver;

import java.util.ArrayList;

import algo.graph.Graph;

public class State<T> extends BaseSolverState {
    public T data;
    public State(T data, int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        super(cyclesCount, resultMatching);
        this.data = data;
    }
}
