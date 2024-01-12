package ch.faetzminator.aocutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ScannerUtil {

    private ScannerUtil() {
    }

    public static List<List<String>> readNonBlankLinesBlocks() {
        try (Scanner scanner = new Scanner(System.in)) {
            return readNonBlankLinesBlocks(scanner);
        }
    }

    public static List<List<String>> readNonBlankLinesBlocks(final Scanner scanner) {
        final List<List<String>> result = new ArrayList<>();
        List<String> lines;
        while (!(lines = readNonBlankLines(scanner)).isEmpty()) {
            result.add(lines);
        }
        return result;
    }

    public static List<String> readNonBlankLines() {
        try (Scanner scanner = new Scanner(System.in)) {
            return readNonBlankLines(scanner);
        }
    }

    public static List<String> readNonBlankLines(final Scanner scanner) {
        final List<String> lines = new ArrayList<>();
        String line;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).isBlank()) {
            lines.add(line);
        }
        return lines;
    }

    public static String readNonBlankLine() {
        try (Scanner scanner = new Scanner(System.in)) {
            return readNonBlankLine(scanner);
        }
    }

    public static String readNonBlankLine(final Scanner scanner) {
        final String line = readLine(scanner);
        if (line.isBlank()) {
            throw new RuntimeException("unexpected blank line");
        }
        return line;
    }

    public static void readBlankLine(final Scanner scanner) {
        final String line = readLine(scanner);
        if (!line.isBlank()) {
            throw new RuntimeException("unexpected non blank line: " + line);
        }
    }

    private static String readLine(final Scanner scanner) {
        if (!scanner.hasNextLine()) {
            throw new RuntimeException("no next line");
        }
        return scanner.nextLine();
    }
}
