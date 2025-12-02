package ch.faetzminator.aoc2025;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day02 {

    public static void main(final String[] args) {
        final Day02 puzzle = new Day02();
        final String line = ScannerUtil.readNonBlankLine();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLine(line);
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private long solution = 0;

    public void parseLine(final String line) {
        for (final String input : line.split(",")) {
            final String[] split = input.split("-");
            checkRange(Long.parseLong(split[0]), Long.parseLong(split[1]));
        }
    }

    private void checkRange(final long from, final long to) {
        for (long value = from; value <= to; value++) {
            check(value);
        }
    }

    private void check(final long value) {
        final String str = String.valueOf(value);
        if (str.length() % 2 == 0 && check(str, str.length() / 2)) {
            solution += value;
        }
    }

    private boolean check(final String str, final int len) {
        // more boilerplate but faster than substring
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) != str.charAt(len + i)) {
                return false;
            }
        }
        return true;
    }

    public long getSolution() {
        return solution;
    }
}
