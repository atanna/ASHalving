package algo.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import algo.graph.Graph;

public class Solution {

    protected int cyclesCount;
    protected ArrayList<Graph.Edge> resultMatching;
    private Counts counts;
    private boolean isExact = false;

    public void setExact(boolean exact) {
        isExact = exact;
    }

    public boolean isExact() {
        return isExact;
    }

    public static class Counts {
        public HashMap<String, Integer> counts;
        public int iterations = 0;
        public int bruteForceBranches = 0;
        public int terminatedBranches = 0;
        public int as1 = 0;
        public int as2 = 0;

        public Counts() {
            counts = new HashMap<>();
        }

        @Override
        public String toString() {
            String result = String.join("\n", Arrays.asList(
                    "iterations: ", String.valueOf(iterations),
                    "counts: ", String.valueOf(counts)
            ));
            return result;
        }
    }

    public Solution() {
        cyclesCount = 0;
        resultMatching = new ArrayList<>();
        counts = new Counts();
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

    public boolean isBetter(BaseSolverState state) {
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

    public void update(BaseSolverState state) {
        cyclesCount = state.cyclesCount;
        resultMatching = new ArrayList<>(state.resultMatching);
    }

    public Counts getCounts() {
        return counts;
    }

    @Override
    public String toString() {
        String result = String.join("\n", Arrays.asList(
                "cyclesCount: " + cyclesCount,
                "distance: " + getDistance(),
                "resultMatching: " + resultMatching
        ));
        return result;
    }

    public int getDistance() {
        return 3*resultMatching.size() - cyclesCount;
    }

}

