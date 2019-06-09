package simple_tests;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ExecutorsExample {

    private volatile ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ExecutorsExample() {
        executorService = Executors.newFixedThreadPool(2);

    }


    public void run() {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service with a thread pool of Size 2");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("tratata " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                return "Next";
            }
        }, executorService);
        future.thenApply(
                x -> {
                    System.out.println(x + " " + Thread.currentThread().getName());
                    return "next";
                }
        );


// Block and wait for the future to complete
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        Runnable task1 = () -> {
            System.out.println("Executing Task1 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(2);



            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };


        Runnable task2 = () -> {
            System.out.println("Executing Task2 inside : " + Thread.currentThread().getName());
            try {
                for (int i = 0 ; i < 10; ++i) {
                    int finalI = i;
                    Runnable task = () -> {
                        System.out.println("Executing Task " + String.valueOf(finalI) + " inside : " + Thread.currentThread().getName());
                        try {
                            TimeUnit.SECONDS.sleep(2);



                        } catch (InterruptedException ex) {
                            throw new IllegalStateException(ex);
                        }
                    };
                    executorService.submit(task);
                }
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };

        Runnable task3 = () -> {
            System.out.println("Executing Task3 inside : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        };


        System.out.println("Submitting the tasks for execution...");
//        executorService.submit(task1);
        executorService.submit(task2);
        executorService.submit(task3);

        executorService.shutdown();
    }

    public static void main(String[] args) {
        ExecutorsExample executor = new ExecutorsExample();
        executor.run();
    }
}
