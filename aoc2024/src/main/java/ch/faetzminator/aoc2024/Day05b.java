package ch.faetzminator.aoc2024;

import java.util.Arrays;
import java.util.Comparator;
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

public class Day05b {

    public static void main(final String[] args) {
        final Day05b puzzle = new Day05b();
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
        puzzle.init();
        for (final String line : updateLines) {
            puzzle.parseUpdate(line);
        }
        final long solution = puzzle.getPageNumberSum();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern RULE_LINE_PATTERN = Pattern.compile("(\\d+)\\|(\\d+)");
    private final static Pattern PAGES_LINE_PATTERN = Pattern.compile(",");

    private final Map<Integer, Set<Integer>> rules = new HashMap<>();
    private Comparator<Integer> pageRulesComparator;

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

    public void init() {
        pageRulesComparator = new PageRulesComparator(rules);
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
        final Integer[] copiedPages = new Integer[pages.length];
        System.arraycopy(pages, 0, copiedPages, 0, pages.length);
        Arrays.sort(copiedPages, pageRulesComparator);
        if (!Arrays.equals(pages, copiedPages)) {
            // did not pass - this time processing
            if (copiedPages.length % 2 == 0) {
                throw new IllegalArgumentException("pages: " + copiedPages);
            }
            pageNumberSum += copiedPages[copiedPages.length / 2];
        }
    }

    public long getPageNumberSum() {
        return pageNumberSum;
    }

    private static class PageRulesComparator implements Comparator<Integer> {

        private final Map<Integer, Set<Integer>> left;
        private final Map<Integer, Set<Integer>> right;

        public PageRulesComparator(final Map<Integer, Set<Integer>> rules) {
            left = new HashMap<>(rules);
            right = new HashMap<>();
            for (final Map.Entry<Integer, Set<Integer>> leftEntry : left.entrySet()) {
                for (final Integer rightKey : leftEntry.getValue()) {
                    if (!right.containsKey(rightKey)) {
                        right.put(rightKey, new HashSet<>());
                    }
                    right.get(rightKey).add(leftEntry.getKey());
                }
            }
        }

        @Override
        public int compare(final Integer arg0, final Integer arg1) {
            if (left.containsKey(arg0) && left.get(arg0).contains(arg1)) {
                return -1;
            }
            if (right.containsKey(arg0) && right.get(arg0).contains(arg1)) {
                return 1;
            }
            return 0;
        }
    }
}
