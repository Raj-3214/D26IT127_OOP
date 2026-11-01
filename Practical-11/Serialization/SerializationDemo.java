import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * Practical 11 - Q1: Save/load objects with ObjectOutputStream / ObjectInputStream.
 *
 * The class below is named "Serializable" on purpose. It implements
 * java.io.Serializable using its fully-qualified name, because the simple name
 * "Serializable" is shadowed by the nested class itself.
 */
public class SerializationDemo {

    static class Serializable implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private final int id;
        private final String name;
        private transient String password; // transient -> NOT saved

        Serializable(int id, String name, String password) {
            this.id = id;
            this.name = name;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Serializable{id=" + id + ", name='" + name + "', password='" + password + "'}";
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String file = "objects.ser";

        Serializable[] original = {
                new Serializable(1, "Alice", "alice-secret"),
                new Serializable(2, "Bob", "bob-secret"),
                new Serializable(3, "Carol", "carol-secret")
        };

        System.out.println("Before saving:");
        Arrays.stream(original).forEach(o -> System.out.println("  " + o));

        // --- Save the whole array in one go ---
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(original);
        }
        System.out.println("\nSaved array to " + file);

        // --- Read it back into a fresh array of fresh objects ---
        Serializable[] restored;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            restored = (Serializable[]) in.readObject();
        }

        System.out.println("\nAfter loading:");
        Arrays.stream(restored).forEach(o -> System.out.println("  " + o));

        // --- Confirm the persistent values survived and the transient one did not ---
        boolean valuesSurvived = true;
        boolean transientLost = true;
        for (int i = 0; i < original.length; i++) {
            valuesSurvived &= original[i].id == restored[i].id
                    && original[i].name.equals(restored[i].name);
            transientLost &= restored[i].password == null;
        }

        System.out.println("\nid and name survived   : " + valuesSurvived);
        System.out.println("transient password lost: " + transientLost);
        System.out.println(valuesSurvived && transientLost ? "RESULT: PASS" : "RESULT: FAIL");
    }
}
