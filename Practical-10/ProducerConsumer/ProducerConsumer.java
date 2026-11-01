public class ProducerConsumer {
    private static final int CAPACITY = 3;   // small shared buffer
    private static final int ITEMS = 10;     // fixed number of items

    private static final int[] buffer = new int[CAPACITY];
    private static int count = 0, in = 0, out = 0;

    // Explicit monitor: static synchronized methods lock the Class, but
    // wait()/notify() must be called on an object, so use this lock instead.
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread producer = new Thread(() -> {
            for (int item = 1; item <= ITEMS; item++) {
                produce(item);
                try {
                    Thread.sleep(50);        // pace production so consumer waits on empty buffer
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int expected = 1; expected <= ITEMS; expected++) {
                int item = consume();
                if (item != expected) {
                    System.err.printf("Order mismatch: expected %d but got %d%n", expected, item);
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("All " + ITEMS + " items produced and consumed in order; none lost.");
    }

    private static void produce(int item) {
        synchronized (LOCK) {
            while (count == CAPACITY) {          // buffer full -> wait for space
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            buffer[in] = item;
            in = (in + 1) % CAPACITY;
            count++;
            System.out.printf("Produced %2d  (buffer size = %d)%n", item, count);
            LOCK.notifyAll();                    // wake a waiting consumer
        }
    }

    private static int consume() {
        synchronized (LOCK) {
            while (count == 0) {                 // buffer empty -> wait for data
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return -1;
                }
            }
            int item = buffer[out];
            out = (out + 1) % CAPACITY;
            count--;
            System.out.printf("Consumed %2d  (buffer size = %d)%n", item, count);
            LOCK.notifyAll();                    // wake a waiting producer
            return item;
        }
    }
}
