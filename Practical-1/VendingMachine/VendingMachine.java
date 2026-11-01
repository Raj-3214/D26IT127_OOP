import java.util.Scanner;

public class VendingMachine {
    enum Coin {
        ONE, TWO, FIVE, TEN
    }

    public static void main(String[] args) {
        final int PRICE = 15;
        int total = 0;
        Scanner scanner = new Scanner(System.in);

        while (total < PRICE) {
            String input = scanner.next();
            Coin coin;

            try {
                coin = Coin.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid coin: " + input);
                continue;
            }

            total += switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            System.out.println("Total so far: " + total);
        }

        System.out.println("Paid. Change: " + (total - PRICE));
        scanner.close();
    }
}
