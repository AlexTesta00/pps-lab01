package it.unibo.pps.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The test suite for testing the SimpleBankAccount implementation
 */
class SimpleBankAccountTest {

    private final double INITIAL_BALANCE = 0;
    private final int POSITIVE_AMOUNT = 100;
    private final int NEGATIVE_AMOUNT = -100;
    private final int ANOTHER_BANK_ACCOUNT_ID = 2;

    private AccountHolder accountHolder;
    private BankAccount bankAccount;

    @BeforeEach
    void beforeEach(){
        String ACCOUNT_HOLDER_NAME = "Mario";
        String ACCOUNT_HOLDER_SURNAME = "Rossi";
        int ACCOUNT_HOLDER_ID = 1;
        accountHolder = new AccountHolder(ACCOUNT_HOLDER_NAME, ACCOUNT_HOLDER_SURNAME, ACCOUNT_HOLDER_ID);
        bankAccount = new SimpleBankAccount(accountHolder, INITIAL_BALANCE);
    }

    @Test
    void testInitialBalance() {
        assertEquals(INITIAL_BALANCE, bankAccount.getBalance());
    }

    @Nested
    class TestDeposit{

        @BeforeEach
        void setup(){
            bankAccount.deposit(accountHolder.id(), POSITIVE_AMOUNT);
        }

        @Test
        void testCorrectDepositWithPositiveAmount() {
            assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance());
        }

        @Test
        void testWrongDepositWithNegativeAmount() {
            assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(accountHolder.id(), NEGATIVE_AMOUNT));
        }

        @Test
        void testWrongDepositWithAnotherBankAccountId() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(ANOTHER_BANK_ACCOUNT_ID, POSITIVE_AMOUNT)),
                    () -> assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance())
            );
        }
    }

    @Nested
    class TestWithdraw{

        private final int NEGATIVE_AMOUNT_WITHDRAW = -70;
        private final int POSITIVE_AMOUNT_WITHDRAW = 70;

        @BeforeEach
        void setUp(){
            bankAccount.deposit(accountHolder.id(), POSITIVE_AMOUNT);
        }

        @Test
        void testCorrectWithdrawWithNegativeAmount() {
            System.out.println(bankAccount.getBalance());
            bankAccount.withdraw(accountHolder.id(), NEGATIVE_AMOUNT_WITHDRAW);
            assertEquals(POSITIVE_AMOUNT + NEGATIVE_AMOUNT_WITHDRAW, bankAccount.getBalance());
        }

        @Test
        void testWrongWithdrawWithPositiveAmount() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(accountHolder.id(), POSITIVE_AMOUNT_WITHDRAW)),
                    () -> assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance())
            );
        }

        @Test
        void testWrongWithdrawWithAnotherBankAccountId() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(ANOTHER_BANK_ACCOUNT_ID, NEGATIVE_AMOUNT_WITHDRAW)),
                    () -> assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance())
            );
        }
    }

    @Nested
    class TestBankAccountWithFee{
        private final int FEE = 1;
        private final int NEGATIVE_WITHDRAW = -70;
        private final int POSITIVE_WITHDRAW = -NEGATIVE_WITHDRAW;

        @BeforeEach
        void setUp(){
            bankAccount = new SimpleBankAccountWithFee(accountHolder, INITIAL_BALANCE, FEE);
            bankAccount.deposit(accountHolder.id(), POSITIVE_AMOUNT);
        }

        @Test
        void testCorrectWithdrawWithNegativeAmount() {
            bankAccount.withdraw(accountHolder.id(), NEGATIVE_WITHDRAW);
            assertEquals(POSITIVE_AMOUNT + NEGATIVE_WITHDRAW - FEE, bankAccount.getBalance());
        }

        @Test
        void testWrongWithdrawWithPositiveAmount() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(accountHolder.id(), POSITIVE_WITHDRAW)),
                    () -> assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance())
            );
        }

        @Test
        void testWrongWithdrawWithAnotherBankAccountId() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(ANOTHER_BANK_ACCOUNT_ID, NEGATIVE_AMOUNT)),
                    () -> assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance())
            );
        }

        @Test
        void testWrongWithDrawExceedsFee(){
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(accountHolder.id(), -POSITIVE_AMOUNT)),
                    () -> assertEquals(POSITIVE_AMOUNT, bankAccount.getBalance())
            );
        }
    }
}
