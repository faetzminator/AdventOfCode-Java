package ch.faetzminator.aoc2025;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day03b {

    public static void main(final String[] args) {
        final Day03b puzzle = new Day03b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final int LIMIT = 12;

    private long solution = 0;

    public void parseLine(final String line) {
        final char[] chars = line.toCharArray();
        long joltage = 0L;

        int startAt = 0;
        for (int i = 0; i < LIMIT; i++) {
            char highest = 0;
            for (int pos = startAt; pos < chars.length - (LIMIT - i - 1); pos++) {
                if (chars[pos] > highest) {
                    highest = chars[pos];
                    startAt = pos + 1;
                }
            }
            joltage = joltage * 10L + (highest - '0');
        }

        solution += joltage;
    }

    public long getSolution() {
        return solution;
    }
}
