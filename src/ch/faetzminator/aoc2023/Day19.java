package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {

    public static void main(final String[] args) {
        final Day19 puzzle = new Day19();

        final List<String> input1 = new ArrayList<>();
        final List<String> input2 = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input1.add(line);
            }
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input2.add(line);
            }
        }

        System.out.println("Calculating...");
        for (final String line : input1) {
            puzzle.parseWorkflow(line);
        }
        for (final String line : input2) {
            puzzle.parseData(line);
        }
        final long sum = puzzle.getAcceptedDataSum();
        System.out.println("Solution: " + sum);
    }

    private static final Pattern WORKFLOW_PATTERN = Pattern.compile("(\\w+)\\{(.*?)\\}");
    private static final Pattern DATA_PATTERN = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}");

    private static final Pattern COMPLEX_RULE_PATTERN = Pattern.compile("([xmas])([<>])(\\d+):(\\w+)");
    private static final Pattern SIMPLE_RULE_PATTERN = Pattern.compile("(\\w+)");

    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private static final String IN_NAME = "in";
    private static final String ACCEPTED_NAME = "A";
    private static final String REJECTED_NAME = "R";

    private final Map<String, Workflow> workflows = new HashMap<>();

    private long acceptedDataSum;

    public Day19() {
        workflows.put(ACCEPTED_NAME, data -> new Result() {
            @Override
            public boolean isAccepted() {
                return true;
            }
        });
        workflows.put(REJECTED_NAME, data -> new Result() {
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
            for (final Rule rule : rules) {
                if (rule.matches(data)) {
                    return new Result() {
                        @Override
                        public String getNextRuleName() {
                            return rule.getResult(data);
                        }
                    };
                }
            }
            throw new IllegalArgumentException();
        };
    }

    private Rule parseRule(final String rule) {
        Matcher matcher = COMPLEX_RULE_PATTERN.matcher(rule);
        if (matcher.matches()) {
            final char field = matcher.group(1).charAt(0);
            final char operation = matcher.group(2).charAt(0);
            final int value = Integer.parseInt(matcher.group(3));
            final String target = matcher.group(4);

            return new Rule() {

                @Override
                public boolean matches(final Data data) {
                    int actual;
                    switch (field) {
                    case 'x':
                        actual = data.getX();
                        break;
                    case 'm':
                        actual = data.getM();
                        break;
                    case 'a':
                        actual = data.getA();
                        break;
                    case 's':
                        actual = data.getS();
                        break;
                    default:
                        throw new IllegalArgumentException("field: " + field);
                    }
                    switch (operation) {
                    case '<':
                        return actual < value;
                    case '>':
                        return actual > value;
                    default:
                        throw new IllegalArgumentException("operation: " + operation);
                    }
                }

                @Override
                public String getResult(final Data data) {
                    return target;
                }
            };
        }

        matcher = SIMPLE_RULE_PATTERN.matcher(rule);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("rule: " + rule);
        }
        final String target = matcher.group(1);
        return new Rule() {

            @Override
            public boolean matches(final Data data) {
                return true;
            }

            @Override
            public String getResult(final Data data) {
                return target;
            }
        };
    }

    public void parseData(final String line) {
        final Matcher matcher = DATA_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final int x = Integer.parseInt(matcher.group(1));
        final int m = Integer.parseInt(matcher.group(2));
        final int a = Integer.parseInt(matcher.group(3));
        final int s = Integer.parseInt(matcher.group(4));
        processData(new Data(x, m, a, s));
    }

    private void processData(final Data data) {
        Result result;
        Workflow workflow = workflows.get(IN_NAME);
        do {
            result = workflow.process(data);
            workflow = workflows.get(result.getNextRuleName());
        } while (!result.isAccepted() && !result.isRejected());

        if (result.isAccepted()) {
            acceptedDataSum += data.getValue();
        }
    }

    public long getAcceptedDataSum() {
        return acceptedDataSum;
    }

    private static interface Result {

        default boolean isAccepted() {
            return false;
        }

        default boolean isRejected() {
            return false;
        }

        default String getNextRuleName() {
            return null;
        }
    }

    private static interface Workflow {

        Result process(Data data);
    }

    private static interface Rule {

        boolean matches(Data data);

        String getResult(Data data);
    }

    private static class Data {

        private final int x;
        private final int m;
        private final int a;
        private final int s;

        public Data(final int x, final int m, final int a, final int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }

        public int getX() {
            return x;
        }

        public int getM() {
            return m;
        }

        public int getA() {
            return a;
        }

        public int getS() {
            return s;
        }

        public int getValue() {
            return x + m + a + s;
        }
    }
}
