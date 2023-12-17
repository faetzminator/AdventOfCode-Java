package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day13 {

    public static void main(final String[] args) {
        final Day13 puzzle = new Day13();

        final List<List<String>> input = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            final Pattern linePattern = Pattern.compile("[#.]+");
            boolean newNeeded = true;
            List<String> subInput = new ArrayList<>();
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (linePattern.matcher(line).matches()) {
                    if (newNeeded) {
                        subInput = new ArrayList<>();
                        input.add(subInput);
                        newNeeded = false;
                    }
                    subInput.add(line);
                } else if (line.isEmpty()) {
                    newNeeded = true;
                } else if ("EOF".equals(line)) {
                    break;
                } else {
                    throw new IllegalArgumentException("line: " + line);
                }
            }
        }

        System.out.println("Calculating...");
        for (final List<String> lines : input) {
            puzzle.parsePattern(lines);
        }
        System.out.println("Solution: " + puzzle.getSummary());
    }

    private long summary;

    public void parsePattern(final List<String> lines) {
        final boolean[][] pattern = new boolean[lines.size()][];
        for (int y = 0; y < lines.size(); y++) {
            final String line = lines.get(y);
            pattern[y] = new boolean[line.length()];
            for (int x = 0; x < line.length(); x++) {
                pattern[y][x] = line.charAt(x) == '#';
            }
        }

        final int vMatch = getVerticalMatch(pattern);
        final int hMatch = getHorizontalMatch(pattern);
        if (vMatch >= 0) {
            summary += vMatch + 1;
        }
        if (hMatch >= 0) {
            summary += 100 * (hMatch + 1);
        }
    }

    private int getHorizontalMatch(final boolean[][] pattern) {
        for (int mirrorAfter = 0; mirrorAfter < pattern.length - 1; mirrorAfter++) {
            final int checks = Math.min(mirrorAfter + 1, pattern.length - mirrorAfter - 1);
            boolean matches = true;
            for (int i = 0; i < checks && matches; i++) {
                matches = Arrays.equals(pattern[mirrorAfter - i], pattern[mirrorAfter + i + 1]);
            }
            if (matches) {
                return mirrorAfter;
            }
        }
        return -1;
    }

    private int getVerticalMatch(final boolean[][] pattern) {
        for (int mirrorAfter = 0; mirrorAfter < pattern[0].length - 1; mirrorAfter++) {
            final int checks = Math.min(mirrorAfter + 1, pattern[0].length - mirrorAfter - 1);
            boolean matches = true;
            for (int i = 0; i < checks && matches; i++) {
                for (int y = 0; y < pattern.length && matches; y++) {
                    matches = pattern[y][mirrorAfter - i] == pattern[y][mirrorAfter + i + 1];
                }
            }
            if (matches) {
                return mirrorAfter;
            }
        }
        return -1;
    }

    public long getSummary() {
        return summary;
    }
}
