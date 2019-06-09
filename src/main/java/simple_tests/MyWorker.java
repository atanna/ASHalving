package simple_tests;

import java.util.concurrent.BlockingQueue;

public class MyWorker extends Thread {
    private final BlockingQueue<String> queue;
    private String name;
    public MyWorker(BlockingQueue<String> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    public void run() {
        try {
            while ( true ) {
                String s = queue.take();
                doWork(s);
            }
        }
        catch ( InterruptedException ie ) {
            // just terminate
        }
    }

    void doWork(String s) {
        System.out.println("Worker " + name + "  " + s);
    }
}