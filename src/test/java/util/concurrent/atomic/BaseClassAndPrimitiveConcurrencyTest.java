package util.concurrent.atomic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseClassAndPrimitiveConcurrencyTest {

    private final static int THREAD_COUNT = 999;
    private final static int INCREMENTS_PER_THREAD = 1000;

    @Test
    @DisplayName("testBoolean: demonstrates race condition")
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
                        Thread.sleep(1);
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
        assertTrue(observedThreadCount.get() > 1);
    }

    @Test
    @DisplayName("testPrimitiveBoolean: demonstrates race condition")
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
        assertTrue(observedThreadCount.get() > 1);
    }

    @Test
    @DisplayName("testInteger: demonstrates race conditions and lost updates with Integer")
    public void testIntegerRaceCondition() throws InterruptedException {
        Integer[] sharedCounter = new Integer[1];
        sharedCounter[0] = 0;

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

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
        System.out.println("Expected: " + expectedCount);
        System.out.println("Actual:   " + sharedCounter[0]);
    }

    @Test
    @DisplayName("testPrimitiveInt: demonstrates race conditions and lost updates with int")
    public void testPrimitiveIntRaceCondition() throws InterruptedException {
        int[] sharedCounter = {0};

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

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
        System.out.println("Expected: " + expectedCount);
        System.out.println("Actual:   " + sharedCounter[0]);
    }
}