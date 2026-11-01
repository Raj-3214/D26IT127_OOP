import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Practical 12 - Q1: Word frequency using HashMap and a Comparator.
 *
 * Splits a paragraph into words, counts each word in a HashMap, then prints
 * the words sorted by frequency (highest first). Words with the same count
 * are printed alphabetically so the output is stable.
 */
public class WordFrequency {

    public static void main(String[] args) {
        String paragraph = "Java is great and Java is fast. "
                + "Maps store keys and values, and sets store unique values. "
                + "Practice Java every day to master Java collections.";

        // split on anything that is not a letter, drop empties, fold case
        String[] words = paragraph.toLowerCase().split("[^a-z]+");

        Map<String, Integer> counts = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                counts.put(word, counts.getOrDefault(word, 0) + 1);
            }
        }

        // sort entries by count (highest first), then alphabetically
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator
                .comparingInt((Map.Entry<String, Integer> e) -> e.getValue())
                .reversed()
                .thenComparing(Map.Entry::getKey));

        System.out.println("Word frequency (highest first):");
        System.out.printf("%-12s %s%n", "WORD", "COUNT");
        System.out.println("--------------------");
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.printf("%-12s %d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nTotal unique words: " + counts.size());
    }
}
