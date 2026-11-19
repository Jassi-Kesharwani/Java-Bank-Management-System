import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Bank doesn't care HOW an account enforces its withdraw rules —
 * it just calls account.withdraw(amount) and trusts the subclass
 * to throw if something's wrong. That's the payoff of the Account design.
 */
public class Bank {

    private final Map<String, Account> accounts;
    private int nextAccountNumber;

    public Bank() {
        this.accounts = new HashMap<>();
        this.nextAccountNumber = 1001; // simple auto-incrementing account numbers
    }

    public Account createSavingsAccount(String name, double initialDeposit,
                                         double minBalance, double interestRate) {
        String accNo = "ACC" + nextAccountNumber++;
        Account account = new SavingsAccount(accNo, name, initialDeposit, minBalance, interestRate);
        accounts.put(accNo, account);
        System.out.println("Account created successfully: " + accNo);
        return account;
    }

    public Account createCurrentAccount(String name, double initialDeposit,
                                         double overdraftLimit) {
        String accNo = "ACC" + nextAccountNumber++;
        Account account = new CurrentAccount(accNo, name, initialDeposit, overdraftLimit);
        accounts.put(accNo, account);
        System.out.println("Account created successfully: " + accNo);
        return account;
    }

    // Throws instead of returning null — every caller is now FORCED by the
    // compiler to either handle "account doesn't exist" or declare that
    // they pass the problem upward. No more silent null-pointer landmines.
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return acc;
    }

    public void deposit(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException {
        Account acc = getAccount(accountNumber);
        acc.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        Account acc = getAccount(accountNumber);
        acc.withdraw(amount); // Bank doesn't know/care which subclass this is
    }

    public void transfer(String fromAccNo, String toAccNo, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        Account from = getAccount(fromAccNo);
        Account to = getAccount(toAccNo);

        // If withdraw() throws, this method exits immediately — execution
        // never reaches to.deposit(). Money can't vanish or get duplicated,
        // and we didn't need a single manual rollback check to guarantee that.
        from.withdraw(amount);
        to.deposit(amount);
        System.out.println("Transferred " + amount + " from " + fromAccNo + " to " + toAccNo);
    }

    public void printAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        System.out.println("All accounts:");
        for (Account acc : accounts.values()) {
            System.out.println("  " + acc);
        }
    }

    // One line per account: TYPE,accNo,name,balance,then type-specific fields.
    // The type tag at the start is what lets loadFromFile() know which
    // subclass to reconstruct.
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Account acc : accounts.values()) {
                String line;
                if (acc instanceof SavingsAccount) {
                    SavingsAccount sa = (SavingsAccount) acc;
                    line = String.join(",", "SAVINGS", acc.getAccountNumber(), acc.getName(),
                            String.valueOf(acc.getBalance()), String.valueOf(sa.getMinBalance()),
                            String.valueOf(sa.getInterestRate()));
                } else if (acc instanceof CurrentAccount) {
                    CurrentAccount ca = (CurrentAccount) acc;
                    line = String.join(",", "CURRENT", acc.getAccountNumber(), acc.getName(),
                            String.valueOf(acc.getBalance()), String.valueOf(ca.getOverdraftLimit()));
                } else {
                    continue; // unknown subclass — shouldn't happen, but don't crash if it does
                }
                writer.write(line);
                writer.newLine();
            }
        }
        System.out.println("Saved " + accounts.size() + " account(s) to " + filename);
    }

    public void loadFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No existing data file found (" + filename + "). Starting fresh.");
            return;
        }

        int highestSeen = 1000; // so nextAccountNumber picks up where we left off
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String type = parts[0];
                String accNo = parts[1];
                String name = parts[2];
                double balance = Double.parseDouble(parts[3]);

                Account acc;
                if (type.equals("SAVINGS")) {
                    double minBal = Double.parseDouble(parts[4]);
                    double rate = Double.parseDouble(parts[5]);
                    acc = new SavingsAccount(accNo, name, balance, minBal, rate);
                } else if (type.equals("CURRENT")) {
                    double overdraft = Double.parseDouble(parts[4]);
                    acc = new CurrentAccount(accNo, name, balance, overdraft);
                } else {
                    continue; // unrecognized line — skip rather than crash the whole load
                }
                accounts.put(accNo, acc);

                int num = Integer.parseInt(accNo.replace("ACC", ""));
                if (num > highestSeen) {
                    highestSeen = num;
                }
            }
        }
        nextAccountNumber = highestSeen + 1;
        System.out.println("Loaded " + accounts.size() + " account(s) from " + filename);
    }
}