// Practical 13.1 - Generic containers
// One file, one public class; Box and Pair are nested generic classes.

public class GenericContainers {

    // Generic container: Box<T>
    static class Box<T> {
        private T value;

        Box(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }

        void set(T value) {
            this.value = value;
        }
    }

    // Generic container: Pair<A, B>
    static class Pair<A, B> {
        private final A first;
        private final B second;

        Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        A first() {
            return first;
        }

        B second() {
            return second;
        }
    }

    // Generic method: returns the first element of any array
    static <T> T first(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        return array[0];
    }

    public static void main(String[] args) {
        // Box used with different types - get()/set() need no casts
        Box<String> name = new Box<>("Rushang");
        Box<Integer> age = new Box<>(21);

        String studentName = name.get();   // no cast
        int studentAge = age.get();        // no cast (auto-unboxing)
        System.out.println("Box<String>  : " + studentName);
        System.out.println("Box<Integer> : " + studentAge);

        name.set("Rushang Patel");
        age.set(22);
        System.out.println("After set()  : " + name.get() + ", " + age.get());

        // Box is type-safe at compile time:
        // name.set(100);  // COMPILE ERROR: int cannot be converted to String

        // Pair used with different types - accessors need no casts
        Pair<String, Double> subjectMarks = new Pair<>("Maths", 92.5);
        String subject = subjectMarks.first();  // no cast
        double marks = subjectMarks.second();   // no cast
        System.out.println("Pair<String, Double>  : " + subject + " = " + marks);

        Pair<Integer, Boolean> answer = new Pair<>(7, true);
        int number = answer.first();            // no cast
        boolean correct = answer.second();      // no cast
        System.out.println("Pair<Integer, Boolean>: " + number + " -> " + correct);

        // Generic method works with any reference-type array
        String[] names = {"Amit", "Bhavna", "Chirag"};
        Integer[] numbers = {10, 20, 30};
        Double[] prices = {19.99, 5.50};

        System.out.println("first(String[]) : " + first(names));
        System.out.println("first(Integer[]): " + first(numbers));
        System.out.println("first(Double[]) : " + first(prices));
    }
}
