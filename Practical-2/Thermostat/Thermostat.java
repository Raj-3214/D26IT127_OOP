public class Thermostat {
    private String location;
    private int temperature;
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;

    public Thermostat(String location, int startTemp) {
        this.location = location;
        this.temperature = (startTemp >= MIN && startTemp <= MAX) ? startTemp : 22;
        activeCount++;
    }

    public Thermostat(String location) {
        this(location, 22);
    }

    public void raise() {
        if (temperature < MAX) {
            temperature++;
        } else {
            System.out.println("Already at maximum (30)");
        }
    }

    public void lower() {
        if (temperature > MIN) {
            temperature--;
        } else {
            System.out.println("Already at minimum (16)");
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public static int getActiveCount() {
        return activeCount;
    }

    public static void main(String[] args) {
        Thermostat livingRoom = new Thermostat("Living Room", 22);
        Thermostat bedroom = new Thermostat("Bedroom", 35); // Invalid, so defaults to 22

        for (int i = 0; i < 10; i++) {
            livingRoom.raise();
            System.out.println("Temperature: " + livingRoom.getTemperature());
        }

        for (int i = 0; i < 20; i++) {
            livingRoom.lower();
            System.out.println("Temperature: " + livingRoom.getTemperature());
        }

        System.out.println("Active thermostats: " + Thermostat.getActiveCount());
    }
}
