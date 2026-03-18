package it.unibo.pps.tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmartDoorLockTest {

    private final int MAX_ATTEMPTS = 3;
    private final int NO_FAILED_ATTEMPTS = 0;

    final int PIN = 1234;

    private SmartDoorLock smartDoorLock;

    @BeforeEach
    void setUp() {
        this.smartDoorLock = new SmartDoorLockImpl(MAX_ATTEMPTS);
    }

    private void assertLockIsInInitialState() {
        assertAll(
                () -> assertFalse(smartDoorLock.isLocked()),
                () -> assertFalse(smartDoorLock.isBlocked()),
                () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getMaxAttempts()),
                () -> assertEquals(NO_FAILED_ATTEMPTS, smartDoorLock.getFailedAttempts())
        );
    }

    @Test
    void testCorrectInitSettings(){
        assertLockIsInInitialState();
    }

    private void assertCannotLockWithoutPin() {
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> smartDoorLock.lock()),
                this::assertLockIsInInitialState
        );
    }

    private void assertLockedState() {
        assertAll(
                () -> assertTrue(smartDoorLock.isLocked()),
                () -> assertFalse(smartDoorLock.isBlocked()),
                () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getMaxAttempts()),
                () -> assertEquals(NO_FAILED_ATTEMPTS, smartDoorLock.getFailedAttempts())
        );
    }

    @Test
    void testWrongLockDoorIfPinIsUnsetted(){
        assertCannotLockWithoutPin();
    }

    @Test
    void canSetPin(){
        assertDoesNotThrow(() -> smartDoorLock.setPin(PIN));
    }

    @Nested
    class TestWithPinSet {

        final int UNCORRECT_PIN = 4321;

        @BeforeEach
        void setPin() {
            smartDoorLock.setPin(PIN);
        }

        @Test
        void testLockWhenAlreadyLocked() {
            smartDoorLock.lock();
            assertDoesNotThrow(() -> smartDoorLock.lock());
        }

        @Test
        void testCorrectLockDoor(){
            smartDoorLock.lock();
            assertAll(
                    () -> assertTrue(smartDoorLock.isLocked()),
                    () -> assertFalse(smartDoorLock.isBlocked()),
                    () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getMaxAttempts()),
                    () -> assertEquals(NO_FAILED_ATTEMPTS, smartDoorLock.getFailedAttempts())
            );
        }

        @Test
        void testCorrectUnlockDoor(){
            smartDoorLock.unlock(PIN);
            assertAll(
                    () -> assertFalse(smartDoorLock.isLocked()),
                    () -> assertFalse(smartDoorLock.isBlocked()),
                    () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getMaxAttempts()),
                    () -> assertEquals(NO_FAILED_ATTEMPTS, smartDoorLock.getFailedAttempts())
            );
        }

        @Test
        void testFailedAttemptsIncreaseCorrectly(){
            for (int attempts = 1; attempts <= MAX_ATTEMPTS; attempts++) {
                smartDoorLock.unlock(UNCORRECT_PIN);
                assertEquals(attempts, smartDoorLock.getFailedAttempts());
            }

            smartDoorLock.unlock(UNCORRECT_PIN);
            assertEquals(3, smartDoorLock.getFailedAttempts());
            assertTrue(smartDoorLock.isBlocked());
        }

        @Test
        void testCorrectUnlockAfterTwoFailedAttempts(){
            int numberOfFailedAttempts = 2;
            for (int attempts = 0; attempts < numberOfFailedAttempts; attempts++) {
                smartDoorLock.unlock(UNCORRECT_PIN);
            }
            smartDoorLock.unlock(PIN);
            assertAll(
                    () -> assertFalse(smartDoorLock.isLocked()),
                    () -> assertFalse(smartDoorLock.isBlocked()),
                    () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getMaxAttempts()),
                    () -> assertEquals(numberOfFailedAttempts, smartDoorLock.getFailedAttempts())
            );
        }

        @Nested
        class TestWithBlockedDoorLock {

            @BeforeEach
            void blockDoorLock() {
                for (int i = 0; i <= MAX_ATTEMPTS; i++) {
                    smartDoorLock.unlock(UNCORRECT_PIN);
                }
            }

            @Test
            void testCorrectBlockedDoorLock() {
                assertAll(
                        () -> assertFalse(smartDoorLock.isLocked()),
                        () -> assertTrue(smartDoorLock.isBlocked()),
                        () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getMaxAttempts()),
                        () -> assertEquals(MAX_ATTEMPTS, smartDoorLock.getFailedAttempts())
                );
            }

            @Test
            void testWrongOperationWithDoorBlocked(){
                assertAll(
                        () -> assertThrows(IllegalStateException.class, () -> smartDoorLock.setPin(PIN)),
                        () -> assertThrows(IllegalStateException.class, () -> smartDoorLock.lock()),
                        () -> assertDoesNotThrow(() -> smartDoorLock.unlock(PIN)),
                        () -> assertFalse(smartDoorLock.isLocked()),
                        () -> assertTrue(smartDoorLock.isBlocked())
                );
            }

            @Nested
            class TestResetBlockedDoorLock {

                private void setPin() {
                    smartDoorLock.setPin(PIN);
                }

                @BeforeEach
                void resetDoorLock() {
                    smartDoorLock.reset();
                }

                @Test
                void testWrongLockBecausePinIsUnsetted(){
                    assertCannotLockWithoutPin();
                }

                @Test
                void testCorrectLockDoorAfterReset(){
                    setPin();
                    smartDoorLock.lock();
                    assertLockedState();
                }
            }
        }
    }
}
