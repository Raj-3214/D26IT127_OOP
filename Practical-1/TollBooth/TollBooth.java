import java.util.Scanner;

public class TollBooth {
    record Vehicle(String number, String type) {
    }

    public static void main(String[] args) {
        int totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String number = scanner.next();
            if (number.equalsIgnoreCase("done")) {
                break;
            }

            String type = scanner.next().toLowerCase();
            Vehicle vehicle = new Vehicle(number, type);

            int toll = switch (vehicle.type()) {
                case "bike" -> 20;
                case "car" -> 50;
                case "truck" -> 150;
                default -> 0;
            };

            totalToll += toll;

            switch (vehicle.type()) {
                case "bike" -> bikeCount++;
                case "car" -> carCount++;
                case "truck" -> truckCount++;
            }
        }

        String mostFrequent = "bike";
        int highestCount = bikeCount;

        if (carCount > highestCount) {
            mostFrequent = "car";
            highestCount = carCount;
        }
        if (truckCount > highestCount) {
            mostFrequent = "truck";
            highestCount = truckCount;
        }
        if (highestCount == 0) {
            mostFrequent = "none";
        }

        System.out.println("Total toll: " + totalToll);
        System.out.println("Most frequent: " + mostFrequent);
        scanner.close();
    }
}
