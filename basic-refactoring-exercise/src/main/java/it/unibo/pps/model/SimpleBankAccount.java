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
        if (!checkUser(userID)) {
            throw new IllegalArgumentException("The user is not the account holder");
        }
        if (!isAmountPositive(amount)) {
            throw new IllegalArgumentException("The amount must be positive");
        }
        this.balance += amount;
    }

    @Override
    public void withdraw(final int userID, final double amount) throws IllegalArgumentException {
        if (!checkUser(userID)) {
            throw new IllegalArgumentException("The user is not the account holder");
        }
        if (isAmountPositive(amount)) {
            throw new IllegalArgumentException("The amount must be negative");
        }
        if(!isWithdrawAllowed(amount)){
            throw new IllegalArgumentException("The balance is not sufficient for the withdrawal");
        }
        this.balance += amount;
    }

    private boolean isWithdrawAllowed(final double amount){
        return this.balance + amount >= 0;
    }

    private boolean isAmountPositive(final double amount){ return amount > 0; }

    private boolean checkUser(final int id) {
        return this.holder.id() == id;
    }
}
