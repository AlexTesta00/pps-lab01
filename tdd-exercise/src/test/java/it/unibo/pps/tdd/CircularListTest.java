package it.unibo.pps.tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The test suite for testing the CircularList implementation
 */
public class CircularListTest {
    private static final int DEFAULT_CAPACITY = 3;
    private static final int SIZE_OF_EMPTY_QUEUE = 0;

    CircularQueue circularQueue;

    @BeforeEach
    void setUp() {
        this.circularQueue = new CircularQueueImpl(DEFAULT_CAPACITY);
    }

    private void populateQueueWithValues(final List<Integer> values) {
        values.forEach(circularQueue::enqueue);
    }

    private void drainQueueForEachTimes(final int times) {
        for (int i = 0; i < times; i++) {
            circularQueue.dequeue();
        }
    }

    @Nested
    class TestConstructor {

        @Test
        void testWrongCapacityZero() {
            assertThrows(IllegalArgumentException.class, () -> new CircularQueueImpl(0));
        }

        @Test
        void testWrongNegativeCapacity() {
            assertThrows(IllegalArgumentException.class, () -> new CircularQueueImpl(-1));
        }

        @Test
        void testCorrectCapacity() {
            final int customCapacity = 5;
            final CircularQueue queue = new CircularQueueImpl(customCapacity);

            assertAll(
                    () -> assertEquals(customCapacity, queue.capacity()),
                    () -> assertTrue(queue.isEmpty()),
                    () -> assertFalse(queue.isFull()),
                    () -> assertEquals(SIZE_OF_EMPTY_QUEUE, queue.size())
            );
        }
    }

    @Nested
    class TestEmptyQueue {

        @Test
        void testWrongDequeueInEmptyQueue() {
            assertThrows(IllegalStateException.class, () -> circularQueue.dequeue());
        }

        @Test
        void testWrongPeekInEmptyQueue() {
            assertThrows(IllegalStateException.class, () -> circularQueue.peek());
        }

        @Test
        void testCorrectEmptyQueueFunctionInEmptyQueue() {
            assertTrue(circularQueue.isEmpty());
        }

        @Test
        void testCorrectFullQueueFunctionInEmptyQueue() {
            assertFalse(circularQueue.isFull());
        }

        @Test
        void testCorrectSizeInEmptyQueue() {
            assertEquals(SIZE_OF_EMPTY_QUEUE, circularQueue.size());
        }

        @Test
        void testCorrectCapacityInEmptyQueue() {
            assertEquals(DEFAULT_CAPACITY, circularQueue.capacity());
        }
    }

    @Nested
    class TestQueueWithOneElement {

        private static final int ONE_VALUE = 7;

        @BeforeEach
        void setUp() {
            populateQueueWithValues(List.of(ONE_VALUE));
        }

        @Test
        void testQueueWithOneElement() {
            assertAll(
                    () -> assertEquals(ONE_VALUE, circularQueue.peek()),
                    () -> assertFalse(circularQueue.isEmpty()),
                    () -> assertFalse(circularQueue.isFull()),
                    () -> assertEquals(1, circularQueue.size()),
                    () -> assertEquals(DEFAULT_CAPACITY, circularQueue.capacity())
            );
        }

        @Test
        void testDequeueInQueueWithOneElement() {
            assertAll(
                    () -> assertEquals(ONE_VALUE, circularQueue.dequeue()),
                    () -> assertTrue(circularQueue.isEmpty()),
                    () -> assertFalse(circularQueue.isFull()),
                    () -> assertEquals(SIZE_OF_EMPTY_QUEUE, circularQueue.size())
            );
        }
    }

    @Nested
    class TestQueueWithMultipleElementsNotFull {

        @BeforeEach
        void setUp() {
            populateQueueWithValues(List.of(4, 5));
        }

        @Test
        void testCorrectValuesInQueueWithMultipleElements() {
            assertAll(
                    () -> assertEquals(4, circularQueue.peek()),
                    () -> assertFalse(circularQueue.isEmpty()),
                    () -> assertFalse(circularQueue.isFull()),
                    () -> assertEquals(2, circularQueue.size())
            );
        }

