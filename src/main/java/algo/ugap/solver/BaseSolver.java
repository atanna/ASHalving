package algo.ugap.solver;

import algo.ugap.graph.GenomeException;


public abstract class BaseSolver {

    private State firstState;
    public Solution currentSolution;

    protected long startTime = 0;
    protected long finishTime = 0;
    protected long timeLimit = 2 * 60 * 60 * 1000; // 2 hours
    protected boolean isRestricted = false;

    public BaseSolver (State firstState) {
        this.firstState = firstState;
        init();
    }

    public BaseSolver (State firstState, boolean isRestricted) {
        this.firstState = firstState;
        this.isRestricted = isRestricted;
        init();
    }

    private void init() {
        currentSolution = new Solution();
    }

    protected State getFirstState() throws GenomeException {
        return firstState;
    }

    public boolean solveWithLimit(long limit) throws Exception {
        return solveWithLimit(limit, isRestricted);
    }

    public boolean solveWithLimit(long limit, boolean isResticted) throws Exception {
        timeLimit = limit;
        this.isRestricted = isResticted;
        boolean result = solve();
        return result;
    }

    public boolean solve() throws Exception {
        startTime = System.currentTimeMillis();

        boolean result = innerSolve();

        finishTime = System.currentTimeMillis();
        return result;
    }

    boolean isLimitReached() {
        return startTime + timeLimit <  System.currentTimeMillis();
    }

    protected abstract boolean innerSolve() throws GenomeException;

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public long getSolutionTime() {
        return finishTime - startTime;
    }

    public int getCurrentCyclesCount() {
        return currentSolution.getCyclesCount();
    }
}
