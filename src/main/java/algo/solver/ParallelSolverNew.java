package algo.solver;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public abstract class ParallelSolverNew<T> extends BaseSolver<T> {

    private volatile AtomicInteger atomicLowerBound;
    private volatile AtomicInteger atomicUpperBound;

    public ParallelSolverNew(T data) {
        super(data);
        atomicLowerBound = new AtomicInteger(-1);
        atomicUpperBound = new AtomicInteger(-1);
    }

    public boolean solve(int maxIterariton) throws Exception {
        BaseNode root = new Node(getFirstState());
        branchesCounter = new AtomicIntegerArray(getSize());
        new ForkJoinPool().invoke(new MainSolver(root));


        if (isLimitReached()) {
            return false;
        }
        return true;
    }

    public static abstract class BaseNode {
        abstract Collection<BaseNode> getNextStates();

        abstract String getName();

        abstract Solution getSolution();

        abstract void updateIfBetter(Solution solution);
    }

    void updateAtomicBounds(State<T> state) {
        if (state.getLowerBound().isPresent()) {
            int stateLowerBound = state.getLowerBound().get();

            atomicLowerBound.updateAndGet(
                    value -> (value == -1 || value < stateLowerBound) ? stateLowerBound : value);
        }
        if (state.getUpperBound().isPresent()) {
            int stateUpperBound = state.getUpperBound().get();
            atomicUpperBound.updateAndGet(
                    value -> (value == -1 || value < stateUpperBound) ? stateUpperBound : value);
        }
    }

    protected boolean isMissState(State<T> state) {
        if (atomicLowerBound.get() == -1) {
            return false;
        }
        if (state.getUpperBound().isPresent() && state.getUpperBound().get() < atomicLowerBound.get()) {
//            System.out.println(state.resultMatching.size() + ":  " + state.getUpperBound().get() + " " + atomicLowerBound.get());
            return true;
        }
        return false;
    }

    public class Node extends BaseNode {
        State<T> state;
        public Node(State<T> state) {
            this.state = state;
        }

        @Override
        Collection<BaseNode> getNextStates() {
            try {
                updateAtomicBounds(state);
                if (isMissState(state)) {
                    return List.of();
                }
                Collection<BaseNode> nodes = new LinkedList<>();
                for (State<T> s : computeNextStates(state)) {
                    if (isMissState(s)) {
                        continue;
                    }
                    nodes.add(new Node(s));
                }
                if (nodes.size() == 0) {
                    Solution solution = getSolution();
                    if (currentSolution.isBetter(solution)) {

//                        lock.writeLock().lock();
//                        try {
//                            sleep(1);
//                            map.put("foo", "bar");
//                        } finally {
//                            lock.writeLock().unlock();
//                        }
                        currentSolution.updateIfBeter(getSolution());
                    }
                }
                return nodes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return List.of();
        }

        @Override
        String getName() {
            return null;
        }

        @Override
        Solution getSolution() {
            Solution solution = new Solution();
            solution.update(state);
            return solution;
        }

        @Override
        void updateIfBetter(Solution solution) {
            solution.updateIfBeter(currentSolution);
            updateAtomicBounds(state);
        }

    };

    public class MainSolver extends RecursiveAction {
        private final BaseNode node;

        public MainSolver(BaseNode state) {
            this.node = state;
        }

        @Override
        protected void compute() {


            if (isLimitReached()) {
                return;
            }

            for(BaseNode child : node.getNextStates()) {

                MainSolver task = new MainSolver(child);
                task.fork();
            }

        }

    }

}

