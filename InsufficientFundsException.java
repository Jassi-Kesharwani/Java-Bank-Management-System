/**
 * Thrown when a withdrawal would push the balance below what's allowed —
 * below minBalance for SavingsAccount, or below -overdraftLimit for
 * CurrentAccount. Each subclass decides WHEN to throw this; the exception
 * itself doesn't need to know which rule was broken.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}