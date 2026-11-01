import java.util.LinkedHashMap;
import java.util.Map;

public class StockIssue {

    /** Checked exception: requested more than the warehouse has; carries the shortfall. */
    static class OutOfStockException extends Exception {
        private final int shortfall;

        OutOfStockException(String item, int shortfall) {
            super("insufficient stock for " + item + ", short by " + shortfall + " unit(s).");
            this.shortfall = shortfall;
        }

        int getShortfall() {
            return shortfall;
        }
    }

    /** Checked exception: quantity must be greater than zero. */
    static class InvalidQuantityException extends Exception {
        InvalidQuantityException(int qty) {
            super("quantity must be > 0, but was " + qty + ".");
        }
    }

    static class Warehouse {
        private final Map<String, Integer> stock = new LinkedHashMap<>();

        Warehouse() {
            stock.put("Pen", 10);
            stock.put("Pencil", 8);
            stock.put("Notebook", 5);
            stock.put("Eraser", 12);
            stock.put("Marker", 6);
        }

        int stockOf(String item) {
            return stock.getOrDefault(item, 0);
        }

        void issue(String item, int qty) throws OutOfStockException, InvalidQuantityException {
            if (qty <= 0) {
                throw new InvalidQuantityException(qty);
            }
            int available = stockOf(item);
            if (qty > available) {
                throw new OutOfStockException(item, qty - available);
            }
            stock.put(item, available - qty);
            System.out.printf("OK       : issued %d x %s (remaining: %d)%n",
                    qty, item, available - qty);
        }
    }

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        String[] items = {"Pen", "Pencil", "Notebook", "Eraser", "Stapler", "Marker", "Pen"};
        int[] quantities = {5, 0, 20, 3, -2, 4, 10};

        for (int i = 0; i < items.length; i++) {
            try {
                warehouse.issue(items[i], quantities[i]);
            } catch (InvalidQuantityException e) {
                System.out.println("REJECTED : " + items[i] + " -> " + e.getMessage());
            } catch (OutOfStockException e) {
                System.out.println("REJECTED : " + items[i] + " -> " + e.getMessage()
                        + " [shortfall = " + e.getShortfall() + "]");
            }
        }

        System.out.println("\nFinal stock: " + warehouse.stock);
    }
}
