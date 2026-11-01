public class Driver {

    public static void main(String[] args) {
        String[] passwords = {
            "abc",
            "abcdefgh",
            "Abcd1234",
            "Abcd1234!",
            "abcd1234!",
            "Password1"
        };

        for (String pw : passwords) {
            System.out.println("Password: \"" + pw + "\"");
            System.out.println("  " + PasswordChecker.report(pw));
            System.out.println("  Strength: " + PasswordChecker.strength(pw));
            System.out.println();
        }
    }
}
