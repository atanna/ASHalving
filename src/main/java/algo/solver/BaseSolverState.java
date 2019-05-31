package algo.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import algo.graph.Graph;


public class BaseSolverState {
    public int cyclesCount = 0;
    public List<Graph.Edge> resultMatching;
    private Optional<Integer> upperBound;
    private Optional<Integer> lowerBound;

    public BaseSolverState(int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        this.cyclesCount = cyclesCount;
        this.resultMatching = resultMatching;
        upperBound = Optional.empty();
        lowerBound = Optional.empty();
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = Optional.of(lowerBound);
    }

    public Optional<Integer> getLowerBound() {
        return lowerBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = Optional.of(upperBound);
    }

    public Optional<Integer> getUpperBound() {
        return upperBound;
    }
}
