package it.unibo.pps.tdd;

public class SmartDoorLockImpl implements SmartDoorLock {

    private static final int INITIAL_ATTEMPTS = 0;
    private static final int INITIAL_PIN_VALUE = 0000;
    final int maxAttempts;
    int pin;
    int failedAttempts;
    SmartDoorLockState state;

    public SmartDoorLockImpl(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        this.pin = INITIAL_PIN_VALUE;
        this.state = SmartDoorLockState.UNLOCKED;
        this.failedAttempts = INITIAL_ATTEMPTS;
    }

    @Override
    public void setPin(int pin) {
        this.ensureUnlockedOrThrowException();
        this.pin = pin;
    }

    @Override
    public void unlock(int pin) {
        if (haveReachedMaxAttempts()) {
            this.state = SmartDoorLockState.BLOCKED;
            return;
        }

        if (isPinCorrect(pin)) {
            this.state = SmartDoorLockState.UNLOCKED;
            return;
        }

        this.failedAttempts++;
    }

    @Override
    public void lock() {
        isPinSetOrThrowException();
        isDoorBlockedOrThrowException();
        this.state = SmartDoorLockState.LOCKED;
    }

    @Override
    public boolean isLocked() {
        return this.state == SmartDoorLockState.LOCKED;
    }

    @Override
    public boolean isBlocked() {
        return this.state == SmartDoorLockState.BLOCKED;
    }

    @Override
    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    @Override
    public int getFailedAttempts() {
        return this.failedAttempts;
    }

    @Override
    public void reset() {
        this.state = SmartDoorLockState.UNLOCKED;
        this.failedAttempts = INITIAL_ATTEMPTS;
        this.pin = INITIAL_PIN_VALUE;
    }

    private boolean haveReachedMaxAttempts() {
        return this.failedAttempts >= this.maxAttempts;
    }

    private boolean isPinCorrect(int pin){
        return this.pin == pin;
    }

    private void isPinSetOrThrowException(){
        if(this.pin == INITIAL_PIN_VALUE){
            throw new IllegalStateException("Pin is unsetted");
        }
    }

    private void isDoorBlockedOrThrowException(){
        if(this.isBlocked()){
            throw new IllegalStateException("Door is blocked");
        }
    }

    private void ensureUnlockedOrThrowException() {
        if (this.state != SmartDoorLockState.UNLOCKED) {
            throw new IllegalStateException("Cannot set pin after locking");
        }
    }
}
