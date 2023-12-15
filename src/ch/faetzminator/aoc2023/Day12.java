package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {

    public static void main(String[] args) {
        Day12 puzzle = new Day12();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (String line : input) {
            puzzle.parseConditionRecord(line);
        }
        System.out.println("Solution: " + puzzle.getArrangementSum());
    }

    private long arrangementSum;

    private Pattern linePattern = Pattern.compile("([.?#]+) ((\\d+,)+\\d+)");

    public void parseConditionRecord(String line) {
        Matcher matcher = linePattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        String record = matcher.group(1);
        String[] partsStr = matcher.group(2).split(",");
        int[] parts = new int[partsStr.length];
        for (int i = 0; i < partsStr.length; i++) {
            parts[i] = Integer.parseInt(partsStr[i]);
        }
        arrangementSum += calculateArrangements(record, parts);
    }

    private long calculateArrangements(String record, int[] parts) {
        int partsLength = parts.length - 1;
        for (int part : parts) {
            partsLength += part;
        }
        return calculateArrangements(record, parts, 0, partsLength, 0);
    }

    private boolean substrDoesNotContain(String record, int pos, int len, char c) {
        for (int i = 0; i < len; i++) {
            int x = pos + i;
            if (x >= 0 && x < record.length() && record.charAt(x) == c) {
                return false;
            }
        }
        return true;
    }

    private long calculateArrangements(String record, int[] parts, int atPart, int partsLength, int startIndex) {
        long arrangements = 0;
        int partsLen = partsLength - parts[atPart] - 1;
        for (int pos = startIndex; pos < record.length() - partsLength + 1; pos++) {
            if (substrDoesNotContain(record, startIndex - 1, pos - startIndex + 1, '#')
                    && substrDoesNotContain(record, pos, parts[atPart], '.')
                    && substrDoesNotContain(record, pos + parts[atPart], 1, '#')) {

                long combo = 1;
                int nextStartIndex = pos + parts[atPart] + 1;
                if (atPart < parts.length - 1) {
                    combo = calculateArrangements(record, parts, atPart + 1, partsLen, nextStartIndex);
                } else if (!substrDoesNotContain(record, nextStartIndex, record.length() - nextStartIndex, '#')) {
                    combo = 0;
                }
                arrangements += combo;
            }
        }
        return arrangements;
    }

    public long getArrangementSum() {
        return arrangementSum;
    }
}
