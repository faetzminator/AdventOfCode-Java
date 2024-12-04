package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day03b {

    public static void main(final String[] args) {
        final Day03b puzzle = new Day03b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getProductSum();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern SKIP_PATTERN = Pattern.compile("don't\\(\\).*?do\\(\\)");
    private final static Pattern MULTIPLIER_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    private long productSum = 0L;

    public void parseLines(final List<String> lines) {
        // slow and ugly, but still better than keeping state of "unclosed" don't command on one line
        parseLine(String.join("---", lines));
    }

    private void parseLine(final String line) {
        // don't replace by empty string to not accidentally create a command
        final String sanitized = SKIP_PATTERN.matcher(line).replaceAll("---");
        final Matcher matcher = MULTIPLIER_PATTERN.matcher(sanitized);
        while (matcher.find()) {
            final long a = Long.parseLong(matcher.group(1));
            final long b = Long.parseLong(matcher.group(2));
            productSum += a * b;
        }
    }

    public long getProductSum() {
        return productSum;
    }
}
