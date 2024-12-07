package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day01 {

    public static void main(final String[] args) {
        final Day01 puzzle = new Day01();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+) +(\\d+)");

    private final List<Long> left = new ArrayList<>();
    private final List<Long> right = new ArrayList<>();

    public void parseLine(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        left.add(Long.valueOf(matcher.group(1)));
        right.add(Long.valueOf(matcher.group(2)));
    }

    public long getSolution() {
        Collections.sort(left);
        Collections.sort(right);

        long sum = 0L;
        for (int i = 0; i < left.size(); i++) {
            sum += Math.abs(left.get(i) - right.get(i));
        }

        return sum;
    }
}
