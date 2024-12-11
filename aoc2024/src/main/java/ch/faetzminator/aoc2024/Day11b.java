package ch.faetzminator.aoc2024;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day11b {

    public static void main(final String[] args) {
        final Day11b puzzle = new Day11b();
        final String line = ScannerUtil.readNonBlankLine();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLine(line);
        final long solution = puzzle.blinkMultipleTimes();
        PuzzleUtil.end(solution, timer);
    }

    private static final int ITERATIONS = 75;

    private long[] stones;

    public void parseLine(final String input) {
        final String[] numbers = input.split(" ");
        stones = new long[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            stones[i] = Long.parseLong(numbers[i]);
        }
    }

    private long blinkMultipleTimes() {
        long sum = 0L;
        for (final long stone : stones) {
            sum += blink(0, stone);
        }
        return sum;
    }

    private final Map<CacheKey, Long> cache = new HashMap<>();

    private long blink(final int level, final long... stones) {
        long sum = 0L;
        if (level == ITERATIONS) {
            return stones.length;
        }
        for (final long stone : stones) {
            final CacheKey cacheKey = new CacheKey(level, stone);
            if (cache.containsKey(cacheKey)) {
                sum += cache.get(cacheKey);
                continue;
            }
            final int digits = MathUtil.countDigits(stone);
            long result;
            if (stone == 0L) {
                result = blink(level + 1, 1L);
            } else if (digits % 2 == 0) {
                final long divisor = MathUtil.pow10(digits / 2);
                result = blink(level + 1, stone / divisor, stone % divisor);
            } else {
                result = blink(level + 1, stone * 2024L);
            }
            cache.put(cacheKey, result);
            sum += result;
        }
        return sum;
    }

    private static class CacheKey {

        private final int level;
        private final long stone;

        public CacheKey(final int level, final long stone) {
            super();
            this.level = level;
            this.stone = stone;
        }

        @Override
        public int hashCode() {
            return Objects.hash(level, stone);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CacheKey other = (CacheKey) obj;
            return level == other.level && stone == other.stone;
        }
    }
}
