public class CounterRace {
    private static final int THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 500_000;

    private static int counter;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Without synchronization ---");
        run(false);

        System.out.println();
        System.out.println("--- With synchronization ---");
        run(true);
    }

    private static void run(boolean sync) throws InterruptedException {
        counter = 0;

        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    if (sync) {
                        incrementSynchronized();
                    } else {
                        increment();
                    }
                }
            });
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        int expected = THREADS * INCREMENTS_PER_THREAD;
        System.out.println("Expected : " + expected);
        System.out.println("Actual   : " + counter);
        System.out.println(counter == expected
                ? "Result   : CORRECT"
                : "Result   : WRONG - lost updates");
    }

    private static void increment() {
        counter++;
    }

    private static synchronized void incrementSynchronized() {
        counter++;
    }
}
