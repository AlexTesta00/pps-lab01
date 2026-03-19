package it.unibo.pps.tdd;

import java.util.ArrayList;
import java.util.List;

public class MinMaxStackImpl implements MinMaxStack {

    private final List<Integer> stack;
    private int min;
    private int max;

    public MinMaxStackImpl() {
        this.stack = new ArrayList<>();
    }

    @Override
    public void push(int value) {
        this.stack.add(value);
        computeMinMax();
    }

    @Override
    public int pop() {
        checkNotEmptyOrThrow();
        int lastElement = this.stack.removeLast();
        if (!this.stack.isEmpty()) {
            computeMinMax();
        }
        return lastElement;
    }

    @Override
    public int peek() {
        checkNotEmptyOrThrow();
        return this.stack.getLast();
    }


    @Override
    public int getMin() {
        checkNotEmptyOrThrow();
        return this.min;
    }

    @Override
    public int getMax() {
        checkNotEmptyOrThrow();
        return this.max;
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public int size() {
        return this.stack.size();
    }

    private void computeMinMax() {
        checkNotEmptyOrThrow();

        int currentMin = Integer.MAX_VALUE;
        int currentMax = Integer.MIN_VALUE;

        for (int value : this.stack) {
            currentMin = Math.min(currentMin, value);
            currentMax = Math.max(currentMax, value);
        }

        this.min = currentMin;
        this.max = currentMax;
    }

    private void checkNotEmptyOrThrow() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
    }
}
