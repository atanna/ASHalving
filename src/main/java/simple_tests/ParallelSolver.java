package simple_tests;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;




public class ParallelSolver {
    public class State {
        String value;
        State(String value) {
            this.value = value;
        }
    }

    public class MyWorker extends Thread {
        private int id;

        private BlockingQueue<ParallelSolver.State> queue;
        public MyWorker(int id, BlockingQueue<ParallelSolver.State> queue) {
            super();
            this.queue = queue;
            this.id = id;
        }
        public void run() {
            try {
//                while ( true ) {
                    ParallelSolver.State state = queue.take();
                    System.out.println("Worker " + id + "  take " + state.value + " queue size = " + queue.size());
                    sleep(1000);
                    queue.addAll(computeNextStates(state));
                    System.out.println("Worker " + id + "  finish " + state.value  + " queue size = " + queue.size());
//                }
            }
            catch ( InterruptedException ie ) {
                System.out.println("tratata");
                // just terminate
            }
        }
    }

    public  List<ParallelSolver.State> computeNextStates(ParallelSolver.State state) {
        if (state.value.length() >= 4) {
            return List.of();
        }
        return Arrays.asList(new ParallelSolver.State(state.value + "0"), new ParallelSolver.State(state.value + "1"));
    }

    public ParallelSolver() {
        int MAX_Q_SIZE = 100;
        BlockingQueue<ParallelSolver.State> queue;

        queue = new ArrayBlockingQueue<ParallelSolver.State>(MAX_Q_SIZE);
        queue.add(new ParallelSolver.State(""));

        for (int i = 0; i < 3; ++i) {
            MyWorker worker = new MyWorker(i, queue);
            worker.start();
        }
        System.out.println(queue.size());

    }

    public static void main(String[] args) {
        ParallelSolver solver = new ParallelSolver();
    }
}


