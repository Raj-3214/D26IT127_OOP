package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        StringBuilder report = new StringBuilder("End-of-day transaction report\n");

        try (DirectoryStream<Path> files = Files.newDirectoryStream(logsFolder)) {
            for (Path file : files) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(file, BasicFileAttributes.class);
                report.append("File: ").append(file.getFileName())
                        .append(", size=").append(attributes.size())
                        .append(", last-modified=").append(attributes.lastModifiedTime())
                        .append('\n');

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length != 2) {
                            continue;
                        }
                        long amount = Long.parseLong(parts[1]);
                        if ("DEPOSIT".equals(parts[0])) {
                            totalDeposits += amount;
                        } else if ("WITHDRAW".equals(parts[0])) {
                            totalWithdrawals += amount;
                        }
                    }
                }
            }
        }

        report.append("Total deposits: ").append(totalDeposits).append('\n');
        report.append("Total withdrawals: ").append(totalWithdrawals).append('\n');
        Files.writeString(reportFile, report.toString());
        return report.toString();
    }
}
