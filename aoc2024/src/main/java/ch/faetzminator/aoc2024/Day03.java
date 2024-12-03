package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day03 {

    public static void main(final String[] args) {
        final Day03 puzzle = new Day03();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getProductSum();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern MULTIPLIER_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    private long productSum = 0L;

    public void parseLine(final String line) {
        final Matcher matcher = MULTIPLIER_PATTERN.matcher(line);
        while (matcher.find()) {
            final long a = Long.parseLong(matcher.group(1));
            final long b = Long.parseLong(matcher.group(2));
            productSum += a*b;
        }
    }

    public long getProductSum() {
        return productSum;
    }
}
