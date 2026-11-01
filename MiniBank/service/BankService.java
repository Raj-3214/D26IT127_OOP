package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import util.ReportGenerator;
import util.StatePersister;
import util.TransactionLog;
import util.Validator;

public class BankService {
    private final Repository<Account> repository = new Repository<>();
    private final Path stateFile;
    private final Path logsFolder;
    private final Path reportFile;

    public BankService(Path stateFile, Path logsFolder, Path reportFile) {
        this.stateFile = stateFile;
        this.logsFolder = logsFolder;
        this.reportFile = reportFile;
        loadAccounts();
    }

    public Account openAccount(String ownerName, String email, String mobile, long openingBalance)
            throws InvalidAmountException {
        if (!Validator.isValidEmail(email) || !Validator.isValidMobile(mobile)) {
            throw new IllegalArgumentException("Invalid email or mobile");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        Account account = new Account(ownerName, openingBalance);
        repository.save(account);
        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws AccountNotFoundException, InvalidAmountException {
        Account account = requireAccount(accountNumber);
        account.deposit(amount);
        appendLog("DEPOSIT " + amount);
    }

    public void withdraw(String accountNumber, long amount)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
        Account account = requireAccount(accountNumber);
        account.withdraw(amount);
        appendLog("WITHDRAW " + amount);
    }

    public void transfer(String fromNumber, String toNumber, long amount)
            throws BankException {
        Account from = requireAccount(fromNumber);
        Account to = requireAccount(toNumber);
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
        appendLog("TRANSFER " + amount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        Account account = requireAccount(accountNumber);
        return "Account Statement\nAccount Number: " + account.getAccountNumber()
                + "\nOwner Name: " + account.getOwnerName()
                + "\nBalance: " + account.getBalance();
    }

    public ArrayList<Account> listAccounts() {
        return repository.findAll();
    }

    public List<String> processBatch(List<BatchTransaction> transactions) {
        TransactionProcessor processor = new TransactionProcessor();
        for (Account account : repository.findAll()) {
            processor.addAccount(account);
        }

        List<String> results = Collections.synchronizedList(new ArrayList<>());
        for (BatchTransaction transaction : transactions) {
            processor.submit(() -> {
                try {
                    Account account = processor.getAccount(transaction.accountNumber());
                    if (account == null) {
                        throw new AccountNotFoundException(
                                "Account not found: " + transaction.accountNumber());
                    }
                    if (transaction.type() == BatchType.DEPOSIT) {
                        account.deposit(transaction.amount());
                        appendLog("DEPOSIT " + transaction.amount());
                    } else {
                        account.withdraw(transaction.amount());
                        appendLog("WITHDRAW " + transaction.amount());
                    }
                    results.add("Completed " + transaction.type() + " for "
                            + transaction.accountNumber());
                } catch (BankException | IllegalArgumentException e) {
                    results.add("Failed " + transaction.type() + ": " + e.getMessage());
                }
            });
        }
        try {
            processor.stop();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            results.add("Batch interrupted");
        }
        return results;
    }

    public String generateReport() throws IOException {
        Files.createDirectories(logsFolder);
        return ReportGenerator.generate(logsFolder, reportFile);
    }

    public void save() throws IOException {
        Files.createDirectories(stateFile.toAbsolutePath().getParent());
        StatePersister.save(repository.findAll().toArray(Account[]::new), stateFile);
    }

    private Account requireAccount(String accountNumber) throws AccountNotFoundException {
        Account account = repository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    private void loadAccounts() {
        if (!Files.exists(stateFile)) {
            return;
        }
        try {
            repository.saveAll(StatePersister.load(stateFile));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load saved accounts", e);
        }
    }

    private void appendLog(String line) {
        try {
            Files.createDirectories(logsFolder);
            TransactionLog.append(logsFolder.resolve("transactions.log"), line);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write transaction log", e);
        }
    }

    public enum BatchType {
        DEPOSIT,
        WITHDRAW
    }

    public record BatchTransaction(BatchType type, String accountNumber, long amount) {
    }
}
