package service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;

public class MiniBank {
    public static void main(String[] args) {
        Path stateFile = Path.of("minibank-state.bin");
        BankService service = new BankService(
                stateFile, Path.of("logs"), Path.of("end-of-day-report.txt"));

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                displayMenu();
                if (!scanner.hasNextInt()) {
                    System.out.println("Enter a number from 1 to 8.");
                    scanner.next();
                    continue;
                }

                int option = scanner.nextInt();
                try {
                    switch (option) {
                        case 1 -> openAccount(scanner, service);
                        case 2 -> service.deposit(readText(scanner, "Account number: "),
                                readAmount(scanner, "Deposit amount: "));
                        case 3 -> service.withdraw(readText(scanner, "Account number: "),
                                readAmount(scanner, "Withdrawal amount: "));
                        case 4 -> service.transfer(readText(scanner, "From account: "),
                                readText(scanner, "To account: "),
                                readAmount(scanner, "Transfer amount: "));
                        case 5 -> System.out.println(service.generateReport());
                        case 6 -> service.listAccounts().forEach(System.out::println);
                        case 7 -> runBatch(service);
                        case 8 -> running = false;
                        default -> throw new IllegalArgumentException("Invalid menu choice");
                    }
                } catch (BankException | IOException | IllegalArgumentException e) {
                    System.out.println("Operation failed: " + e.getMessage());
                } finally {
                    if (option != 8) {
                        System.out.println("Operation finished.");
                    }
                }
            }
        }

        try {
            service.save();
            System.out.println("Accounts saved.");
        } catch (IOException e) {
            System.out.println("Unable to save accounts: " + e.getMessage());
        }
    }

    private static void runBatch(BankService service) {
        List<BankService.BatchTransaction> transactions = List.of(
                new BankService.BatchTransaction(BankService.BatchType.DEPOSIT, "AC0001", 50),
                new BankService.BatchTransaction(BankService.BatchType.WITHDRAW, "AC0001", 25));
        service.processBatch(transactions).forEach(System.out::println);
    }

    private static void openAccount(Scanner scanner, BankService service)
            throws InvalidAmountException {
        String owner = readText(scanner, "Owner name: ");
        String email = readText(scanner, "Email: ");
        String mobile = readText(scanner, "Mobile: ");
        long balance = readAmount(scanner, "Opening balance: ");
        System.out.println("Opened " + service.openAccount(owner, email, mobile, balance));
    }

    private static String readText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }

    private static long readAmount(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLong()) {
            scanner.next();
            throw new IllegalArgumentException("Amount must be a whole number");
        }
        return scanner.nextLong();
    }

    private static void displayMenu() {
        System.out.println("\n1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Report");
        System.out.println("6. List accounts");
        System.out.println("7. Process sample batch");
        System.out.println("8. Exit");
        System.out.print("Choose an option: ");
    }
}
