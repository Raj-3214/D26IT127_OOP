public class Practical1 {

    // ---------- Switchable contract ----------
    interface Switchable {
        void on();

        void off();

        boolean isOn();

        default void toggle() {
            if (isOn()) {
                off();
            } else {
                on();
            }
        }
    }

    // ---------- Implementations ----------
    static class Fan implements Switchable {
        private boolean on;

        @Override
        public void on() {
            on = true;
        }

        @Override
        public void off() {
            on = false;
        }

        @Override
        public boolean isOn() {
            return on;
        }

        @Override
        public String toString() {
            return "Fan";
        }
    }

    static class Light implements Switchable {
        private boolean on;

        @Override
        public void on() {
            on = true;
        }

        @Override
        public void off() {
            on = false;
        }

        @Override
        public boolean isOn() {
            return on;
        }

        @Override
        public String toString() {
            return "Light";
        }
    }

    // ---------- Functional interface: may this device switch on at this hour? ----------
    @FunctionalInterface
    interface SwitchRule {
        boolean canSwitchOn(Switchable device, int hour);
    }

    public static void main(String[] args) {
        Switchable[] devices = { new Fan(), new Light() };

        System.out.println("--- Toggling all devices ---");
        for (Switchable device : devices) {
            device.toggle();
            System.out.println(device + " is now " + (device.isOn() ? "ON" : "OFF"));
        }

        // Rule supplied once as an anonymous class.
        SwitchRule daytimeOnly = new SwitchRule() {
            @Override
            public boolean canSwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour < 18;
            }
        };

        // Rule supplied once as a lambda.
        SwitchRule nightLightOnly = (device, hour) ->
                !(device instanceof Light) || hour >= 18 || hour < 6;

        int hour = 20;
        System.out.println("\n--- Rules at " + hour + ":00 ---");
        for (Switchable device : devices) {
            System.out.println(device + " | anonymous daytime rule: "
                    + daytimeOnly.canSwitchOn(device, hour));
            System.out.println(device + " | lambda night-light rule: "
                    + nightLightOnly.canSwitchOn(device, hour));
        }
    }
}
