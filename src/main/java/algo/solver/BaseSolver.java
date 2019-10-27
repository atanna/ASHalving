package algo.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Stream;

import algo.graph.GenomeException;
import algo.graph.Graph;


public abstract class BaseSolver<T> {

    private PriorityQueue<State<T>> pq;
    public Solution currentSolution;
    protected Solution.Counts resultCounts;

    protected int lowerBound = -1;
    protected int upperBound = -1;

    protected T data;
    private int initialQueueCapacity = 1000;
    private final int INF = -1;
    protected long startTime = 0;
    protected long finishTime = 0;
    protected long timeLimit = 2 * 60 * 60 * 1000; // 2 hours
    protected boolean isRestricted = false;
    protected AtomicIntegerArray branchesCounter;

    protected abstract int getSize();

    protected abstract int getFullDegree();

    public int getDistance() {
        return getFullDegree()*getSize() / 2 - currentSolution.getCyclesCount();
    }

    public BaseSolver (T data) {
        this.data = data;
        initQueue();
    }

    public BaseSolver (T data, boolean isRestricted) {
        this.data = data;
        this.isRestricted = isRestricted;
        initQueue();
    }

    public BaseSolver(T data, int initialQueueCapacity) {
        this(data);
        this.initialQueueCapacity = initialQueueCapacity;
    }

    private void initQueue() {
        pq = new PriorityQueue<State<T>>(initialQueueCapacity, (a, b) -> b.cyclesCount - a.cyclesCount);
        currentSolution = new Solution();
        resultCounts = new Solution.Counts();
    }

    protected State<T> getFirstState() throws GenomeException {
        return new State<T>(this.data, 0, new ArrayList<>());
    }

    private State<T> getNextState() {
//        System.out.println("Queue size: " + pq.size());
        return pq.remove();
    }

    protected boolean hasNextState() {
        return pq.size() > 0;
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

        boolean result = solve(INF);

        finishTime = System.currentTimeMillis();
//        System.out.println((finishTime +0. - startTime) / 1000/ 60  + " " + "min");
//        System.out.println((finishTime + 0. - startTime) / 1000/ 60 / 60 + " "  + "hours");
        return result;
    }

    public abstract List<State<T>> computeNextStates(State<T> state)
            throws Exception;

    protected boolean isMissState(State<T> state) {
        if (state.getUpperBound().isPresent() && state.getUpperBound().get() < lowerBound) {
//            System.out.println(" " + state.getLowerBound().get() + " " + state.getUpperBound().get() + "  " +
//                    "    " + lowerBound + " " + upperBound
//                    + "           " + pq.size()
//            );
            return true;
        }
        return false;
    }

    private boolean updateIfBetter(State<T> state) {
        if (currentSolution.isBetter(state)) {
            currentSolution.update(state);
            if (lowerBound < state.cyclesCount) {
                lowerBound = state.cyclesCount;
            }
            return true;
        }
        return false;
    }

    protected void setBoundsToState(State<T> state) {
    }

    private void updateBounds(State<T> state) {
        if (state.getLowerBound().isPresent()) {
            int stateLowerBound = state.getLowerBound().get();
            if (lowerBound < stateLowerBound) {
                lowerBound = stateLowerBound;
            }
        }
        if (state.getUpperBound().isPresent()) {
            int stateUpperBound = state.getUpperBound().get();
            if (upperBound < stateUpperBound) {
                upperBound = stateUpperBound;
            }
        }
    }

    boolean isGoodState(State<T> state) {
        return !isMissState(state);
    }

    void cleanMissedStates() {
//        List<State<T>>n("removed count: " + count);
    }

    boolean isLimitReached() {
        if (timeLimit == -1) {
            return false;
        }
        return startTime + timeLimit <  System.currentTimeMillis();
    }

    public boolean solve(int maxIteration) throws Exception {
        startTime = System.currentTimeMillis();
        if (pq.isEmpty()) {
            pq.add(getFirstState());
        }

        while (hasNextState() && (maxIteration == INF || resultCounts.iterations < maxIteration)) {
            if (isLimitReached()) {
                System.out.println("Time limit");
                break;
            }
            resultCounts.iterations++;
            if (resultCounts.iterations % 10000 == 0) {
                cleanMissedStates();
                if (!hasNextState()) {
                    break;
                }
            }

            State<T> state = getNextState();

            updateIfBetter(state);

            if (isMissState(state)) {
                continue;
            }

            List<State<T>> states = computeNextStates(state);
            for (State<T> nextState : states) {
                setBoundsToState(nextState);
                updateBounds(nextState);
                if (!isMissState(nextState)) {
                    pq.add(nextState);
                }
            }
        }
        if (!hasNextState()) {
            currentSolution.setExact(true);
        }

        return !hasNextState();
    }

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
