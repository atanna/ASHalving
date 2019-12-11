package algo.ugap.solver;

import java.util.List;
import java.util.PriorityQueue;

import algo.ugap.graph.GenomeException;


public class SeqSolver extends BaseSolver {

    private PriorityQueue<State> pq;

    protected int lowerBound = -1;
    protected int upperBound = -1;
    private int iterations = 0;
    int firstBruteForce = 0;

    private int initialQueueCapacity = 1000;

    public SeqSolver (State firstState) {
        super(firstState);
        initQueue();
    }

    public SeqSolver (State firstState, boolean isRestricted) {
        super(firstState, isRestricted);
        initQueue();
    }

    public SeqSolver(State firstState, int initialQueueCapacity) {
        this(firstState);
        this.initialQueueCapacity = initialQueueCapacity;
    }

    private void initQueue() {
        pq = new PriorityQueue<State>(initialQueueCapacity, (a, b) -> b.cyclesCount - a.cyclesCount);
        currentSolution = new Solution();
    }

    private State getNextState() {
        return pq.remove();
    }

    protected boolean hasNextState() {
        return pq.size() > 0;
    }

    protected boolean isMissedState(State state) {
        if (state.getUpperBound().isPresent() && state.getUpperBound().get() < lowerBound) {
            return true;
        }
        return false;
    }

    private boolean updateIfBetter(State state) {
        if (currentSolution.isBetter(state)) {
            currentSolution.update(state);
            if (lowerBound < state.cyclesCount) {
                lowerBound = state.cyclesCount;
            }
            return true;
        }
        return false;
    }

    protected void setBoundsToState(State state) {
    }

    private void updateBounds(State state) {
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

    boolean isLimitReached() {
        return startTime + timeLimit <  System.currentTimeMillis();
    }

    @Override
    protected boolean innerSolve() throws GenomeException {
        if (pq.isEmpty()) {
            pq.add(getFirstState());

        }

        while (hasNextState()) {
            if (isLimitReached()) {
                System.out.println("Time limit");
                break;
            }
            iterations++;

            State state = getNextState();

            updateIfBetter(state);

            if (isMissedState(state)) {
                continue;
            }

            List<State> states = state.computeNextStates(isRestricted);
            if (states.size() > 1) {
                int stateSize = state.getSize();
                if (firstBruteForce == 0 || firstBruteForce < stateSize) {
                    firstBruteForce = stateSize;
                    System.out.println("Size = " + stateSize + " first brute force");
                }
            }
            for (State nextState : states) {
                setBoundsToState(nextState);
                updateBounds(nextState);
                updateIfBetter(nextState);
                if (!isMissedState(nextState) && ! nextState.isMissedState()) {
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
