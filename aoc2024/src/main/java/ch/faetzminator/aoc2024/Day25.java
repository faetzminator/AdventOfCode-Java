package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day25 {

    public static void main(final String[] args) {
        final Day25 puzzle = new Day25();
        final List<List<String>> blocks = ScannerUtil.readNonBlankLinesBlocks();
        final Timer timer = PuzzleUtil.start();
        for (final List<String> lines : blocks) {
            puzzle.parseBlock(lines);
        }
        final long solution = puzzle.getFitTogetherSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("[.#]{5}");

    private final List<LockOrKey> locks = new ArrayList<>();
    private final List<LockOrKey> keys = new ArrayList<>();

    public void parseBlock(final List<String> input) {
        if (input.size() != 7) {
            throw new IllegalArgumentException("size: " + input.size());
        }
        final boolean lock = input.get(0).charAt(0) == '#';
        final int[] values = new int[5];
        for (int i = 1; i < input.size() - 1; i++) {
            final String line = input.get(i);
            if (!LINE_PATTERN.matcher(line).matches()) {
                throw new IllegalArgumentException("line: " + line);
            }
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    values[j]++;
                }
            }
        }
        final LockOrKey lockOrKey = new LockOrKey(values);
        if (lock) {
            locks.add(lockOrKey);
        } else {
            keys.add(lockOrKey);
        }
    }

    public long getFitTogetherSum() {
        long sum = 0L;
        for (final LockOrKey lock : locks) {
            for (final LockOrKey key : keys) {
                if (lock.tryKey(key)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private static class LockOrKey {
        private final int[] values;

        public LockOrKey(final int[] values) {
            this.values = values;
        }

        public boolean tryKey(final LockOrKey key) {
            if (key.values.length != values.length) {
                return false;
            }
            for (int i = 0; i < values.length; i++) {
                if (values[i] + key.values[i] >= 6) {
                    return false;
                }
            }
            return true;
        }
    }
}
