package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day07b {

    public static void main(final String[] args) {
        final Day07b puzzle = new Day07b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseEquation(line);
        }
        final long solution = puzzle.getEquationSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final String SPLIT_EXPRESSION = " ";
    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+): (\\d+(?:" + SPLIT_EXPRESSION + "\\d+)+)");
    private static final Pattern NUMBER_SPLIT_PATTERN = Pattern.compile(SPLIT_EXPRESSION);

    private long equationSum = 0L;

    public void parseEquation(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }

        final long expectedResult = Long.parseLong(matcher.group(1));
        final String[] split = NUMBER_SPLIT_PATTERN.split(matcher.group(2));
        final long[] numbers = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            numbers[i] = Long.parseLong(split[i]);
        }

        if (isValidEquation(numbers, expectedResult)) {
            equationSum += expectedResult;
        }
    }

    private boolean isValidEquation(final long[] numbers, final long expectedResult) {
        return isValidEquation(numbers, expectedResult, numbers[0], 1);
    }

    private boolean isValidEquation(final long[] numbers, final long expectedResult, final long currentResult,
            final int currentPosition) {

        if (currentPosition == numbers.length) {
            return currentResult == expectedResult;
        }
        final int nextPosition = currentPosition + 1;
        if (isValidEquation(numbers, expectedResult, currentResult + numbers[currentPosition], nextPosition)
                || isValidEquation(numbers, expectedResult, currentResult * numbers[currentPosition], nextPosition)) {
            return true;
        }
        final long shiftedResult = currentResult * MathUtil.pow10(MathUtil.countDigits(numbers[currentPosition]));
        return isValidEquation(numbers, expectedResult, shiftedResult + numbers[currentPosition], nextPosition);
    }

    public long getEquationSum() {
        return equationSum;
    }
}
