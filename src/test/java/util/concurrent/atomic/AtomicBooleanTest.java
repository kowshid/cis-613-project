package util.concurrent.atomic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class AtomicBooleanTest {

    private final static int THREAD_COUNT = 999;

    @Test
    @DisplayName("Default constructor -> false")
    void testDefaultConstructor() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("Constructor(true) -> true")
    void testConstructWithTrue() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("Constructor(false) -> false")
    void testConstructWithFalse() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndSet: matches expected false -> updates and returns true")
    void testCompareFalseAndSetSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean success = atomicBoolean.compareAndSet(false, true);

        assertTrue(success);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndSet: mismatched expected false -> no update, returns false")
    void testCompareFalseAndSetFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean success = atomicBoolean.compareAndSet(true, true);
        assertFalse(success);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndSet: matches expected true -> updates and returns true")
    void testCompareTrueAndSetSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean success = atomicBoolean.compareAndSet(true, false);

        assertTrue(success);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndSet: mismatched expected true -> no update, returns false")
    void testCompareTrueAndSetFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean success = atomicBoolean.compareAndSet(false, false);
        assertFalse(success);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("set: updates the value to true")
    void testSetTrue() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        atomicBoolean.set(true);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("set: updates the value to false")
    void testSetFalse() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        atomicBoolean.set(false);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("getAndSet: returns previous false value and applies new true value")
    void testGetFalseAndSetTrue() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        boolean previous = atomicBoolean.getAndSet(true);
        assertFalse(previous);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("getAndSet: returns previous true value and applies new false value")
    void testGetTrueAndSetFalse() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);

        boolean previous = atomicBoolean.getAndSet(false);
        assertTrue(previous);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("toString: returns correct string for true")
    void testToStringTrue() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        assertEquals("true", atomicBoolean.toString());
    }

    @Test
    @DisplayName("toString: returns correct string for false")
    void testToStringFalse() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        assertEquals("false", atomicBoolean.toString());
    }

    @Test
    @DisplayName("compareAndExchange: matches expected false -> updates, returns observed == expected")
    void testCompareFalseAndExchangeSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean observed = atomicBoolean.compareAndExchange(false, true);

        assertFalse(observed);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchange: mismatched expected false -> no update, returns observed != expected")
    void testCompareFalseAndExchangeFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean observed = atomicBoolean.compareAndExchange(true, true);

        assertFalse(observed);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchange: matches expected true -> updates, returns observed == expected")
    void testCompareTrueAndExchangeSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean observed = atomicBoolean.compareAndExchange(true, false);

        assertTrue(observed);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchange: mismatched expected true -> no update, returns observed != expected")
    void testCompareTrueAndExchangeFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean observed = atomicBoolean.compareAndExchange(false, false);

        assertTrue(observed);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: matches expected false -> updates, returns observed == expected")
    void testCompareFalseAndExchangeAcquireSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean observed = atomicBoolean.compareAndExchangeAcquire(false, true);

        assertFalse(observed);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: mismatched expected false -> no update")
    void testCompareFalseAndExchangeAcquireFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean observed = atomicBoolean.compareAndExchangeAcquire(true, true);

        assertFalse(observed);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: matches expected true -> updates, returns observed == expected")
    void testCompareTrueAndExchangeAcquireSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean observed = atomicBoolean.compareAndExchangeAcquire(true, false);

        assertTrue(observed);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: mismatched expected true -> no update")
    void testCompareTrueAndExchangeAcquireFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean observed = atomicBoolean.compareAndExchangeAcquire(false, false);

        assertTrue(observed);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: matches expected false -> updates, returns observed == expected")
    void testCompareFalseAndExchangeReleaseSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean observed = atomicBoolean.compareAndExchangeRelease(false, true);

        assertFalse(observed);
        assertTrue(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: mismatched expected false-> no update")
    void testCompareFalseAndExchangeReleaseFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        boolean observed = atomicBoolean.compareAndExchangeRelease(true, true);

        assertFalse(observed);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: matches expected true -> updates, returns observed == expected")
    void testCompareTrueAndExchangeReleaseSuccess() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean observed = atomicBoolean.compareAndExchangeRelease(true, false);

        assertTrue(observed);
        assertFalse(atomicBoolean.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: mismatched expected true -> no update")
    void testCompareTrueAndExchangeReleaseFailure() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean observed = atomicBoolean.compareAndExchangeRelease(false, false);

        assertTrue(observed);
        assertTrue(atomicBoolean.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndSetAtomicity: only one can thread increments value")
    void testCompareAndSetAtomicity() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

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

                    if (atomicBoolean.compareAndSet(false, true)) {
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

        assertTrue(atomicBoolean.get());
        assertEquals(1, observedThreadCount.get());
    }

    @RepeatedTest(20)
    @DisplayName("getAndSetAtomicity: only one can thread increments value")
    void testGetAndSetAtomicity() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger falseToTrueCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    boolean previousValue = atomicBoolean.getAndSet(true);

                    if (!previousValue) {
                        falseToTrueCount.incrementAndGet();
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

        assertTrue(atomicBoolean.get());
        assertEquals(1, falseToTrueCount.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndExchangeAtomicity: only one can thread observes old value")
    void testCompareAndExchangeAtomicity() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

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
                    boolean observed = atomicBoolean.compareAndExchange(false, true);

                    if (!observed) {
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

        assertTrue(atomicBoolean.get());
        assertEquals(1, observedThreadCount.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndExchangeAcquireAtomicity: only one can thread observes old value")
    void testCompareAndExchangeAcquireAtomicity() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

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
                    boolean observed = atomicBoolean.compareAndExchangeAcquire(false, true);

                    if (!observed) {
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

        assertTrue(atomicBoolean.get());
        assertEquals(1, observedThreadCount.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndExchangeReleaseAtomicity: only one can thread observes old value")
    void testCompareAndExchangeReleaseAtomicity() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

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
                    boolean observed = atomicBoolean.compareAndExchangeRelease(false, true);

                    if (!observed) {
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

        assertTrue(atomicBoolean.get());
        assertEquals(1, observedThreadCount.get());
    }
}