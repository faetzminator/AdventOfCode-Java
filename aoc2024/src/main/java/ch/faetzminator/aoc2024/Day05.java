package ch.faetzminator.aoc2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day05 {

    public static void main(final String[] args) {
        final Day05 puzzle = new Day05();
        final List<String> ruleLines;
        final List<String> updateLines;
        try (Scanner scanner = new Scanner(System.in)) {
            ruleLines = ScannerUtil.readNonBlankLines(scanner);
            updateLines = ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        for (final String line : ruleLines) {
            puzzle.parseRule(line);
        }
        for (final String line : updateLines) {
            puzzle.parseUpdate(line);
        }
        final long solution = puzzle.getPageNumberSum();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern RULE_LINE_PATTERN = Pattern.compile("(\\d+)\\|(\\d+)");
    private final static Pattern PAGES_LINE_PATTERN = Pattern.compile(",");

    private final Map<Integer, Set<Integer>> rules = new HashMap<>();

    private long pageNumberSum = 0L;

    public void parseRule(final String line) {
        final Matcher matcher = RULE_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final Integer left = Integer.valueOf(matcher.group(1));
        final Integer right = Integer.valueOf(matcher.group(2));
        if (!rules.containsKey(left)) {
            rules.put(left, new HashSet<>());
        }
        rules.get(left).add(right);
    }

    public void parseUpdate(final String line) {
        final String[] split = PAGES_LINE_PATTERN.split(line);
        final Integer[] pages = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            pages[i] = Integer.valueOf(split[i]);
        }
        parseUpdate(pages);
    }

    private void parseUpdate(final Integer[] pages) {
        // we could rewrite this to use PageRulesComparator from second part
        // but I like below logic and will leave it ;)
        final Set<Integer> parsed = new HashSet<>();
        for (final Integer page : pages) {
            if (rules.containsKey(page)) {
                final HashSet<Integer> mustBeAfter = new HashSet<>(rules.get(page));
                mustBeAfter.retainAll(parsed);
                if (!mustBeAfter.isEmpty()) {
                    // did not pass - ignoring
                    return;
                }
            }
            parsed.add(page);
        }
        if (pages.length % 2 == 0) {
            throw new IllegalArgumentException("pages: " + pages);
        }
        pageNumberSum += pages[pages.length / 2];
    }

    public long getPageNumberSum() {
        return pageNumberSum;
    }
}
