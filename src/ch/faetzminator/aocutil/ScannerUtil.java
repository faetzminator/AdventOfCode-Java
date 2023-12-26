package ch.faetzminator.aocutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ScannerUtil {

    private ScannerUtil() {
    }

    public static List<String> readNonEmptyLines() {
        try (Scanner scanner = new Scanner(System.in)) {
            return readNonEmptyLines(scanner);
        }
    }

    public static List<String> readNonEmptyLines(final Scanner scanner) {
        final List<String> lines = new ArrayList<>();
        String line;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
            lines.add(line);
        }
        return lines;
    }

    public static String readLine() {
        try (Scanner scanner = new Scanner(System.in)) {
            return readLine(scanner);
        }
    }

    public static String readLine(final Scanner scanner) {
        return scanner.nextLine();
    }
}
