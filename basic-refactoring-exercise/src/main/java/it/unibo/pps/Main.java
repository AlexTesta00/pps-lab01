package it.unibo.pps;

import it.unibo.pps.model.AccountHolder;
import it.unibo.pps.model.BankAccount;
import it.unibo.pps.model.SimpleBankAccount;

public class Main {

    public static void main(String[] args) {
        final AccountHolder accountHolder = new AccountHolder("Mario", "Rossi", 1);
        final BankAccount bankAccount = new SimpleBankAccount(accountHolder, 0);
        bankAccount.deposit(accountHolder.id(), 100);
        System.out.println("Current balance is " + bankAccount.getBalance());
        bankAccount.withdraw(accountHolder.id(), -30);
        System.out.println("Current balance is " + bankAccount.getBalance());
        bankAccount.withdraw(accountHolder.id(), -80);
        System.out.println("Current balance is " + bankAccount.getBalance());
    }
}
