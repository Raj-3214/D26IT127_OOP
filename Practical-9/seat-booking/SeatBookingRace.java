import java.util.concurrent.atomic.AtomicInteger;

public class SeatBookingRace {
    private static final int TOTAL_SEATS = 5;
    private static final int THREADS = 10;

    private static int seatsLeft;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Without synchronization ---");
        run(false);

        System.out.println();
        System.out.println("--- With synchronization ---");
        run(true);
    }

    private static void run(boolean sync) throws InterruptedException {
        seatsLeft = TOTAL_SEATS;
        AtomicInteger booked = new AtomicInteger();

        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                boolean success = sync ? book() : bookUnsafe();
                if (success) {
                    booked.incrementAndGet();
                }
            });
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Seats available : " + TOTAL_SEATS);
        System.out.println("Threads trying  : " + THREADS);
        System.out.println("Bookings made   : " + booked.get());
        System.out.println("Seats left      : " + seatsLeft);
        System.out.println(booked.get() == TOTAL_SEATS
                ? "Result          : CORRECT - exactly " + TOTAL_SEATS + " seats booked"
                : "Result          : WRONG - oversold (" + booked.get()
                        + " bookings for " + TOTAL_SEATS + " seats)");
    }

    // UNSAFE: check-then-act is not atomic, so several threads can pass the check.
    private static boolean bookUnsafe() {
        if (seatsLeft > 0) {
            pauseToWidenRaceWindow();
            seatsLeft--;
            return true;
        }
        return false;
    }

    // SAFE: the whole check-then-decrement runs under the class lock.
    private static synchronized boolean book() {
        if (seatsLeft > 0) {
            pauseToWidenRaceWindow();
            seatsLeft--;
            return true;
        }
        return false;
    }

    private static void pauseToWidenRaceWindow() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
