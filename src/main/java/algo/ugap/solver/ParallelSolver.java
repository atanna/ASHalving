package algo.ugap.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

import algo.ugap.graph.GenomeException;


public class ParallelSolver extends BaseSolver {

    private volatile AtomicInteger atomicLowerBound;
    private volatile AtomicInteger atomicUpperBound;

    public ParallelSolver(State firstState) {
        super(firstState);
        atomicLowerBound = new AtomicInteger(-1);
        atomicUpperBound = new AtomicInteger(-1);
        currentSolution = new Solution();
    }

    @Override
    protected boolean innerSolve() {
        try {
            MainSolver solver = new MainSolver(getFirstState());
            currentSolution = new ForkJoinPool().invoke(solver);
        } catch (GenomeException e) {
            e.printStackTrace();
        }

        return true;
    }

    void updateAtomicBounds(State state) {
        if (state.getLowerBound().isPresent()) {
            int stateLowerBound = state.getLowerBound().get();
            updateAtomicLowerBound(stateLowerBound);
        }
        if (state.getUpperBound().isPresent()) {
            int stateUpperBound = state.getUpperBound().get();
            updateAtomicUpperBound(stateUpperBound);
        }
    }

    void updateAtomicLowerBound(int lowerBound) {
        atomicLowerBound.updateAndGet(
                value -> (value == -1 || value < lowerBound) ? lowerBound : value);
    }

    void updateAtomicUpperBound(int uppserBound) {
        atomicUpperBound.updateAndGet(
                value -> (value == -1 || value < uppserBound) ? uppserBound : value);
    }

    protected boolean isMissState(State state) {
        if (state.isMissedState()) {
            return true;
        }
        if (atomicLowerBound.get() == -1) {
            return false;
        }
        if (state.getUpperBound().isPresent() && state.getUpperBound().get() < atomicLowerBound.get()) {
            return true;
        }
        return false;
    }

    public class MainSolver extends RecursiveTask<Solution> {
        private final State state;

        public MainSolver(State state) {
            this.state = state;
        }

        @Override
        protected Solution compute() {
            boolean exact = true;

            Solution bestSolution = new Solution();
            bestSolution.update(state);
            List<MainSolver> subTasks = new LinkedList<>();

            if (isLimitReached() || isMissState(state)) {
                return bestSolution;
            }

            for (State newState : state.computeNextStates(isRestricted)) {
                updateAtomicBounds(newState);
                if (isMissState(newState)) {
                    continue;
                }
                MainSolver task = new MainSolver(newState);
                task.fork();
                subTasks.add(task);
            }

            for (MainSolver task : subTasks) {
                Solution solution = task.join();
                if (!solution.isExact()) {
                    exact = false;
                }
                bestSolution.updateIfBeter(solution);
                updateAtomicLowerBound(solution.getCyclesCount());
            }

            bestSolution.setExact(exact);
            return bestSolution;
        }
    }

}
