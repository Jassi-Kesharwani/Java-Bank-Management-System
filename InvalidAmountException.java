/**
 * Thrown when a deposit or withdrawal amount is zero or negative.
 * A checked exception (extends Exception, not RuntimeException) on purpose —
 * we WANT the compiler to force every caller to handle this.
 */
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}