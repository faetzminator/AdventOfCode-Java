package ch.faetzminator.aoc2024;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day22 {

    public static void main(final String[] args) {
        final Day22 puzzle = new Day22();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSecretNumberSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final int ITERATIONS = 2000;

    private long secretNumberSum;

    public void parseLine(final String input) {
        long number = Long.parseLong(input);
        for (int i = 0; i < ITERATIONS; i++) {
            number = next(number);
        }
        secretNumberSum += number;
    }

    private long next(long number) {
        number = (number ^ (number * 64L)) % 16777216;
        number = (number ^ (number / 32L)) % 16777216;
        return (number ^ number * 2048L) % 16777216;
    }

    public long getSecretNumberSum() {
        return secretNumberSum;
    }
}
