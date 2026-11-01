public class ChatFilter {

    // Returns "Matches: N" followed by the matching lines.
    public static String filter(String[] lines, String keyword) {
        String key = keyword.toLowerCase();
        int matches = 0;
        StringBuilder report = new StringBuilder();

        for (String line : lines) {
            String[] parts = line.split(" ", 3);
            if (parts.length < 3) {
                continue;                       // skip malformed lines
            }
            if (parts[2].toLowerCase().contains(key)) {
                matches++;
                report.append(parts[0]).append(' ')
                      .append(parts[1]).append(": ")
                      .append(parts[2]).append('\n');
            }
        }

        return "Matches: " + matches + "\n" + report;
    }
}
