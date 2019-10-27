package algo.ugap.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import algo.graph.Graph;


public abstract class State {
    public int cyclesCount = 0;
    public List<Graph.Edge> resultMatching;
    private Optional<Integer> upperBound;
    private Optional<Integer> lowerBound;

    public State() {
        this(0, new ArrayList<>());
    }

    public State(int cyclesCount, List<Graph.Edge> resultMatching) {
        this.cyclesCount = cyclesCount;
        this.resultMatching = resultMatching;
        upperBound = Optional.empty();
        lowerBound = Optional.empty();
    }

    abstract public List<State> computeNextStates(boolean isRestricted);

    public boolean isMissedState() {
        return false;
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

