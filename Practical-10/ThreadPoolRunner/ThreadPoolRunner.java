import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {
    public static void main(String[] args) throws InterruptedException {
        // Fixed pool of 3 worker threads, reused for all submitted tasks.
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            final int id = i;
            pool.submit(() -> {
                String worker = Thread.currentThread().getName();
                System.out.printf("Task %2d started  on %s%n", id, worker);
                try {
                    Thread.sleep(200);   // simulate brief work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.printf("Task %2d finished on %s%n", id, worker);
            });
        }

        pool.shutdown();                         // no new tasks accepted
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
        System.out.println("All 10 tasks completed; pool shut down.");
    }
}
