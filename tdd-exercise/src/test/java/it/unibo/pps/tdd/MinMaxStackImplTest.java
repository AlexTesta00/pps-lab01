package it.unibo.pps.tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinMaxStackImplTest {

    private static final int SIZE_OF_EMPTY_STACK = 0;

    MinMaxStack minMaxStack;

    @BeforeEach
    void setUp() {
        this.minMaxStack = new MinMaxStackImpl();
    }

    private void populateStackWithValues(List<Integer> values){
        values.forEach(minMaxStack::push);
    }

    private void drainStackForEachTimes(int times){
        for(int i = 0; i < times; i++){
            minMaxStack.pop();
        }
    }

    @Nested
    class TestEmptyStack{
        @Test
        void testWrongPopInEmptyStack(){
            assertThrows(IllegalStateException.class, () -> minMaxStack.pop());
        }

        @Test
        void testWrongPeekInEmptyStack(){
            assertThrows(IllegalStateException.class, () -> minMaxStack.peek());
        }

        @Test
        void testWrongGetMinInEmptyStack(){
            assertThrows(IllegalStateException.class, () -> minMaxStack.getMin());
        }

        @Test
        void testWrongGetMaxInEmptyStack(){
            assertThrows(IllegalStateException.class, () -> minMaxStack.getMax());
        }

        @Test
        void testCorrectEmptyStackFunctionInEmptyStack(){
            assertTrue(minMaxStack.isEmpty());
        }

        @Test
        void testCorrectSizeInEmptyStack(){
            assertEquals(SIZE_OF_EMPTY_STACK, minMaxStack.size());
        }
    }

    @Nested
    class TestStackOverflow{
        @Nested
        class TestStackWithOneElement{

            private static final int ONE_VALUE = 1;

            @BeforeEach
            void setUp(){
                populateStackWithValues(List.of(ONE_VALUE));
            }

            @Test
            void testStackWithOneElement(){
                int expectedSizeInStack = 1;
                assertAll(
                        () -> assertEquals(ONE_VALUE, minMaxStack.peek()),
                        () -> assertEquals(ONE_VALUE, minMaxStack.getMin()),
                        () -> assertEquals(ONE_VALUE, minMaxStack.getMax()),
                        () -> assertFalse(minMaxStack.isEmpty()),
                        () -> assertEquals(expectedSizeInStack, minMaxStack.size())
                );
            }

            @Test
            void testPopInStackWithOneElement(){
                assertAll(
                        () -> assertEquals(ONE_VALUE, minMaxStack.pop()),
                        () -> assertTrue(minMaxStack.isEmpty()),
                        () -> assertEquals(SIZE_OF_EMPTY_STACK, minMaxStack.size())
                );
            }
        }

        @Nested
        class TestStackWithMultipleElements{
            @BeforeEach
            void setUp(){
                populateStackWithValues(List.of(1, 4, 3, 5));
            }

            @Test
            void testCorrectValuesInStackWithMultipleElements(){
                assertAll(
                        () -> assertFalse(minMaxStack.isEmpty()),
                        () -> assertEquals(5, minMaxStack.peek()),
                        () -> assertEquals(1, minMaxStack.getMin()),
                        () -> assertEquals(5, minMaxStack.getMax()),
                        () -> assertEquals(4, minMaxStack.size())
                );
            }

            @Test
            void testCorrectValuesAfterTwoPop(){
                int timesToPop = 2;
                drainStackForEachTimes(timesToPop);
                assertAll(
                        () -> assertFalse(minMaxStack.isEmpty()),
                        () -> assertEquals(4, minMaxStack.peek()),
                        () -> assertEquals(1, minMaxStack.getMin()),
                        () -> assertEquals(4, minMaxStack.getMax()),
                        () -> assertEquals(2, minMaxStack.size()
                ));
            }

            @Test
            void testWrongPopAfterDrainAllStack(){
                int timesToPop = 4;
                drainStackForEachTimes(timesToPop);
                assertAll(
                        () -> assertThrows(IllegalStateException.class, () -> minMaxStack.pop()),
                        () -> assertThrows(IllegalStateException.class, () -> minMaxStack.peek()),
                        () -> assertThrows(IllegalStateException.class, () -> minMaxStack.getMin()),
                        () -> assertThrows(IllegalStateException.class, () -> minMaxStack.getMax()),
                        () -> assertTrue(minMaxStack.isEmpty()),
                        () -> assertEquals(SIZE_OF_EMPTY_STACK, minMaxStack.size())
                );
            }
        }
    }
}