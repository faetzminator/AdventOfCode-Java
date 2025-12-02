package ch.faetzminator.aoc2025;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day02b {

    public static void main(final String[] args) {
        final Day02b puzzle = new Day02b();
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
        final int maxLen = str.length() / 2;
        for (int len = 1; len <= maxLen; len++) {
            if (str.length() % len == 0 && check(str, len)) {
                solution += value;
                return;
            }
        }
    }

    private boolean check(final String str, final int len) {
        // more boilerplate but faster than substring
        for (int i = 0; i < len; i++) {
            final char c = str.charAt(i);
            for (int j = i + len; j < str.length(); j += len) {
                if (c != str.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public long getSolution() {
        return solution;
    }
}
