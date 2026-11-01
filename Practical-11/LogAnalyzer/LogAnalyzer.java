import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Practical 11 - Q2: Log analyzer using java.nio.file.Path / Files.
 *
 * Creates a few .log files, reads every file line by line, counts total lines
 * and lines containing a keyword, and prints each file's size from its
 * BasicFileAttributes.
 */
public class LogAnalyzer {

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("logs");
        Files.createDirectories(dir);

        // --- Create a few sample log files ---
        Files.writeString(dir.resolve("app.log"), String.join("\n",
                "INFO  App started",
                "INFO  Loading configuration",
                "ERROR Database connection failed",
                "INFO  Retrying in 5s",
                "ERROR Retry failed"), StandardCharsets.UTF_8);

        Files.writeString(dir.resolve("auth.log"), String.join("\n",
                "INFO  User alice logged in",
                "WARN  Failed login for bob",
                "INFO  User carol logged in",
                "ERROR Token expired for alice"), StandardCharsets.UTF_8);

        Files.writeString(dir.resolve("worker.log"), String.join("\n",
                "INFO  Worker 1 ready",
                "ERROR Job 42 crashed",
                "INFO  Worker 1 restarted",
                "INFO  Job 42 requeued",
                "ERROR Job 42 crashed again",
                "INFO  Shutdown complete"), StandardCharsets.UTF_8);

        String keyword = "ERROR";

        // --- Analyze every .log file in the directory ---
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.log")) {
            stream.forEach(files::add);
        }
        Collections.sort(files);

        int totalLines = 0;
        int totalMatches = 0;

        System.out.println("Analyzing files in: " + dir.toAbsolutePath());
        System.out.println("Keyword: \"" + keyword + "\"\n");

        for (Path file : files) {
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
            int fileLines = 0;
            int fileMatches = 0;

            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines++;
                    if (line.contains(keyword)) {
                        fileMatches++;
                    }
                }
            }

            System.out.printf("%-12s size=%4d bytes  lines=%2d  matches=%d%n",
                    file.getFileName(), attrs.size(), fileLines, fileMatches);

            totalLines += fileLines;
            totalMatches += fileMatches;
        }

        System.out.println("\n---------------------------------------------");
        System.out.printf("Total files        : %d%n", files.size());
        System.out.printf("Total lines        : %d%n", totalLines);
        System.out.printf("Lines with keyword : %d%n", totalMatches);
    }
}
