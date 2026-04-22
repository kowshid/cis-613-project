package util.concurrent.atomic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

public class BaseClassAndPrimitiveConcurrencyTest {

    private static final int THREAD_COUNT = 999;
    private static final int INCREMENTS_PER_THREAD = 1000;
    private static final long WASTE_CYCLE_COUNT = 9999999L;
    private static final long READER_MAX_LIFESPAN = 5000L;

    @Test
    @DisplayName("testBoolean: DOESNOT demonstrates race condition")
    void testBooleanRaceCondition() throws InterruptedException {
        Boolean[] standardBool = {Boolean.FALSE};

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    if (!standardBool[0]) {
                        standardBool[0] = Boolean.TRUE;
                        observedThreadCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();
        executor.shutdown();

        System.out.println("Standard Boolean object race condition allowed " + observedThreadCount.get() +
                " threads to execute 'once-only' code.");
        // assertTrue(observedThreadCount.get() > 1);
    }

    @Test
    @DisplayName("testPrimitiveBoolean: DOES NOT demonstrates race condition")
    void testPrimitiveBooleanRaceCondition() throws InterruptedException {
        boolean[] standardBool = new boolean[]{false};

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    if (!standardBool[0]) {
                        Thread.sleep(1);
                        standardBool[0] = true;
                        observedThreadCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();
        executor.shutdown();

        System.out.println("Primitive boolean race condition allowed " + observedThreadCount.get() +
                " threads to execute 'once-only' code.");
        // assertTrue(observedThreadCount.get() > 1);
    }

    @Test
    @DisplayName("Primitive Boolean: demonstrates stale condition")
    void testPrimitiveBooleanStaleCondition() throws InterruptedException {

        AtomicLong spinsBeforeSeen = new AtomicLong(0);
        CountDownLatch readyLatch = new CountDownLatch(1);
        boolean[] sharedFlag = {false};

        Thread reader = new Thread(() -> {
            readyLatch.countDown();

            long spins = 0;
            while (!sharedFlag[0]) {
                spins++;
            }

            spinsBeforeSeen.set(spins);
        }, "reader");

        Thread writer = new Thread(() -> {
            try {
                readyLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long waste = 0;
            for (long i = 0; i < WASTE_CYCLE_COUNT; i++) {
                waste += i;
            }

            sharedFlag[0] = true;
            assertTrue(waste > 0);
        }, "writer");

        reader.start();
        writer.start();
        reader.join(READER_MAX_LIFESPAN);

        assertTrue(sharedFlag[0]);

        if (reader.isAlive()) {
            System.out.println("testPrimitiveBooleanStaleCondition\nReader still alive after writer set sharedFlag. spins = " + spinsBeforeSeen.get());
        } else {
            System.out.println("testPrimitiveBooleanStaleCondition\nReader exited after writer set sharedFlag. spins = " + spinsBeforeSeen.get());
        }
    }

    @Test
    @DisplayName("Boolean: demonstrates non-stale condition")
    void testBooleanStaleCondition() throws InterruptedException {

        AtomicLong spinsBeforeSeen = new AtomicLong(0);
        CountDownLatch readyLatch = new CountDownLatch(1);
        Boolean[] sharedFlag = new Boolean[]{false};

        Thread reader = new Thread(() -> {
            readyLatch.countDown();

            long spins = 0;
            while (!sharedFlag[0]) {
                spins++;
            }

            spinsBeforeSeen.set(spins);
        }, "reader");

        Thread writer = new Thread(() -> {
            try {
                readyLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long waste = 0;
            for (long i = 0; i < WASTE_CYCLE_COUNT; i++) {
                waste += i;
            }

            sharedFlag[0] = true;
            assertTrue(waste > 0);
        }, "writer");

        reader.start();
        writer.start();
        reader.join(READER_MAX_LIFESPAN);

        assertTrue(sharedFlag[0]);

        if (reader.isAlive()) {
            System.out.println("testBooleanStaleCondition\nReader still alive after writer set sharedFlag. spins = " + spinsBeforeSeen.get());
        } else {
            System.out.println("testBooleanStaleCondition\nReader exited after writer set sharedFlag. spins = " + spinsBeforeSeen.get());
        }
    }

    @Test
    @DisplayName("Atomic Boolean: demonstrates non-stale condition")
    void testAtomicBooleanNonStaleCondition() throws InterruptedException {

        AtomicBoolean stalenessDetected = new AtomicBoolean(false);
        AtomicLong spinsBeforeSeen = new AtomicLong(0);
        CountDownLatch readyLatch = new CountDownLatch(1);
        AtomicBoolean[] sharedFlag = new AtomicBoolean[1];
        sharedFlag[0] = new AtomicBoolean(false);

        Thread reader = new Thread(() -> {
            readyLatch.countDown();

            long spins = 0;
            while (!sharedFlag[0].get()) {
                spins++;
            }

            spinsBeforeSeen.set(spins);
        }, "reader");

        Thread writer = new Thread(() -> {
            try {
                readyLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long waste = 0;
            for (long i = 0; i < WASTE_CYCLE_COUNT; i++) {
                waste += i;
            }

            sharedFlag[0].set(true);
            assertTrue(waste > 0);
        }, "writer");

        reader.start();
        writer.start();
        reader.join(READER_MAX_LIFESPAN);

        assertTrue(sharedFlag[0].get());

        if (reader.isAlive()) {
            stalenessDetected.set(true);
            System.out.println("testAtomicBooleanNonStaleCondition\nReader still alive after writer set sharedFlag. spins = " + spinsBeforeSeen.get());
        } else {
            System.out.printf("testAtomicBooleanNonStaleCondition\nReader exited after writer set sharedFlag. spins = " + spinsBeforeSeen.get());
        }

        assertFalse(stalenessDetected.get());
    }

    @Test
    @DisplayName("testInteger: demonstrates lost updates with Integer")
    public void testIntegerRaceCondition() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        Integer[] sharedCounter = new Integer[1];
        sharedCounter[0] = 0;

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        sharedCounter[0]++;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();
        executor.shutdown();

        int expectedCount = THREAD_COUNT * INCREMENTS_PER_THREAD;
        assertNotEquals(expectedCount, sharedCounter[0]);
        System.out.println("testIntegerRaceCondition");
        System.out.println("Expected: " + expectedCount);
        System.out.println("Actual: " + sharedCounter[0]);
    }

    @Test
    @DisplayName("testPrimitiveInt: demonstrates lost updates with int")
    public void testPrimitiveIntRaceCondition() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);
        int[] sharedCounter = {0};

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        sharedCounter[0]++;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();
        executor.shutdown();

        int expectedCount = THREAD_COUNT * INCREMENTS_PER_THREAD;
         System.out.println("testPrimitiveIntRaceCondition");
        System.out.println("Expected: " + expectedCount);
        System.out.println("Actual: " + sharedCounter[0]);
    }

    @Test
    @DisplayName("testAtomicInteger: demonstrates no updates is lost")
    public void testAtomicIntegerNonRaceCondition() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger[] sharedCounter = new AtomicInteger[1];
        sharedCounter[0] = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        sharedCounter[0].getAndIncrement();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();
        executor.shutdown();

        int expectedCount = THREAD_COUNT * INCREMENTS_PER_THREAD;
        assertEquals(expectedCount, sharedCounter[0].get());
        System.out.println("testAtomicIntegerNonRaceCondition");
        System.out.println("Expected: " + expectedCount);
        System.out.println("Actual: " + sharedCounter[0].get());
    }
}