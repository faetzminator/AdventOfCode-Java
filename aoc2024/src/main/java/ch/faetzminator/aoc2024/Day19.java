package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day19 {

    public static void main(final String[] args) {
        final Day19 puzzle = new Day19();
        final String patternsLine;
        final List<String> lines;
        try (Scanner scanner = new Scanner(System.in)) {
            patternsLine = ScannerUtil.readNonBlankLine(scanner);
            ScannerUtil.readBlankLine(scanner);
            lines = ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        puzzle.parsePatterns(patternsLine);
        for (final String line : lines) {
            puzzle.parseTowel(line);
        }
        final long solution = puzzle.getPossibleTowelSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final String PATTERNS_SEPARATOR = ", ";
    private static final Pattern PATTERNS_LINE_PATTERN = Pattern.compile("([a-z]+" + PATTERNS_SEPARATOR + ")*[a-z]+");

    private final List<String> patterns = new ArrayList<>();
    private long possibleSum = 0L;

    public void parsePatterns(final String line) {
        if (!PATTERNS_LINE_PATTERN.matcher(line).matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        patterns.addAll(Arrays.asList(line.split(PATTERNS_SEPARATOR)));
    }

    public void parseTowel(final String line) {
        if (checkTowel(line)) {
            possibleSum++;
        }
    }

    private boolean checkTowel(final String sub) {
        if (sub.isEmpty()) {
            return true;
        }
        for (final String pattern : patterns) {
            if (sub.startsWith(pattern) && checkTowel(sub.substring(pattern.length()))) {
                return true;
            }
        }
        return false;
    }

    public long getPossibleTowelSum() {
        return possibleSum;
    }
}
