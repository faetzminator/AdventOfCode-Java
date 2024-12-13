package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day13 {

    public static void main(final String[] args) {
        final Day13 puzzle = new Day13();

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

        final long result = calculate(buttonA, buttonB, prize);
        if (result > 0) {
            tokenSum += result;
        }
    }

    private long calculate(final long[] a, final long[] b, final long[] prize) {
        // let's just brute force...
        final long maxNumberOfAAttempts = Math.min(prize[0] / a[0], prize[1] / a[1]) + 1L;
        for (long aAttempts = 0L; aAttempts < maxNumberOfAAttempts; aAttempts++) {
            long x = a[0] * aAttempts;
            long y = a[1] * aAttempts;
            final long bAttempts = (prize[0] - x) / b[0];
            x += b[0] * bAttempts;
            y += b[1] * bAttempts;
            if (x == prize[0] && y == prize[1]) {
                return 3L * aAttempts + bAttempts;
            }
        }
        return -1L;
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
