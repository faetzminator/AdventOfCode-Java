package ch.faetzminator.aoc2023;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day12b {

    public static void main(final String[] args) {
        final Day12b puzzle = new Day12b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseConditionRecord(line);
        }
        final long solution = puzzle.getArrangementSum();
        PuzzleUtil.end(solution, timer);
    }

    private long arrangementSum;

    private final Pattern linePattern = Pattern.compile("([.?#]+) ((\\d+,)+\\d+)");

    public void parseConditionRecord(final String line) {
        final Matcher matcher = linePattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final String record = matcher.group(1);
        final String[] partsStr = matcher.group(2).split(",");
        final int[] parts = new int[partsStr.length];
        for (int i = 0; i < partsStr.length; i++) {
            parts[i] = Integer.parseInt(partsStr[i]);
        }
        arrangementSum += calculateArrangementsBy5(record, parts);
    }

    private Map<CacheKey, Long> cache;

    private long calculateArrangementsBy5(final String record, final int[] parts) {
        cache = new HashMap<>();
        final String recordBy5 = record + '?' + record + '?' + record + '?' + record + '?' + record;
        final int[] partsBy5 = new int[parts.length * 5];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(parts, 0, partsBy5, parts.length * i, parts.length);
        }
        return calculateArrangements(recordBy5, partsBy5);
    }

    private long calculateArrangements(final String record, final int[] parts) {
        int partsLength = parts.length - 1;
        for (final int part : parts) {
            partsLength += part;
        }
        return calculateArrangements(record, parts, 0, partsLength, 0);
    }

    private boolean substrDoesNotContain(final String record, final int pos, final int len, final char c) {
        for (int i = 0; i < len; i++) {
            final int x = pos + i;
            if (x >= 0 && x < record.length() && record.charAt(x) == c) {
                return false;
            }
        }
        return true;
    }

    private long calculateArrangements(final String record, final int[] parts, final int atPart, final int partsLength,
            final int startIndex) {
        long arrangements = 0;
        final int partsLen = partsLength - parts[atPart] - 1;
        for (int pos = startIndex; pos < record.length() - partsLength + 1; pos++) {
            if (substrDoesNotContain(record, startIndex - 1, pos - startIndex + 1, '#')
                    && substrDoesNotContain(record, pos, parts[atPart], '.')
                    && substrDoesNotContain(record, pos + parts[atPart], 1, '#')) {

                long combo = 1;
                final int nextStartIndex = pos + parts[atPart] + 1;
                if (atPart < parts.length - 1) {
                    final CacheKey cacheKey = new CacheKey(atPart + 1, nextStartIndex);
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

    private static class CacheKey {

        private final int atPart;
        private final int startIndex;

        public CacheKey(final int atPart, final int startIndex) {
            super();
            this.atPart = atPart;
            this.startIndex = startIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(atPart, startIndex);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final CacheKey other = (CacheKey) obj;
            return atPart == other.atPart && startIndex == other.startIndex;
        }
    }
}
