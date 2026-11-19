/**
 * A SavingsAccount can't be withdrawn from below its minimum balance.
 * This is the ONE thing that makes it different from a generic account —
 * everything else (deposit, history, getters) it inherits for free.
 */
public class SavingsAccount extends Account {

    private final double minBalance;
    private final double interestRate; // not used yet — wire this up later

    public SavingsAccount(String accountNumber, String name, double initialDeposit,
                           double minBalance, double interestRate) {
        super(accountNumber, name, initialDeposit);
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (getBalance() - amount < minBalance) {
            throw new InsufficientFundsException("Withdrawal denied: balance can't go below minimum ("
                    + minBalance + ") for a savings account.");
        }
        setBalance(getBalance() - amount);
        logTransaction("Withdrew: " + amount + " | New balance: " + getBalance());
        System.out.println("Withdrawal successful. New balance: " + getBalance());
    }

    public double getMinBalance() {
        return minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }
}