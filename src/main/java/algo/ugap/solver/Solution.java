package algo.ugap.solver;

import java.util.ArrayList;
import java.util.Arrays;

import algo.graph.Graph;

public class Solution {

    protected int cyclesCount;
    protected ArrayList<Graph.Edge> resultMatching;
    private boolean isExact = false;

    public void setExact(boolean exact) {
        isExact = exact;
    }

    public boolean isExact() {
        return isExact;
    }


    public Solution() {
        cyclesCount = 0;
        resultMatching = new ArrayList<>();
    }

    public boolean updateIfBeter(Solution solution) {
        boolean isBetter = isBetter(solution);
        if (isBetter) {
            update(solution);
            return true;
        }
        return false;
    }

    public boolean isBetter(Solution solution) {
        return isBetter(solution.getCyclesCount());
    }

    public boolean isBetter(State state) {
        return isBetter(state.cyclesCount);
    }

    public boolean isBetter(int cyclesCount) {
        return this.cyclesCount < cyclesCount;
    }

    public int getCyclesCount() {
        return cyclesCount;
    }

    public ArrayList<Graph.Edge> getResultMatching() {
        return resultMatching;
    }


    public void  update(Solution solution) {
        cyclesCount = solution.getCyclesCount();
        resultMatching = solution.resultMatching;
    }

    public void update(State state) {
        cyclesCount = state.cyclesCount;
        resultMatching = new ArrayList<>(state.resultMatching);
    }

    @Override
    public String toString() {
        String result = String.join("\n", Arrays.asList(
                "cyclesCount: " + cyclesCount,
                "isExact: " + isExact
        ));
        return result;
    }

    public String toStringWithMatchig() {
        return toString() + "\nresultedMatching: " + resultMatching;

    }

    public boolean isFull(int size) {
        return 2*resultMatching.size() == size;
    }

}

