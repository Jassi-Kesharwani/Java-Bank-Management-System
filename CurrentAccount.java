/**
 * A CurrentAccount allows the balance to go negative, but only down to
 * -overdraftLimit. Compare this to SavingsAccount's rule: balance can't
 * go BELOW minBalance. Same shape of rule, opposite direction — that's
 * exactly why this belongs in withdraw(), not somewhere shared.
 */
public class CurrentAccount extends Account {

    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, String name, double initialDeposit,
                           double overdraftLimit) {
        super(accountNumber, name, initialDeposit);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (getBalance() - amount < -overdraftLimit) {
            throw new InsufficientFundsException("Withdrawal denied: would exceed overdraft limit of "
                    + overdraftLimit + ".");
        }
        setBalance(getBalance() - amount);
        logTransaction("Withdrew: " + amount + " | New balance: " + getBalance());
        System.out.println("Withdrawal successful. New balance: " + getBalance());
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}