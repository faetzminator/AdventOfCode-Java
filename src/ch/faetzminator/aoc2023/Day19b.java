package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.Range;

public class Day19b {

    public static void main(final String[] args) {
        final Day19b puzzle = new Day19b();

        final List<String> input1 = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input1.add(line);
            }
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {

            }
        }

        System.out.println("Calculating...");
        for (final String line : input1) {
            puzzle.parseWorkflow(line);
        }
        puzzle.findAcceptedData();
        final long sum = puzzle.getAcceptedDataSum();
        System.out.println("Solution: " + sum);
    }

    private static final String VALUES = "xmas";

    private static final Pattern WORKFLOW_PATTERN = Pattern.compile("(\\w+)\\{(.*?)\\}");

    private static final Pattern COMPLEX_RULE_PATTERN = Pattern.compile("([" + VALUES + "])([<>])(\\d+):(\\w+)");
    private static final Pattern SIMPLE_RULE_PATTERN = Pattern.compile("(\\w+)");

    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private static final String IN_NAME = "in";
    private static final String ACCEPTED_NAME = "A";
    private static final String REJECTED_NAME = "R";

    private final Map<String, Workflow> workflows = new HashMap<>();

    private long acceptedDataSum;

    public Day19b() {
        workflows.put(ACCEPTED_NAME, data -> new Result(List.of(), data) {
            @Override
            public boolean isAccepted() {
                return true;
            }
        });
        workflows.put(REJECTED_NAME, data -> new Result(List.of(), data) {
            @Override
            public boolean isRejected() {
                return true;
            }
        });
    }

    public void parseWorkflow(final String line) {
        final Matcher matcher = WORKFLOW_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final String name = matcher.group(1);
        final String[] rules = COMMA_PATTERN.split(matcher.group(2));

        workflows.put(name, parseWorkflow(rules));
    }

    private Workflow parseWorkflow(final String[] rulesStr) {
        final List<Rule> rules = new ArrayList<>();
        for (final String rule : rulesStr) {
            rules.add(parseRule(rule));
        }
        return data -> {
            final Result result = new Result(new ArrayList<>(), data);
            for (final Rule rule : rules) {
                final Result newResult = rule.getResult(result.getRemaining());
                result.getSortedOut().addAll(newResult.getSortedOut());
                result.setRemaining(newResult.getRemaining());
            }
            if (!result.getRemaining().isEmpty()) {
                throw new IllegalArgumentException("remaining: " + result.getRemaining());
            }
            return result;
        };
    }

    private Rule parseRule(final String rule) {
        Matcher matcher = COMPLEX_RULE_PATTERN.matcher(rule);
        if (matcher.matches()) {
            final char field = matcher.group(1).charAt(0);
            final char operation = matcher.group(2).charAt(0);
            final int value = Integer.parseInt(matcher.group(3));
            final String target = matcher.group(4);

            return data -> {
                final List<DataSet> split = new ArrayList<>();
                switch (operation) {
                case '<':
                    split.add(data.splitLower(field, value - 1));
                    break;
                case '>':
                    split.add(data.splitUpper(field, value + 1));
                    break;
                default:
                    throw new IllegalArgumentException("operation: " + operation);
                }

                final List<DataSetWithTarget> sortedOut = new ArrayList<>();
                for (final DataSet d : split) {
                    sortedOut.add(new DataSetWithTarget(d, target));
                }
                return new Result(sortedOut, data);
            };
        }

        matcher = SIMPLE_RULE_PATTERN.matcher(rule);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("rule: " + rule);
        }
        final String target = matcher.group(1);
        return data -> new Result(List.of(new DataSetWithTarget(data, target)), DataSet.empty());
    }

    public void findAcceptedData() {

        final Queue<DataSetWithTarget> queue = new LinkedList<>();
        queue.add(new DataSetWithTarget(DataSet.entireRange(), IN_NAME));

        while (!queue.isEmpty()) {
            final DataSetWithTarget item = queue.poll();
            final Result result = workflows.get(item.getTarget()).process(item.getDataSet());
            if (result.isAccepted()) {
                acceptedDataSum += result.getRemaining().getCount();
            } else if (!result.isRejected()) {
                queue.addAll(result.getSortedOut());
            }
        }
    }

    public long getAcceptedDataSum() {
        return acceptedDataSum;
    }

    private static class Result {

        private final List<DataSetWithTarget> sortedOut;
        private DataSet remaining;

        public Result(final List<DataSetWithTarget> sortedOut, final DataSet remaining) {
            this.sortedOut = sortedOut;
            this.remaining = remaining;
        }

        public List<DataSetWithTarget> getSortedOut() {
            return sortedOut;
        }

        public DataSet getRemaining() {
            return remaining;
        }

        public void setRemaining(final DataSet remaining) {
            this.remaining = remaining;
        }

        public boolean isAccepted() {
            return false;
        }

        public boolean isRejected() {
            return false;
        }
    }

    private static interface Workflow {

        Result process(DataSet data);
    }

    private static interface Rule {

        Result getResult(DataSet data);
    }

    private static final int START = 1;
    private static final int END = 4000;

    private static class DataSetWithTarget {

        private final DataSet dataSet;
        private final String target;

        public DataSetWithTarget(final DataSet dataSet, final String target) {
            this.dataSet = dataSet;
            this.target = target;
        }

        public DataSet getDataSet() {
            return dataSet;
        }

        public String getTarget() {
            return target;
        }
    }

    private static class DataSet {

        private final Map<Character, Range> ranges = new LinkedHashMap<>();

        private DataSet() {
        }

        private DataSet(final DataSet dataSet) {
            for (final Entry<Character, Range> entry : dataSet.getRanges().entrySet()) {
                ranges.put(entry.getKey(), entry.getValue().copy());
            }
        }

        public DataSet splitUpper(final Character id, final int upperStart) {
            final Range range = ranges.get(id);
            final Range splitRange = range.splitUpper(upperStart);
            if (splitRange != null) {
                final DataSet dataSet = new DataSet(this);
                dataSet.setRange(id, splitRange);
                return dataSet;
            }
            return null;
        }

        public DataSet splitLower(final Character id, final int lowerEnd) {
            final Range range = ranges.get(id);
            final Range splitRange = range.splitLower(lowerEnd);
            if (splitRange != null) {
                final DataSet dataSet = new DataSet(this);
                dataSet.setRange(id, splitRange);
                return dataSet;
            }
            return null;
        }

        public long getCount() {
            long product = 1L;
            for (final Range range : ranges.values()) {
                product *= range.getLength();
            }
            return product;
        }

        public boolean isEmpty() {
            return ranges.isEmpty();
        }

        private Map<Character, Range> getRanges() {
            return ranges;
        }

        private void setRange(final Character id, final Range range) {
            ranges.put(id, range);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            for (final Entry<Character, Range> entry : ranges.entrySet()) {
                if (builder.length() > 0) {
                    builder.append("; ");
                }
                builder.append(entry.getKey()).append(": ");
                final Range range = entry.getValue();
                builder.append(range.getStart()).append('-').append(range.getEnd()).append(", ");
                builder.setLength(builder.length() - 2);
            }
            return builder.toString();
        }

        public static DataSet entireRange() {
            final DataSet dataSet = new DataSet();
            for (final char c : VALUES.toCharArray()) {
                dataSet.setRange(c, new Range(START, END));
            }
            return dataSet;
        }

        private static final DataSet EMPTY = new DataSet();

        public static DataSet empty() {
            return EMPTY;
        }

    }
}
