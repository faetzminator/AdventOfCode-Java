package ch.faetzminator.aoc2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.Range;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day05 {

    public static void main(final String[] args) {
        final Day05 puzzle = new Day05();
        List<String> lines, lines2;
        try (Scanner scanner = new Scanner(System.in)) {
            lines = ScannerUtil.readNonBlankLines(scanner);
            lines2 = ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseFreshIngredientRangeLine(line);
        }
        for (final String line : lines2) {
            puzzle.parseAvailableIngredientLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern FRESH_INGREDIENT_RANGE_PATTERN = Pattern.compile("(\\d+)-(\\d+)");

    private final List<Range> ranges = new ArrayList<>();
    private long solution;

    public void parseFreshIngredientRangeLine(final String line) {
        final Matcher matcher = FRESH_INGREDIENT_RANGE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(line);
        }
        ranges.add(new Range(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2))));
    }

    public void parseAvailableIngredientLine(final String line) {
        final long id = Long.parseLong(line);
        for (final Range range : ranges) {
            if (range.getStart() <= id && range.getEnd() >= id) {
                solution++;
                return;
            }
        }
    }

    public long getSolution() {
        return solution;
    }
}
