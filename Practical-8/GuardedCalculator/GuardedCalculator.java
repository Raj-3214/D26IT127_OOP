import java.util.Scanner;

public class GuardedCalculator {

    /** Custom checked exception for division by zero. */
    static class DivideByZeroException extends Exception {
        DivideByZeroException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int attempt = 0;
        boolean done = false;

        while (!done) {
            attempt++;
            try {
                System.out.print("Enter first number : ");
                double a = readNumber(sc);

                System.out.print("Enter operator (+ - * /): ");
                char op = readOperator(sc);

                System.out.print("Enter second number: ");
                double b = readNumber(sc);

                double result = calculate(a, op, b);
                System.out.printf("Result: %.2f %c %.2f = %.2f%n", a, op, b, result);
                done = true;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: not a valid number -> " + e.getMessage());
            } catch (DivideByZeroException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            } finally {
                System.out.println("[log] Attempt " + attempt + " finished.");
                System.out.println();
            }
        }
        sc.close();
    }

    private static double readNumber(Scanner sc) {
        // Parsing the whole line makes "abc" fail here instead of leaving
        // stray tokens in the Scanner for the next prompt.
        return Double.parseDouble(sc.nextLine().trim());
    }

    private static char readOperator(Scanner sc) {
        String token = sc.nextLine().trim();
        if (token.length() != 1 || "+-*/".indexOf(token.charAt(0)) < 0) {
            throw new IllegalArgumentException("'" + token + "' is not a valid operator (use + - * /).");
        }
        return token.charAt(0);
    }

    private static double calculate(double a, char op, double b) throws DivideByZeroException {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            default: // '/'
                if (b == 0) {
                    throw new DivideByZeroException("cannot divide " + a + " by zero.");
                }
                return a / b;
        }
    }
}
