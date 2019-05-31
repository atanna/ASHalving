package algo.solver;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ParallelSolver<T> extends BaseSolver<T> {

    private volatile AtomicInteger atomicLowerBound;
    private volatile AtomicInteger atomicUpperBound;

    public ParallelSolver(T data) {
        super(data);
        atomicLowerBound = new AtomicInteger(-1);
        atomicUpperBound = new AtomicInteger(-1);
    }

    public boolean solve(int maxIterariton) throws Exception {
        BaseNode root = new Node(getFirstState());
        currentSolution = new ForkJoinPool().invoke(new MainSolver(root));
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

    public class MainSolver extends RecursiveTask<Solution> {
        private final BaseNode node;

        public MainSolver(BaseNode state) {
            this.node = state;
        }

        @Override
        protected Solution compute() {
//            System.out.println("compute " + node.getName());


            Solution bestSolution = node.getSolution();
            List<MainSolver> subTasks = new LinkedList<>();

            if (isLimitReached()) {
                System.out.println(startTime);
                System.out.println(timeLimit);
                System.out.println(startTime + timeLimit);
                System.out.println(System.currentTimeMillis());
                System.out.println("Time limit");
                return bestSolution;
            }

            for(BaseNode child : node.getNextStates()) {
                MainSolver task = new MainSolver(child);
                task.fork(); // запустим асинхронно
                subTasks.add(task);
            }
            node.updateIfBetter(bestSolution);

            for(MainSolver task : subTasks) {
                Solution solution = task.join(); // дождёмся выполнения задачи и прибавим результат
                bestSolution.updateIfBeter(solution);
            }
            return bestSolution;
        }

    }

}

