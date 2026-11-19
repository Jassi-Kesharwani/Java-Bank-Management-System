# Java Console Banking System

A lightweight command-line banking application written in Core Java. Built to manage customer accounts, process daily transactions, and store records locally without relying on external databases or third-party libraries.

## What It Does
- Opens **Savings** (minimum balance enforced) and **Current** (overdraft enabled) accounts with auto-generated IDs (`ACC1001`, `ACC1002`, etc.).
- Handles deposits, withdrawals, balance checks, and account-to-account fund transfers.
- Logs all session transactions per account for quick statement viewing.
- Saves account states to `accounts.txt` on exit and reloads them automatically when restarted.
- Validates user inputs and banking rules through custom exceptions (`InvalidAmountException`, `InsufficientFundsException`, `AccountNotFoundException`).

## Project Files
- `BankApp.java` — Main class and CLI menu loop.
- `Bank.java` — Core service handling account storage (`HashMap`), transfers, and file I/O.
- `Account.java` — Abstract base class defining shared account properties and transaction logs.
- `SavingsAccount.java` / `CurrentAccount.java` — Subclasses implementing account-specific withdrawal rules.
- `InvalidAmountException.java` / `InsufficientFundsException.java` / `AccountNotFoundException.java` — Custom exception classes.
- `accounts.txt` — Flat-file storage for persisted account data.

## Run Locally
Make sure **JDK 8+** is installed, then compile and run from the project folder:

```bash
javac *.java
java BankApp
```