package it.unibo.pps.tdd;

/**
 *  Task 3 - TDD for Circular Queue
 *  A simple CircularQueue that stores integers with a **fixed** capacity.
 *  When full, new elements overwrite the oldest ones.
 *  <br>
 *  When removing elements, the oldest ones are removed first.
 *  Therefore, giving [4, 5, 3], the first element to be removed is 4, then 5, and finally 3.
 *  <br>
 *  For the exercise: 
 *   - Think about the test cases you need to write.
 *   - Introduce methods in the interface in order to make the tests pass.
 *   - Refactor
 */
public interface CircularQueue {

    /**
     * Adds an element to the queue.
     * If the queue is full, the oldest element is overwritten.
     *
     * @param element the element to add
     */
    void enqueue(int element);

    /**
     * Removes and returns the oldest element in the queue.
     *
     * @return the oldest element
     * @throws IllegalStateException if the queue is empty
     */
    int dequeue();

    /**
     * Returns the oldest element without removing it.
     *
     * @return the oldest element
     * @throws IllegalStateException if the queue is empty
     */
    int peek();

    /**
     * @return the current number of elements in the queue
     */
    int size();

    /**
     * @return the maximum capacity of the queue
     */
    int capacity();

    /**
     * @return true if the queue is empty
     */
    boolean isEmpty();

    /**
     * @return true if the queue is full
     */
    boolean isFull();
}