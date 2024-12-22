package ch.faetzminator.aoc2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.faetzminator.aocutil.CollectionsUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day22b {

    public static void main(final String[] args) {
        final Day22b puzzle = new Day22b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSecretNumberSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final int ITERATIONS = 2000;

    private static final int MIN = -9;
    private static final int MAX = 9;
    private static final int LENGTH = MAX - MIN + 1;

    private final long[] secretNumberSums = new long[(int) Math.pow(LENGTH, 4)];

    public void parseLine(final String input) {
        final long number = Long.parseLong(input);
        for (final Entry<Integer, Integer> entry : getBananaCounts(number).entrySet()) {
            secretNumberSums[entry.getKey()] += entry.getValue();
        }
    }

    private int toKey(final int[] numbers) {
        int key = 0;
        for (final int number : numbers) {
            key = key * LENGTH + number - MIN;
        }
        return key;
    }

    private Map<Integer, Integer> getBananaCounts(long number) {
        final Map<Integer, Integer> values = new HashMap<>();
        int last = (int) (number % 10);
        final int[] diffs = new int[4];
        for (int i = 0; i < ITERATIONS; i++) {
            number = next(number);
            final int result = (int) (number % 10);
            for (int j = 0; j < diffs.length - 1; j++) {
                diffs[j] = diffs[j + 1];
            }
            diffs[diffs.length - 1] = result - last;
            last = result;
            final int key = toKey(diffs);
            if (!values.containsKey(key) && i >= 3) {
                values.put(key, result);
            }
        }
        return values;
    }

    private long next(long number) {
        number = (number ^ (number * 64L)) % 16777216;
        number = (number ^ (number / 32L)) % 16777216;
        return (number ^ number * 2048L) % 16777216;
    }

    public long getSecretNumberSum() {
        return CollectionsUtil.biggest(Arrays.stream(secretNumberSums).boxed(), value -> value);
    }
}