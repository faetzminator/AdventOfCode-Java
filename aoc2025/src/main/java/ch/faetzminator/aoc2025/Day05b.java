package ch.faetzminator.aoc2025;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.Range;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day05b {

    public static void main(final String[] args) {
        final Day05b puzzle = new Day05b();
        List<String> lines;
        try (Scanner scanner = new Scanner(System.in)) {
            lines = ScannerUtil.readNonBlankLines(scanner);
            ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseFreshIngredientRangeLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern FRESH_INGREDIENT_RANGE_PATTERN = Pattern.compile("(\\d+)-(\\d+)");

    private final Set<Range> ranges = new HashSet<>();

    public void parseFreshIngredientRangeLine(final String line) {
        final Matcher matcher = FRESH_INGREDIENT_RANGE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(line);
        }
        addRange(new Range(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2))));
    }

    private void addRange(final Range range) {
        final Set<Range> toRemove = new HashSet<>();
        final Set<Range> toSplitLower = new HashSet<>();
        final Set<Range> toSplitUpper = new HashSet<>();
        for (final Range existing : ranges) {
            if (existing.intersects(range)) {
                final boolean lowIntersection = range.getStart() <= existing.getStart();
                final boolean highIntersection = range.getEnd() >= existing.getEnd();
                if (lowIntersection && highIntersection) {
                    toRemove.add(existing);
                } else if (lowIntersection) {
                    toSplitLower.add(existing);
                } else if (highIntersection) {
                    toSplitUpper.add(existing);
                } else {
                    // whole range already covered, no need to process further
                    return;
                }
            }
        }
        ranges.removeAll(toRemove);
        for (final Range existing : toSplitLower) {
            existing.splitLower(range.getEnd());
        }
        for (final Range existing : toSplitUpper) {
            existing.splitUpper(range.getStart());
        }
        ranges.add(range);
    }

    public long getSolution() {
        long solution = 0;
        for (final Range range : ranges) {
            solution += range.getLength();
        }
        return solution;
    }
}
