import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Practical 12 - Q2: Unique visitors using HashSet and LinkedHashSet.
 *
 * Takes a list of login names (with repeats) and:
 *   1. uses a HashSet to count distinct users,
 *   2. uses a LinkedHashSet to keep the first-seen order,
 *   3. prints the unique count and the first three distinct names.
 */
public class UniqueVisitors {

    public static void main(String[] args) {
        List<String> logins = new ArrayList<>(List.of(
                "alice", "bob", "alice", "carol", "bob",
                "dave", "alice", "eve", "carol", "frank"));

        // HashSet: uniqueness only, order is not guaranteed
        Set<String> uniqueUsers = new HashSet<>(logins);

        // LinkedHashSet: uniqueness plus insertion (first-seen) order
        Set<String> firstSeenOrder = new LinkedHashSet<>(logins);

        System.out.println("Login log: " + logins);
        System.out.println("Total logins: " + logins.size());
        System.out.println("Unique users: " + uniqueUsers.size());

        System.out.print("First three distinct names: ");
        int limit = Math.min(3, firstSeenOrder.size());
        int i = 0;
        for (String name : firstSeenOrder) {
            if (i++ == limit) {
                break;
            }
            System.out.print(name + (i < limit ? ", " : ""));
        }
        System.out.println();

        System.out.println("First-seen order: " + firstSeenOrder);
    }
}
