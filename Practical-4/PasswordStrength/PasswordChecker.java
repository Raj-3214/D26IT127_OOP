import java.util.regex.Pattern;

public class PasswordChecker {

    private static final Pattern UPPER   = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT   = Pattern.compile(".*[0-9].*");
    private static final Pattern SPECIAL = Pattern.compile(".*[^A-Za-z0-9].*");

    // Rule 1: length >= 8
    public static boolean hasLength(String pw) {
        return pw.length() >= 8;
    }

    // Rule 2: contains an uppercase letter
    public static boolean hasUpper(String pw) {
        return UPPER.matcher(pw).matches();
    }

    // Rule 3: contains a digit
    public static boolean hasDigit(String pw) {
        return DIGIT.matcher(pw).matches();
    }

    // Rule 4: contains a special character
    public static boolean hasSpecial(String pw) {
        return SPECIAL.matcher(pw).matches();
    }

    // Count how many rules pass: 0-1 Weak, 2-3 Medium, 4 Strong
    public static String strength(String pw) {
        int passed = 0;
        if (hasLength(pw))  passed++;
        if (hasUpper(pw))   passed++;
        if (hasDigit(pw))   passed++;
        if (hasSpecial(pw)) passed++;

        if (passed == 4) return "Strong";
        if (passed >= 2) return "Medium";
        return "Weak";
    }

    // Which rules passed
    public static String report(String pw) {
        return "Length>=8: " + hasLength(pw)
             + ", Uppercase: " + hasUpper(pw)
             + ", Digit: " + hasDigit(pw)
             + ", Special: " + hasSpecial(pw);
    }
}
