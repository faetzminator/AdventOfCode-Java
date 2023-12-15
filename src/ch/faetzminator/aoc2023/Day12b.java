package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12b {

    public static void main(String[] args) {
        Day12b puzzle = new Day12b();

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
        arrangementSum += calculateArrangementsBy5(record, parts);
    }

    private Map<CacheKey, Long> cache;

    private long calculateArrangementsBy5(String record, int[] parts) {
        cache = new HashMap<>();
        String recordBy5 = record + '?' + record + '?' + record + '?' + record + '?' + record;
        int[] partsBy5 = new int[parts.length * 5];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(parts, 0, partsBy5, parts.length * i, parts.length);
        }
        return calculateArrangements(recordBy5, partsBy5);
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
                    CacheKey cacheKey = new CacheKey(atPart + 1, nextStartIndex);
                    if (cache.containsKey(cacheKey)) {
                        combo = cache.get(cacheKey);
                    } else {
                        combo = calculateArrangements(record, parts, atPart + 1, partsLen, nextStartIndex);
                        cache.put(cacheKey, combo);
                    }
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

    public final class CacheKey {

        private final int atPart;
        private final int startIndex;

        public CacheKey(int atPart, int startIndex) {
            super();
            this.atPart = atPart;
            this.startIndex = startIndex;
        }

        @Override
        public int hashCode() {
            return 31 + Objects.hash(atPart, startIndex);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            CacheKey other = (CacheKey) obj;
            return atPart == other.atPart && startIndex == other.startIndex;
        }
    }
}
