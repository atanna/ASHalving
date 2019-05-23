package algo.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import algo.graph.GenomeException;


public abstract class BaseSolver<T> {

    private PriorityQueue<State<T>> pq;
    public Solution currentSolution;
    protected Solution.Counts resultCounts;

    protected T data;
    private int initialQueueCapacity = 1000;
    private final int INF = -1;

    public BaseSolver (T data) {
        this.data = data;
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
        return pq.remove();
    }

    protected boolean hasNextState() {
        return pq.size() > 0;
    }

    public boolean solve() throws Exception {
        return solve(INF);
    }

    public abstract List<State<T>> computeNextStates(State<T> state)
            throws Exception;

    private boolean isMissState(State<T> state) {
        return false;
    }

    private boolean updateIfBetter(State<T> state) {
        if (currentSolution.isBetter(state)) {
            currentSolution.update(state);
            return true;
        }
        return false;
    }

    public boolean solve(int maxIteration) throws Exception {
        if (pq.isEmpty()) {
            pq.add(getFirstState());
        }

        while (hasNextState() && (maxIteration == INF || resultCounts.iterations < maxIteration)) {
            resultCounts.iterations++;
            State<T> state = getNextState();

            updateIfBetter(state);

            if (isMissState(state)) {
                continue;
            }

            List<State<T>> states = computeNextStates(state);
            pq.addAll(states);
        }

        return !hasNextState();
    }

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public int getCurrentCyclesCount() {
        return currentSolution.getCyclesCount();
    }

}
