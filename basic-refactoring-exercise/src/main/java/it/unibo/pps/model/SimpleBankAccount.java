package it.unibo.pps.model;

/**
 * This class represent a particular instance of a BankAccount.
 * In particular, a Simple Bank Account allows always the deposit
 * while the withdrawal is allowed only if the balance greater or equal the withdrawal amount
 */
public class SimpleBankAccount implements BankAccount {

    private double balance;
    private final AccountHolder holder;

    public SimpleBankAccount(final AccountHolder holder, final double balance) {
        this.holder = holder;
        this.balance = balance;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void deposit(final int userID, final double amount) throws IllegalArgumentException {
        this.operation(userID, amount, () -> { isAmountPositiveOrThrow(amount); });
    }

    @Override
    public void withdraw(final int userID, final double amount) throws IllegalArgumentException {
        this.operation(userID, amount, () -> {
            isNegativeAmountOrThrow(amount);
            isWithdrawAllowedOrThrow(amount);
        });
    }

    private void operation(final int userID, final double amount, final Runnable checks){
        checkUserOrThrow(userID);
        checks.run();
        updateBalance(amount);
    }

    private void isWithdrawAllowedOrThrow(final double amount) throws IllegalArgumentException {
        if(!(this.balance + amount >= 0)){
            throw new IllegalArgumentException("The balance is not sufficient for the withdrawal");
        }
    }

    private void isAmountPositiveOrThrow(final double amount) throws IllegalArgumentException{
        if (!(amount > 0)) {
            throw new IllegalArgumentException("The amount must be positive");
        }
    }

    private void isNegativeAmountOrThrow(final double amount) throws IllegalArgumentException {
        if (!(amount < 0)) {
            throw new IllegalArgumentException("The amount must be negative");
        }
    }

    private void checkUserOrThrow(final int id) throws IllegalArgumentException {
        if(this.holder.id() != id){
            throw new IllegalArgumentException("The user is not the account holder");
        }
    }

    private void updateBalance(final double amount) {
        this.balance += amount;
    }
}
