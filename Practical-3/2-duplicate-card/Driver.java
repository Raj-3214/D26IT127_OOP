public class Driver {
    public static void main(String[] args) {
        Card[] cards = new Card[10];
        int count = 0;

        Card[] incoming = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Ace", "Spades")
        };

        for (Card card : incoming) {
            boolean duplicate = false;

            for (int i = 0; i < count; i++) {
                if (cards[i].equals(card)) {
                    duplicate = true;
                    break;
                }
            }

            if (duplicate) {
                System.out.println("Duplicate found: " + card);
                break;
            }

            cards[count++] = card;
        }
    }
}
