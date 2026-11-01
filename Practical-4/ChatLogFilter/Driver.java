import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        String[] logs = {
            "10:05 alice Hello there",
            "10:07 bob Hi Alice",
            "10:09 broken"                  // malformed: fewer than 3 parts
        };

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        System.out.print(ChatFilter.filter(logs, keyword));
        sc.close();
    }
}
