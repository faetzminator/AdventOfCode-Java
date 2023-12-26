package ch.faetzminator.aoc2023;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day01 {

    public static void main(final String[] args) {
        final Day01 puzzle = new Day01();
        final List<String> lines = ScannerUtil.readNonEmptyLines();
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

    private int parseCalibration(final String str) {
        char first = 0, last = 0;
        boolean firstSet = false;
        for (final char c : str.toCharArray()) {
            if (c >= '0' && c <= '9') {
                if (!firstSet) {
                    first = c;
                    firstSet = true;
                }
                last = c;
            }
        }
        return 10 * (first - '0') + last - '0';
    }

    public long getCalibrationSum() {
        return calibrationSum;
    }
}
