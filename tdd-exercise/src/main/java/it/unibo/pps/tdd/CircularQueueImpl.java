package it.unibo.pps.tdd;

public class CircularQueueImpl implements CircularQueue {

    private final int[] elements;
    private int head;
    private int size;

    public CircularQueueImpl(final int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        this.elements = new int[capacity];
        this.head = 0;
        this.size = 0;
    }

    @Override
    public void enqueue(final int element) {
        if (this.isFull()) {
            this.elements[this.head] = element;
            this.head = this.nextIndex(this.head);
        } else {
            final int tail = (this.head + this.size) % this.capacity();
            this.elements[tail] = element;
            this.size++;
        }
    }

    @Override
    public int dequeue() {
        this.checkNotEmpty();
        final int value = this.elements[this.head];
        this.head = this.nextIndex(this.head);
        this.size--;
        return value;
    }

    @Override
    public int peek() {
        this.checkNotEmpty();
        return this.elements[this.head];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return this.elements.length;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean isFull() {
        return this.size == this.capacity();
    }

    private void checkNotEmpty() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
    }

    private int nextIndex(final int index) {
        return (index + 1) % this.capacity();
    }
}