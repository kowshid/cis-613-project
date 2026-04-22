import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanJpfTest {

    private static final long WASTE_CYCLE_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {

        AtomicBoolean stalenessDetected = new AtomicBoolean(false);
        long[] spinsBeforeSeen = new long[1];

        AtomicBoolean[] sharedFlag = new AtomicBoolean[1];
        sharedFlag[0] = new AtomicBoolean(false);

        Object lock = new Object();

        Thread reader = new Thread(() -> {
            long spins = 0;
            synchronized (lock) {
                while (!sharedFlag[0].get()) {
                    try {
                        lock.wait();
                        spins++;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            spinsBeforeSeen[0] = spins;
        }, "reader");

        Thread writer = new Thread(() -> {
            long waste = 0;
            for (long i = 0; i < WASTE_CYCLE_COUNT; i++) {
                waste += i;
            }

            synchronized (lock) {
                sharedFlag[0].set(true);
                lock.notifyAll();
            }

            assert waste >= 0 : "Waste computation failed";
        }, "writer");

        reader.start();
        writer.start();

        writer.join();
        reader.join();

        assert sharedFlag[0].get() : "Flag was not set to true";
        assert !stalenessDetected.get() : "Staleness was detected";

        System.out.println("testAtomicBooleanNonStaleCondition");
        System.out.println("Reader exited after writer set sharedFlag. spins = " + spinsBeforeSeen[0]);
    }
}