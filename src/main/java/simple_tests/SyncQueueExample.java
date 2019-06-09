package simple_tests;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SyncQueueExample {

    private BlockingQueue<String> queue;

    public class MyWorkerNew extends Thread {
        private final BlockingQueue<String> queue;
        private String name;
        private AtomicBoolean isFinished;
        private AtomicBoolean isFinishedOther;
        public MyWorkerNew(BlockingQueue<String> queue, AtomicBoolean isFinished, AtomicBoolean isFinishedOther, String name) {
            this.queue = queue;
            this.isFinished = isFinished;
            this.isFinishedOther = isFinishedOther;
            this.name = name;
        }

        public void run() {
            try {
                while (!queue.isEmpty() || !isFinishedOther.get()) {
                    System.out.println(name + " we are here  " + queue.size());

//                    String s = queue.take();
                    String s = queue.poll(100, TimeUnit.MILLISECONDS);
                    if(s == null) {
                        isFinished.set(true);
                        if (isFinishedOther.get()) {
                            break;
                        }
                        continue;
                    }

                    System.out.println("take it");

                    isFinished.set(false);
                    doWork(s);
                }
            }
            catch ( InterruptedException ie ) {
                // just terminate
            }
        }

        void doWork(String s) throws InterruptedException {
            System.out.println("Worker " + name + "  " + s);
            if (s.length() < 10) {
                queue.put(name + "_" + s);
            } else {
                isFinished.set(true);
            }
        }
    }

    public  SyncQueueExample() {
        try {
            int workItem = 0;
            // Create a synchronous queue
            BlockingQueue<String> queue = new ArrayBlockingQueue<>(10000, true);
//            queue = new SynchronousQueue<>();

            // Create the child worker thread

            AtomicBoolean isFinished1 = new AtomicBoolean(false);
            AtomicBoolean isFinished2 = new AtomicBoolean(false);
            MyWorkerNew worker1 = new MyWorkerNew(queue, isFinished1, isFinished2, "1");
            MyWorkerNew worker2 = new MyWorkerNew(queue, isFinished2, isFinished1, "2");

            System.out.println("start");
            worker1.start();
            worker2.start();
            queue.put("tratata");
            // Start sending to the queue
//            while (!queue.isEmpty())
//                System.out.println("\nPlacing work on queue");
//                String work = "Work Item:" + (++workItem);
//                queue.put(queue.take());
//
            } catch (Exception ex) {
                ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

        SyncQueueExample example = new SyncQueueExample();
    }
}
