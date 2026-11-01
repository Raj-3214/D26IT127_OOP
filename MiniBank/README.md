# MiniBank

Console banking application built with Java 17+.

## Compile and run

```bash
mkdir -p build
javac -d build $(find model util service exception -name '*.java' -print)
printf '8\n' | java -cp build service.MiniBank
```

The application saves accounts to `minibank-state.bin`, appends transactions under
`logs/`, and writes `end-of-day-report.txt` when the report menu option is selected.

## Build the runnable JAR

```bash
printf 'Manifest-Version: 1.0\nMain-Class: service.MiniBank\n\n' > manifest.mf
jar cfm MiniBank.jar manifest.mf -C build .
java -jar MiniBank.jar
```

## Menu

1. Open account
2. Deposit
3. Withdraw
4. Transfer
5. Generate report
6. List accounts
7. Process a sample concurrent batch
8. Exit and save

## Tests

`src/test/java/service/BankServiceTest.java` contains JUnit 5 tests for deposits,
withdrawal failures, concurrent deposits, and persistence. Run it with a JUnit 5
console launcher on the classpath.
