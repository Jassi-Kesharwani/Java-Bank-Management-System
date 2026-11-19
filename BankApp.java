import java.io.IOException;
import java.util.Scanner;

/**
 * Notice this class does almost nothing on its own — it just reads
 * input and calls into Bank. That's the goal: main() should be a thin
 * controller, not where the actual logic lives.
 *
 * Notice also: ONE try/catch wraps the whole menu now, instead of a
 * separate null-check inside every case like before. That's the real
 * payoff of exceptions over boolean/null returns — error handling moved
 * to ONE place instead of being repeated everywhere a method is called.
 */
public class BankApp {

    private static final String DATA_FILE = "accounts.txt";

    public static void main(String[] args) {
        Bank bank = new Bank();

        try {
            bank.loadFromFile(DATA_FILE);
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- BANK MENU ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. View Transaction History");
            System.out.println("7. View All Accounts");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline

            try {
                switch (choice) {
                    case 1: {
                        System.out.println("Account type: 1. Savings  2. Current");
                        System.out.print("Choose type: ");
                        int type = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Initial deposit: ");
                        double initial = sc.nextDouble();

                        if (type == 1) {
                            System.out.print("Minimum balance: ");
                            double minBal = sc.nextDouble();
                            System.out.print("Interest rate (%): ");
                            double rate = sc.nextDouble();
                            bank.createSavingsAccount(name, initial, minBal, rate);
                        } else if (type == 2) {
                            System.out.print("Overdraft limit: ");
                            double overdraft = sc.nextDouble();
                            bank.createCurrentAccount(name, initial, overdraft);
                        } else {
                            System.out.println("Invalid account type.");
                        }
                        break;
                    }
                    case 2: {
                        System.out.print("Account number: ");
                        String accNo = sc.nextLine();
                        System.out.print("Amount: ");
                        double amt = sc.nextDouble();
                        bank.deposit(accNo, amt);
                        break;
                    }
                    case 3: {
                        System.out.print("Account number: ");
                        String accNo = sc.nextLine();
                        System.out.print("Amount: ");
                        double amt = sc.nextDouble();
                        bank.withdraw(accNo, amt);
                        break;
                    }
                    case 4: {
                        System.out.print("From account: ");
                        String from = sc.nextLine();
                        System.out.print("To account: ");
                        String to = sc.nextLine();
                        System.out.print("Amount: ");
                        double amt = sc.nextDouble();
                        bank.transfer(from, to, amt);
                        break;
                    }
                    case 5: {
                        System.out.print("Account number: ");
                        String accNo = sc.nextLine();
                        Account acc = bank.getAccount(accNo);
                        System.out.println("Balance: " + acc.getBalance());
                        break;
                    }
                    case 6: {
                        System.out.print("Account number: ");
                        String accNo = sc.nextLine();
                        Account acc = bank.getAccount(accNo);
                        acc.printTransactionHistory();
                        break;
                    }
                    case 7:
                        bank.printAllAccounts();
                        break;
                    case 8:
                        running = false;
                        bank.saveToFile(DATA_FILE);
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (AccountNotFoundException | InvalidAmountException
                     | InsufficientFundsException | IOException e) {
                // All custom exceptions AND IOException (from saveToFile) land here.
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }
}