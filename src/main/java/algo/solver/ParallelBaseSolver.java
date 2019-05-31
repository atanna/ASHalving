package algo.solver;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public abstract  class ParallelBaseSolver<T> {
    BlockingQueue<State<T>> queue;

    public class MyWorker extends Thread {
        public void run() {
            try {
                while ( true ) {
                    algo.solver.State<T> state = queue.take();
                    queue.addAll(computeNextStates(state));
                }
            }
            catch ( InterruptedException ie ) {
                // just terminate
            }
        }
    }

    public abstract List<State<T>> computeNextStates(State<T> state);

    public ParallelBaseSolver() {
        int MAX_Q_SIZE = 100;
        queue = new ArrayBlockingQueue<State<T>>(MAX_Q_SIZE);
    }
}
