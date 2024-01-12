package ch.faetzminator.aoc2023;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day01b {

    public static void main(final String[] args) {
        final Day01b puzzle = new Day01b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.addLine(line);
        }
        final long solution = puzzle.getCalibrationSum();
        PuzzleUtil.end(solution, timer);
    }

    private long calibrationSum;

    public void addLine(final String str) {
        calibrationSum += parseCalibration(str);
    }

    private static final String[] NUMBERS = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
            "nine" };

    private int parseNumber(final String str, final int pos) {
        final char c = str.charAt(pos);
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        final String sub = str.substring(pos);
        // ignore zero, start 1
        for (int i = 1; i < NUMBERS.length; i++) {
            if (sub.startsWith(NUMBERS[i])) {
                return i;
            }
        }
        return -1;
    }

    private int parseCalibration(final String str) {
        int first = 0, last = 0;
        boolean firstSet = false;
        for (int i = 0; i < str.length(); i++) {
            final int number = parseNumber(str, i);
            if (number >= 0) {
                if (!firstSet) {
                    first = number;
                    firstSet = true;
                }
                last = number;
            }
        }
        return 10 * first + last;
    }

    public long getCalibrationSum() {
        return calibrationSum;
    }
}
