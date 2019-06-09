package simple_tests;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int MAX_Q_SIZE = 10000;

    public static void main(String[] args) {
//        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(MAX_Q_SIZE);
//        int POOL_SIZE = 5;
//        for ( int i = 0; i < POOL_SIZE; i++ ) {
//            MyWorker worker = new MyWorker(queue, String.valueOf(i));
//            worker.start();
//        }


        String halfName = "aba_t";
        System.out.println(halfName.substring(0, halfName.length() - 2) + ' ' +  halfName.substring(halfName.length()-1));
//        SyncQueueExample example = new SyncQueueExample();
    }
}
