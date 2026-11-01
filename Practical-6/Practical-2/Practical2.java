public class Practical2 {

    // ---------- Functional interface ----------
    @FunctionalInterface
    interface Notifier {
        void send(String message);
    }

    // ---------- Marker interface ----------
    interface Urgent {
    }

    // A lambda cannot implement a second (marker) interface, so wrap it.
    static class UrgentNotifier implements Notifier, Urgent {
        private final Notifier delegate;

        UrgentNotifier(Notifier delegate) {
            this.delegate = delegate;
        }

        @Override
        public void send(String message) {
            delegate.send(message);
        }
    }

    static void broadcast(Notifier[] senders, String message) {
        for (Notifier sender : senders) {
            sender.send(message);
            if (sender instanceof Urgent) {
                sender.send(message); // urgent senders receive it twice
            }
        }
    }

    public static void main(String[] args) {
        Notifier email = message -> System.out.println("[Email] " + message);
        Notifier sms = message -> System.out.println("[SMS]   " + message);

        Notifier[] senders = {
                email,                      // normal sender
                new UrgentNotifier(sms)     // SMS is marked urgent
        };

        broadcast(senders, "Server is down. Please check immediately.");
    }
}
