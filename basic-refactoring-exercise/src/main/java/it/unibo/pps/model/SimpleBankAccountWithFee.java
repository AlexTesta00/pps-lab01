package it.unibo.pps.model;

public class SimpleBankAccountWithFee implements BankAccount{

    private int fee;
    private SimpleBankAccount simpleBankAccount;

    public SimpleBankAccountWithFee(final AccountHolder accountHolder, final double balance, final int fee){
        this.simpleBankAccount = new SimpleBankAccount(accountHolder, balance);
        this.fee = fee;
    }
    @Override
    public double getBalance() {
        return this.simpleBankAccount.getBalance();
    }

    @Override
    public void deposit(int userID, double amount) {
        this.simpleBankAccount.deposit(userID, amount);
    }

    @Override
    public void withdraw(int userID, double amount) {
        this.simpleBankAccount.withdraw(userID, amount - this.fee);
    }
}
