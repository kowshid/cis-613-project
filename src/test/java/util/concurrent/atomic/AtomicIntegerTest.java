package util.concurrent.atomic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class AtomicIntegerTest {

    private final static int THREAD_COUNT = 999;

    // region Functionality Tests

    @Test
    @DisplayName("Default constructor -> 0")
    void testDefaultConstructor() {
        AtomicInteger atomicInteger = new AtomicInteger();
        assertEquals(0, atomicInteger.get(), "Default constructor should initialize to 0");
    }

    @Test
    @DisplayName("Constructor(0) -> 0")
    void testConstructorWithZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(0, atomicInteger.get(), "Constructor with zero should initialize to 0");
    }

    @Test
    @DisplayName("Constructor(1) -> 1")
    void testConstructorWithPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        assertEquals(1, atomicInteger.get(), "Constructor with one should initialize to 1");
    }

    @Test
    @DisplayName("Constructor(-1) -> -1")
    void testConstructorWithNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        assertEquals(-1, atomicInteger.get(), "Constructor with minus one should initialize to -1");
    }

    @Test
    @DisplayName("Constructor(Max) -> Max")
    void testConstructorWithMax() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get(), "Constructor with Max should initialize to Integer Max");
    }

    @Test
    @DisplayName("Constructor(Min) -> Min")
    void testConstructorWithMin() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get(), "Constructor with Min should initialize to Integer Min");
    }

    @Test
    @DisplayName("Constructor positive extreme")
    void testConstructorWithPositiveExtreme() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE + 1);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("Constructor negative Extreme")
    void testConstructorWithNegativeExtreme() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE - 1);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("set: match the zero")
    void testSetZero() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("set: match the positive value")
    void testSetPositive() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(1);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("set: match the negative value")
    void testSetNegative() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(-1);
        assertEquals(-1, atomicInteger.get());
    }


    @Test
    @DisplayName("set: match the max value")
    void testSetMax() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("set: match the min value")
    void testSetMin() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("set: extreme value should be positive")
    void testSetExtremePositive() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MAX_VALUE + 1);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("set: extreme value should be negative")
    void testSetExtremeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MIN_VALUE - 1);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("getAndSet: match the zero")
    void testGetAndSetZero() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        int oldValue = atomicInteger.getAndSet(0);
        assertEquals(1, oldValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndSet: match the positive value")
    void testGetAndSetPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndSet(1);
        assertEquals(0, oldValue);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndSet: match the negative value")
    void testGetAndSetNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndSet(-1);
        assertEquals(0, oldValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndSet: match the max value")
    void testGetAndSetMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndSet(Integer.MAX_VALUE);
        assertEquals(0, oldValue);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndSet: match the min value")
    void testGetAndSetMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndSet(Integer.MIN_VALUE);
        assertEquals(0, oldValue);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndSet: positive extreme value assertion should throw error")
    void testGetAndSetExtremePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndSet(Integer.MAX_VALUE + 1);
        assertEquals(0, oldValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("getAndSet: negative extreme value assertion should throw error")
    void testGetAndSetExtremeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndSet(Integer.MIN_VALUE - 1);
        assertEquals(0, oldValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("compareAndSet: match the zero")
    void testCompareAndSetZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, 0);
        assertTrue(success);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndSet: match the positive value")
    void testCompareAndSetPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, 1);
        assertTrue(success);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndSet: match the negative value")
    void testCompareAndSetNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, -1);
        assertTrue(success);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndSet: match the max value")
    void testCompareAndSetMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, Integer.MAX_VALUE);
        assertTrue(success);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndSet: match the min value")
    void testCompareAndSetMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, Integer.MIN_VALUE);
        assertTrue(success);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndSet: positive extreme value assertion should throw error")
    void testCompareAndSetExtremePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, Integer.MAX_VALUE + 1);
        assertTrue(success);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("compareAndSet: negative extreme value assertion should throw error")
    void testCompareAndSetExtremeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean success = atomicInteger.compareAndSet(0, Integer.MIN_VALUE - 1);
        assertTrue(success);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("compareAndSet: compare failure return the old value")
    void testCompareAndSetFailure() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean failure = atomicInteger.compareAndSet(-1, 1);
        assertFalse(failure);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndIncrement: match the zero")
    void testGetAndIncrementZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndIncrement();
        assertEquals(0, oldValue);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndIncrement: match the positive value")
    void testGetAndIncrementPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        int oldValue = atomicInteger.getAndIncrement();
        assertEquals(1, oldValue);
        assertEquals(2, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndIncrement: match the negative value")
    void testGetAndIncrementNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        int oldValue = atomicInteger.getAndIncrement();
        assertEquals(-1, oldValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndIncrement: positive extreme value assertion should throw error")
    void testGetAndIncrementMax() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        int oldValue = atomicInteger.getAndIncrement();
        assertEquals(Integer.MAX_VALUE, oldValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("getAndIncrement: match the min value")
    void testGetAndIncrementMin() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        int oldValue = atomicInteger.getAndIncrement();
        assertEquals(Integer.MIN_VALUE, oldValue);
        assertTrue(atomicInteger.get() < 0);
    }

    @Test
    @DisplayName("getAndDecrement: match the zero")
    void testGetAndDecrementZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndDecrement();
        assertEquals(0, oldValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndDecrement: match the positive value")
    void testGetAndDecrementPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        int oldValue = atomicInteger.getAndDecrement();
        assertEquals(1, oldValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndDecrement: match the negative value")
    void testGetAndDecrementNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        int oldValue = atomicInteger.getAndDecrement();
        assertEquals(-1, oldValue);
        assertEquals(-2, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndDecrement: match the max value")
    void testGetAndDecrementMax() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        int oldValue = atomicInteger.getAndDecrement();
        assertEquals(Integer.MAX_VALUE, oldValue);
        assertTrue(atomicInteger.get() > 0);
    }

    @Test
    @DisplayName("getAndDecrement: match the min value")
    void testGetAndDecrementMin() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        int oldValue = atomicInteger.getAndDecrement();
        assertEquals(Integer.MIN_VALUE, oldValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("getAndAdd: add zero")
    void testGetAndAddZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndAdd(0);
        assertEquals(0, oldValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAdd: add positive value")
    void testGetAndAddPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndAdd(1);
        assertEquals(0, oldValue);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAdd: add negative value")
    void testGetAndAddNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndAdd(-1);
        assertEquals(0, oldValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAdd: add max value")
    void testGetAndAddMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndAdd(Integer.MAX_VALUE);
        assertEquals(0, oldValue);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAdd: add min value")
    void testGetAndAddMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int oldValue = atomicInteger.getAndAdd(Integer.MIN_VALUE);
        assertEquals(0, oldValue);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAdd: test positive extreme")
    void testGetAndAddMaxExtreme() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        int oldValue = atomicInteger.getAndAdd(1);
        assertEquals(Integer.MAX_VALUE, oldValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("getAndAdd: test negative extreme")
    void testGetAndAddMinExtreme() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        int oldValue = atomicInteger.getAndAdd(-1);
        assertEquals(Integer.MIN_VALUE, oldValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("incrementAndGet: match the zero")
    void testIncrementAndGetZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(1, atomicInteger.incrementAndGet());
    }

    @Test
    @DisplayName("incrementAndGet: match the positive value")
    void testIncrementAndGetPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        assertEquals(2, atomicInteger.incrementAndGet());
    }

    @Test
    @DisplayName("incrementAndGet: match the negative value")
    void testIncrementAndGetNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        assertEquals(0, atomicInteger.incrementAndGet());
    }

    @Test
    @DisplayName("incrementAndGet: match the max value (overflow)")
    void testIncrementAndGetMax() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.incrementAndGet() > 0));
    }

    @Test
    @DisplayName("incrementAndGet: match the min value")
    void testIncrementAndGetMin() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        assertTrue(atomicInteger.get() < 0);
    }

    @Test
    @DisplayName("decrementAndGet: match the zero")
    void testDecrementAndGetZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(-1, atomicInteger.decrementAndGet());
    }

    @Test
    @DisplayName("decrementAndGet: match the positive value")
    void testDecrementAndGetPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        assertEquals(0, atomicInteger.decrementAndGet());
    }

    @Test
    @DisplayName("decrementAndGet: match the negative value")
    void testDecrementAndGetNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        assertEquals(-2, atomicInteger.decrementAndGet());
    }

    @Test
    @DisplayName("decrementAndGet: match the min value (underflow)")
    void testDecrementAndGetMax() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        assertTrue(atomicInteger.decrementAndGet() > 0);
    }

    @Test
    @DisplayName("decrementAndGet: match the min value (underflow)")
    void testDecrementAndGetMin() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.decrementAndGet() < 0));
    }

    @Test
    @DisplayName("addAndGet: add zero")
    void testAddAndGetZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(0, atomicInteger.addAndGet(0));
    }

    @Test
    @DisplayName("addAndGet: add positive value")
    void testAddAndGetPositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(1, atomicInteger.addAndGet(1));
    }

    @Test
    @DisplayName("addAndGet: add negative value")
    void testAddAndGetNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(-1, atomicInteger.addAndGet(-1));
    }

    @Test
    @DisplayName("addAndGet: add max value")
    void testAddAndGetMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(Integer.MAX_VALUE, atomicInteger.addAndGet(Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("addAndGet: add min value")
    void testAddAndGetMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        assertEquals(Integer.MIN_VALUE, atomicInteger.addAndGet(Integer.MIN_VALUE));
    }

    @Test
    @DisplayName("addAndGet: test positive extreme")
    void testAddAndGetMaxExtreme() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.addAndGet(1) > 0));
    }

    @Test
    @DisplayName("addAndGet: test negative extreme")
    void testAddAndGetMinExtreme() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.addAndGet(-1) < 0));
    }

    @Test
    @DisplayName("compareAndExchange: match the zero")
    void testCompareAndExchangeZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, 0);
        assertEquals(0, observed);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchange: match the positive value")
    void testCompareAndExchangePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, 1);
        assertEquals(0, observed);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchange: match the negative value")
    void testCompareAndExchangeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, -1);
        assertEquals(0, observed);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchange: match the max value")
    void testCompareAndExchangeMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, Integer.MAX_VALUE);
        assertEquals(0, observed);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchange: match the min value")
    void testCompareAndExchangeMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, Integer.MIN_VALUE);
        assertEquals(0, observed);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchange: positive extreme value assertion should throw error")
    void testCompareAndExchangeExtremePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, Integer.MAX_VALUE + 1);
        assertEquals(0, observed);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("compareAndExchange: negative extreme value assertion should throw error")
    void testCompareAndExchangeExtremeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(0, Integer.MIN_VALUE - 1);
        assertEquals(0, observed);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("compareAndExchange: compare failure return the old value")
    void testCompareAndExchangeFailure() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int observed = atomicInteger.compareAndExchange(-1, 1);
        assertEquals(0, observed);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: match the zero")
    void testCompareAndExchangeAcquireZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, 0);
        assertEquals(0, witnessValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: match the positive value")
    void testCompareAndExchangeAcquirePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, 1);
        assertEquals(0, witnessValue);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: match the negative value")
    void testCompareAndExchangeAcquireNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, -1);
        assertEquals(0, witnessValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: match the max value")
    void testCompareAndExchangeAcquireMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, Integer.MAX_VALUE);
        assertEquals(0, witnessValue);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: match the min value")
    void testCompareAndExchangeAcquireMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, Integer.MIN_VALUE);
        assertEquals(0, witnessValue);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: positive extreme value assertion should throw error")
    void testCompareAndExchangeAcquireExtremePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, Integer.MAX_VALUE + 1);
        assertEquals(0, witnessValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: negative extreme value assertion should throw error")
    void testCompareAndExchangeAcquireExtremeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(0, Integer.MIN_VALUE - 1);
        assertEquals(0, witnessValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("compareAndExchangeAcquire: compare failure return the old value")
    void testCompareAndExchangeAcquireFailure() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeAcquire(-1, 1);

        assertEquals(0, witnessValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: match the zero")
    void testCompareAndExchangeReleaseZero() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, 0);
        assertEquals(0, witnessValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: match the positive value")
    void testCompareAndExchangeReleasePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, 1);
        assertEquals(0, witnessValue);
        assertEquals(1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: match the negative value")
    void testCompareAndExchangeReleaseNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, -1);
        assertEquals(0, witnessValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: match the max value")
    void testCompareAndExchangeReleaseMax() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, Integer.MAX_VALUE);
        assertEquals(0, witnessValue);
        assertEquals(Integer.MAX_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: match the min value")
    void testCompareAndExchangeReleaseMin() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, Integer.MIN_VALUE);
        assertEquals(0, witnessValue);
        assertEquals(Integer.MIN_VALUE, atomicInteger.get());
    }

    @Test
    @DisplayName("compareAndExchangeRelease: positive extreme value assertion should throw error")
    void testCompareAndExchangeReleaseExtremePositive() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, Integer.MAX_VALUE + 1);
        assertEquals(0, witnessValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() > 0));
    }

    @Test
    @DisplayName("compareAndExchangeRelease: negative extreme value assertion should throw error")
    void testCompareAndExchangeReleaseExtremeNegative() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(0, Integer.MIN_VALUE - 1);
        assertEquals(0, witnessValue);
        assertThrows(AssertionError.class, () -> assertTrue(atomicInteger.get() < 0));
    }

    @Test
    @DisplayName("compareAndExchangeRelease: compare failure return the old value")
    void testCompareAndExchangeReleaseFailure() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int witnessValue = atomicInteger.compareAndExchangeRelease(-1, 1);
        assertEquals(0, witnessValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndUpdate: Addition lambda")
    void testGetAndUpdateAddition() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndUpdate(x -> x + 3);

        assertEquals(2, oldValue);
        assertEquals(5, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndUpdate: Substraction lambda")
    void testGetAndUpdateSubstraction() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndUpdate(x -> x - 3);

        assertEquals(2, oldValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndUpdate: Multiplication lambda")
    void testGetAndUpdateMultiplication() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndUpdate(x -> x * 3);
        assertEquals(2, oldValue);
        assertEquals(6, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndUpdate: Division lambda")
    void testGetAndUpdateDivision() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndUpdate(x -> x / 3);

        assertEquals(2, oldValue);
        assertEquals(0, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndUpdate: Negation lambda")
    void testGetAndUpdateNegation() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndUpdate(x -> -x);
        assertEquals(2, oldValue);
        assertEquals(-2, atomicInteger.get());
    }

    @Test
    @DisplayName("updateAndGet: Addition lambda")
    void testUpdateAndGetAddition() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(5, atomicInteger.updateAndGet(x -> x + 3));
    }

    @Test
    @DisplayName("updateAndGet: Subtraction lambda")
    void testUpdateAndGetSubtraction() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(-1, atomicInteger.updateAndGet(x -> x - 3));
    }

    @Test
    @DisplayName("updateAndGet: Multiplication lambda")
    void testUpdateAndGetMultiplication() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(6, atomicInteger.updateAndGet(x -> x * 3));
    }

    @Test
    @DisplayName("updateAndGet: Division lambda")
    void testUpdateAndGetDivision() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(0, atomicInteger.updateAndGet(x -> x / 3));
    }

    @Test
    @DisplayName("updateAndGet: Negation lambda")
    void testUpdateAndGetNegation() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(-2, atomicInteger.updateAndGet(x -> -x));
    }

    @Test
    @DisplayName("getAndAccumulate: Addition lambda")
    void testGetAndAccumulateAddition() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndAccumulate(3, (a, b) -> a + b);

        assertEquals(2, oldValue);
        assertEquals(5, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAccumulate: Subtraction lambda")
    void testGetAndAccumulateSubtraction() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndAccumulate(3, (a, b) -> a - b);
        assertEquals(2, oldValue);
        assertEquals(-1, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAccumulate: Multiplication lambda")
    void testGetAndAccumulateMultiplication() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndAccumulate(3, (a, b) -> a * b);
        assertEquals(2, oldValue);
        assertEquals(6, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAccumulate: Bitwise AND lambda")
    void testGetAndAccumulateBitwiseAnd() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndAccumulate(3, (a, b) -> a & b);
        assertEquals(2, oldValue);
        assertEquals(2 & 3, atomicInteger.get());
    }

    @Test
    @DisplayName("getAndAccumulate: Bitwise OR lambda")
    void testGetAndAccumulateBitwiseOr() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        int oldValue = atomicInteger.getAndAccumulate(3, (a, b) -> a | b);
        assertEquals(2, oldValue);
        assertEquals(2 | 3, atomicInteger.get());
    }

    @Test
    @DisplayName("accumulateAndGet: Addition lambda")
    void testAccumulateAndGetAddition() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(5, atomicInteger.accumulateAndGet(3, (a, b) -> a + b));
    }

    @Test
    @DisplayName("accumulateAndGet: Subtraction lambda")
    void testAccumulateAndGetSubtraction() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(-1, atomicInteger.accumulateAndGet(3, (a, b) -> a - b));
    }

    @Test
    @DisplayName("accumulateAndGet: Multiplication lambda")
    void testAccumulateAndGetMultiplication() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(6, atomicInteger.accumulateAndGet(3, (a, b) -> a * b));
    }

    @Test
    @DisplayName("accumulateAndGet: Bitwise AND lambda")
    void testAccumulateAndGetBitwiseAnd() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(2 & 3, atomicInteger.accumulateAndGet(3, (a, b) -> a & b));
    }

    @Test
    @DisplayName("accumulateAndGet: Bitwise OR lambda")
    void testAccumulateAndGetBitwiseOr() {
        AtomicInteger atomicInteger = new AtomicInteger(2);
        assertEquals(2 | 3, atomicInteger.accumulateAndGet(3, (a, b) -> a | b));
    }

    // endregion

    @RepeatedTest(20)
    @DisplayName("getAndSet: only one thread observes old value")
    void testGetAndSet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.getAndSet(1);

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(1, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndSet: only one thread observes old value")
    void testCompareAndSet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    boolean observed = atomicInteger.compareAndSet(0, 1);

                    if (observed) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(1, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("getAndIncrement: only one thread observes old value")
    void testGetAndIncrement() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.getAndIncrement();

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("getAndDecrement: only one thread observes old value")
    void testGetAndDecrement() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(THREAD_COUNT);

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
                    int observed = atomicInteger.getAndDecrement();

                    if (observed == THREAD_COUNT) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(0, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("getAndAdd: only one thread observes old value")
    void testGetAndAdd() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.getAndAdd(1);

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("incrementAndGet: only one thread observes a random value")
    void testIncrementAndGet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);
        int randomValue = new Random().nextInt(1, 10);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int observed = atomicInteger.incrementAndGet();

                    if (observed == randomValue) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("decrementAndGet: only one thread observes a random value")
    void testDecrementAndGet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(THREAD_COUNT);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);
        int randomValue = new Random().nextInt(THREAD_COUNT - 10, THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int observed = atomicInteger.decrementAndGet();

                    if (observed == randomValue) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(0, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("addAndGet: only one thread observes a random value")
    void testAddAndGet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);
        int randomValue = new Random().nextInt(1, 10);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int observed = atomicInteger.addAndGet(1);

                    if (observed == randomValue) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("getAndUpdate: only one thread observes the initial value")
    void testGetAndUpdate() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.getAndUpdate(x -> x + 1);

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("updateAndGet: only one thread observes the initial value")
    void testUpdateAndGet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);
        int randomValue = new Random().nextInt(1, 10);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int observed = atomicInteger.updateAndGet(x -> x + 1);

                    if (observed == randomValue) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("getAndAccumulate: only one thread observes the initial value")
    void testGetAndAccumulate() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(THREAD_COUNT);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);
        int randomValue = new Random().nextInt(THREAD_COUNT - 10, THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int observed = atomicInteger.getAndAccumulate(1, (a, b) -> a - b);

                    if (observed == randomValue) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(0, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("accumulateAndGet: only one thread observes the initial value")
    void testAccumulateAndGet() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger observedThreadCount = new AtomicInteger(0);
        int randomValue = new Random().nextInt(1, 10);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    int observed = atomicInteger.accumulateAndGet(1, (a, b) -> a + b);

                    if (observed == randomValue) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(THREAD_COUNT, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndExchange: only one thread observes the initial value")
    void testCompareAndExchange() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.compareAndExchange(0, 1);

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(1, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndExchangeAcquire: only one thread observes the initial value")
    void testCompareAndExchangeAcquire() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.compareAndExchangeAcquire(0, 1);

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(1, atomicInteger.get());
    }

    @RepeatedTest(20)
    @DisplayName("compareAndExchangeRelease: only one thread observes the initial value")
    void testCompareAndExchangeRelease() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

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
                    int observed = atomicInteger.compareAndExchangeRelease(0, 1);

                    if (observed == 0) {
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

        assertEquals(1, observedThreadCount.get());
        assertEquals(1, atomicInteger.get());
    }
}