        @Test
        void testCorrectFifoOrder() {
            assertAll(
                    () -> assertEquals(4, circularQueue.dequeue()),
                    () -> assertEquals(5, circularQueue.dequeue()),
                    () -> assertTrue(circularQueue.isEmpty()),
                    () -> assertEquals(SIZE_OF_EMPTY_QUEUE, circularQueue.size())
            );
        }
    }

    @Nested
    class TestFullQueue {

        @BeforeEach
        void setUp() {
            populateQueueWithValues(List.of(4, 5, 3));
        }

        @Test
        void testCorrectValuesInFullQueue() {
            assertAll(
                    () -> assertEquals(4, circularQueue.peek()),
                    () -> assertFalse(circularQueue.isEmpty()),
                    () -> assertTrue(circularQueue.isFull()),
                    () -> assertEquals(DEFAULT_CAPACITY, circularQueue.size())
            );
        }

        @Test
        void testCorrectFifoOrderInFullQueue() {
            assertAll(
                    () -> assertEquals(4, circularQueue.dequeue()),
                    () -> assertEquals(5, circularQueue.dequeue()),
                    () -> assertEquals(3, circularQueue.dequeue()),
                    () -> assertTrue(circularQueue.isEmpty()),
                    () -> assertEquals(SIZE_OF_EMPTY_QUEUE, circularQueue.size())
            );
        }
    }

    @Nested
    class TestOverwriteBehaviour {

        @BeforeEach
        void setUp() {
            populateQueueWithValues(List.of(4, 5, 3));
        }

        @Test
        void testOverwriteOldestElementWhenQueueIsFull() {
            circularQueue.enqueue(9);

            assertAll(
                    () -> assertEquals(5, circularQueue.peek()),
                    () -> assertTrue(circularQueue.isFull()),
                    () -> assertEquals(DEFAULT_CAPACITY, circularQueue.size()),
                    () -> assertEquals(5, circularQueue.dequeue()),
                    () -> assertEquals(3, circularQueue.dequeue()),
                    () -> assertEquals(9, circularQueue.dequeue()),
                    () -> assertTrue(circularQueue.isEmpty())
            );
        }

        @Test
        void testMultipleOverwriteWhenQueueIsFull() {
            circularQueue.enqueue(9);
            circularQueue.enqueue(8);

            assertAll(
                    () -> assertEquals(3, circularQueue.peek()),
                    () -> assertTrue(circularQueue.isFull()),
                    () -> assertEquals(DEFAULT_CAPACITY, circularQueue.size()),
                    () -> assertEquals(3, circularQueue.dequeue()),
                    () -> assertEquals(9, circularQueue.dequeue()),
                    () -> assertEquals(8, circularQueue.dequeue()),
                    () -> assertTrue(circularQueue.isEmpty())
            );
        }
    }

    @Nested
    class TestWrapAroundBehaviour {

        @BeforeEach
        void setUp() {
            populateQueueWithValues(List.of(1, 2, 3));
            drainQueueForEachTimes(2);
            populateQueueWithValues(List.of(4, 5));
        }

        @Test
        void testCorrectOrderAfterWrapAround() {
            assertAll(
                    () -> assertEquals(3, circularQueue.peek()),
                    () -> assertTrue(circularQueue.isFull()),
                    () -> assertEquals(DEFAULT_CAPACITY, circularQueue.size()),
                    () -> assertEquals(3, circularQueue.dequeue()),
                    () -> assertEquals(4, circularQueue.dequeue()),
                    () -> assertEquals(5, circularQueue.dequeue()),
                    () -> assertTrue(circularQueue.isEmpty())
            );
        }
    }

    @Nested
    class TestDrainQueue {

        @BeforeEach
        void setUp() {
            populateQueueWithValues(List.of(10, 20, 30));
        }

        @Test
        void testWrongDequeueAfterDrainAllQueue() {
            drainQueueForEachTimes(DEFAULT_CAPACITY);

            assertAll(
                    () -> assertThrows(IllegalStateException.class, () -> circularQueue.dequeue()),
                    () -> assertThrows(IllegalStateException.class, () -> circularQueue.peek()),
                    () -> assertTrue(circularQueue.isEmpty()),
                    () -> assertFalse(circularQueue.isFull()),
                    () -> assertEquals(SIZE_OF_EMPTY_QUEUE, circularQueue.size())
            );
        }
    }
}
