package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day13b {

    public static void main(final String[] args) {
        final Day13b puzzle = new Day13b();

        final List<List<String>> input = ScannerUtil.readNonBlankLinesBlocks();
        final Timer timer = PuzzleUtil.start();
        for (final List<String> lines : input) {
            puzzle.parseLines(lines);
        }
        final long solution = puzzle.getTokenSum();
        PuzzleUtil.end(solution, timer);
    }

    private long tokenSum;

    private final static Pattern BUTTON_PATTERN = Pattern.compile("Button \\w+: X\\+(\\d+), Y\\+(\\d+)");
    private final static Pattern PRIZE_PATTERN = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

    public void parseLines(final List<String> input) {
        final long[] buttonA = parseLine(BUTTON_PATTERN, input.get(0));
        final long[] buttonB = parseLine(BUTTON_PATTERN, input.get(1));
        final long[] prize = parseLine(PRIZE_PATTERN, input.get(2));
        // as expected, we rather don't brute force anymore...
        prize[0] += 10000000000000L;
        prize[1] += 10000000000000L;

        final long result = calculate(buttonA, buttonB, prize);
        if (result > 0) {
            tokenSum += result;
        }
    }

    private static final double EPSILON = 1e-10;

    private long calculate(final long[] a, final long[] b, final long[] prize) {
        // okay, serious algebra now...
        final double divisor = a[1] * b[0] - a[0] * b[1];
        if (divisor == 0.0) {
            return -1L;
        }
        final double aA = (b[0] * prize[1] - b[1] * prize[0]) / divisor;
        final double bA = (a[1] * prize[0] - a[0] * prize[1]) / divisor;
        final long aAttempts = (long) aA;
        final long bAttempts = (long) bA;
        if (Math.abs(aA - aAttempts) > EPSILON || Math.abs(bA - bAttempts) > EPSILON) {
            // our solution is not near to a long value
            return -1L;
        }
        return 3L * aAttempts + bAttempts;
    }

    private long[] parseLine(final Pattern pattern, final String line) {
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        return new long[] { Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)) };
    }

    public long getTokenSum() {
        return tokenSum;
    }
}
