import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all account types.
 * Notice: balance is private. The ONLY way to change it is through
 * deposit()/withdraw(), and those validate first. That's encapsulation —
 * no outside code can ever set balance to an invalid value directly.
 */
public abstract class Account {

    private final String accountNumber;
    private final String name;
    private double balance;
    private final List<String> transactionHistory;

    public Account(String accountNumber, String name, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        logTransaction("Account opened with balance: " + initialDeposit);
    }

    // Every subclass MUST define its own withdraw rules.
    // SavingsAccount won't let you go below a minimum balance.
    // CurrentAccount allows going negative up to an overdraft limit.
    // Bank.java will call account.withdraw(amount) WITHOUT knowing
    // which subclass it's talking to — that's polymorphism.
    //
    // No more boolean return value — if withdraw() returns normally,
    // it succeeded. If it failed, it threw instead.
    public abstract void withdraw(double amount)
            throws InvalidAmountException, InsufficientFundsException;

    // Deposit rules are the same for every account type, so it lives
    // here in the base class instead of being repeated in each subclass.
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        balance += amount;
        logTransaction("Deposited: " + amount + " | New balance: " + balance);
        System.out.println("Deposit successful. New balance: " + balance);
    }

    protected void logTransaction(String entry) {
        transactionHistory.add(entry);
    }

    public void printTransactionHistory() {
        System.out.println("Transaction history for " + accountNumber + ":");
        for (String entry : transactionHistory) {
            System.out.println("  - " + entry);
        }
    }

    // Getters — no setters for accountNumber/name/balance on purpose.
    // Balance changes ONLY through deposit()/withdraw().
    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public double getBalance() { return balance; }

    // Subclasses use this to actually change balance after their
    // own validation passes.
    protected void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Balance: %.2f | Type: %s",
                accountNumber, name, balance, getClass().getSimpleName());
    }
}