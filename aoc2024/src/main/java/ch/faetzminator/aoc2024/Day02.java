package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day02 {

    public static void main(final String[] args) {
        final Day02 puzzle = new Day02();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseReport(line);
        }
        final long solution = puzzle.getValidReportSum();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern SPLIT_PATTERN = Pattern.compile(" ");
    private final static int MAX_DISTANCE = 3;

    private long validReportSum = 0L;

    public void parseReport(final String line) {
        final String[] levelStrs = SPLIT_PATTERN.split(line);
        if (levelStrs.length < 3) {
            throw new IllegalArgumentException("line: " + line);
        }
        final int[] levels = new int[levelStrs.length];
        for (int i = 0; i < levelStrs.length; i++) {
            levels[i] = Integer.parseInt(levelStrs[i]);
        }
        if (checkReport(levels)) {
            validReportSum++;
        }
    }

    private boolean checkReport(final int[] levels) {
        int last = levels[0];
        final boolean increasing = levels[1] > last;
        for (int i = 1; i < levels.length; i++) {
            final int current = levels[i];
            if (increasing != current > last) {
                return false;
            }
            final int diff = last - current;
            if (diff == 0 || diff > MAX_DISTANCE || diff < -MAX_DISTANCE) {
                return false;
            }
            last = current;
        }
        return true;
    }

    public long getValidReportSum() {
        return validReportSum;
    }
}
