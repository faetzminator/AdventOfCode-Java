package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final List<Long> left = new ArrayList<>();
    private final Map<Long, Integer> right = new HashMap<>();


    private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+) +(\\d+)");

    public void addLine(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        left.add(Long.valueOf(matcher.group(1)));
        final Long rightValue = Long.valueOf(matcher.group(2));
        if (!right.containsKey(rightValue)) {
            right.put(rightValue, 0);
        }
        right.put(rightValue, right.get(rightValue) + 1);
    }

    public long getSolution() {
        long sum = 0L;
        for (final Long leftValue : left) {
            if (right.containsKey(leftValue)) {
                sum += leftValue * right.get(leftValue);
            }
        }
        return sum;
    }
}
