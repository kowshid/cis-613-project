import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerJpfTest {
    private final static int THREAD_COUNT = 3;
    private final static int INCREMENTS_PER_THREAD = 3;

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger[] sharedCounter = new AtomicInteger[1];
        sharedCounter[0] = new AtomicInteger(0);

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    sharedCounter[0].getAndIncrement();
                }
            });
            threads[i].start();
        }   

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        int expectedCount = THREAD_COUNT * INCREMENTS_PER_THREAD;
        
        assert expectedCount == sharedCounter[0].get() : "Count mismatch! Expected: " + expectedCount + ", Actual: " + sharedCounter[0].get();
        
        System.out.println("testAtomicIntegerNonRaceCondition");
        System.out.println("Expected: " + expectedCount);
        System.out.println("Actual: " + sharedCounter[0].get());
    }
}