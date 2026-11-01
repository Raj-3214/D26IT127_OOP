// Practical 13.2 - Bounded type parameters
// One file, one class, one generic method.

public class BoundedStats {

    // N must be a Number (or a subclass of Number)
    static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        double sum = 0;
        for (N value : values) {
            sum += value.doubleValue();   // safe: N is guaranteed to be a Number
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        Integer[] integers = {10, 20, 30, 40};
        Double[] doubles = {1.5, 2.5, 3.0};
        Long[] longs = {100L, 200L, 300L};

        System.out.printf("average(Integer[]): %.2f%n", average(integers));
        System.out.printf("average(Double[]) : %.2f%n", average(doubles));
        System.out.printf("average(Long[])   : %.2f%n", average(longs));

        /*
         * Why non-Number types are refused:
         *
         *   String[] words = {"a", "b"};
         *   average(words);  // COMPILE ERROR
         *
         * The bound <N extends Number> restricts N to Number and its subclasses
         * (Integer, Double, Long, Float, Short, Byte, BigInteger, BigDecimal...).
         * The compiler therefore knows every N has doubleValue(), so the method
         * can compute the sum without any cast.
         *
         * String, Character, Boolean, arrays, etc. do NOT extend Number, so they
         * fail the bound at compile time. Even Object[] would be rejected even
         * though it could hold Numbers, because the declared element type is not
         * within the bound. This makes the method type-safe: there is no way to
         * pass a type that cannot produce a numeric value.
         */
    }
}
