package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;

class BankServiceTest {
    @Test
    void depositIncreasesBalance() throws Exception {
        Account account = new Account("Aarav", 100);

        account.deposit(50);

        assertEquals(150, account.getBalance());
    }

    @Test
    void withdrawalOverLimitThrowsException() {
        Account account = new Account("Aarav", 100);

        assertThrows(InsufficientFundsException.class, () -> account.withdraw(101));
    }

    @Test
    void concurrentDepositsKeepBalanceCorrect() throws Exception {
        Account account = new Account("Aarav", 0);
        Thread[] workers = new Thread[10];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    try {
                        account.deposit(10);
                    } catch (InvalidAmountException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }

        assertEquals(10_000, account.getBalance());
    }

    @Test
    void servicePersistsAccounts() throws Exception {
        Path directory = Files.createTempDirectory("minibank-test");
        BankService service = new BankService(
                directory.resolve("state.bin"),
                directory.resolve("logs"),
                directory.resolve("report.txt"));
        service.openAccount("Aarav", "aarav@example.com", "9876543210", 100);
        service.save();

        BankService reloaded = new BankService(
                directory.resolve("state.bin"),
                directory.resolve("logs"),
                directory.resolve("report.txt"));

        assertEquals(1, reloaded.listAccounts().size());
    }
}
