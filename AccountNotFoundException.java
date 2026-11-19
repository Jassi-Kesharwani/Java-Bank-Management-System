/**
 * Thrown when Bank can't find an account for a given account number.
 * Replaces the old "return null and hope the caller checks" pattern.
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}