package ch.faetzminator.aoc2024;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day24b {

    public static void main(final String[] args) {
        final Day24b puzzle = new Day24b();
        final List<String> wires;
        final List<String> gates;
        try (Scanner scanner = new Scanner(System.in)) {
            wires = ScannerUtil.readNonBlankLines(scanner);
            gates = ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        for (final String wire : wires) {
            puzzle.parseWire(wire);
        }
        for (final String gate : gates) {
            puzzle.parseGate(gate);
        }
        final String command = args.length > 0 ? args[0] : null;
        if ("print".equals(command)) {
            System.out.println();
            puzzle.printPlantUml(System.out);
        } else if ("bf".equals(command)) {
            final String solution = puzzle.getBruteForceSolution();
            PuzzleUtil.end(solution, timer);
        } else {
            final String solution = puzzle.getFullAdderChecksSolution();
            PuzzleUtil.end(solution, timer);
        }
    }

    // for testing only - example is a different machine
    private int swapsNeeded = 4;
    private Solver solver = new Solver() {
        @Override
        protected String calculateExpected(final String x, final String y) {
            return Long.toBinaryString(Long.parseLong(x, 2) + Long.parseLong(y, 2));
        }
    };

    public void setSwapsNeeded(final int swapsNeeded) {
        this.swapsNeeded = swapsNeeded;
    }

    public void setSolver(final Solver solver) {
        this.solver = solver;
    }

    private static final Pattern WIRE_LINE_PATTERN = Pattern.compile("(\\w+): ([01])");
    private static final Pattern GATE_LINE_PATTERN = Pattern.compile("(\\w+) (\\w+) (\\w+) -> (\\w+)");

    private final Map<String, Wire> wires = new LinkedHashMap<>();
    private final List<WireGate> gates = new ArrayList<>();

    public void parseWire(final String input) {
        final Matcher matcher = WIRE_LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final String name = matcher.group(1);
        final Wire wire = new Wire(name, "1".equals(matcher.group(2)));
        wires.put(name, wire);
    }

    private Wire get(final String name) {
        if (!wires.containsKey(name)) {
            wires.put(name, new Wire(name, null));
        }
        return wires.get(name);
    }

    public void parseGate(final String input) {
        final Matcher matcher = GATE_LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final Wire inA = get(matcher.group(1));
        final Gate gate = Gate.valueOf(matcher.group(2));
        final Wire inB = get(matcher.group(3));
        final Wire out = get(matcher.group(4));
        gates.add(new WireGate(inA, inB, gate, out));
    }

    public void printPlantUml(final PrintStream stream) {
        int number = 0;
        for (final WireGate gate : gates) {
            final String gateName = String.format("%s%03d", gate.getGate(), number++);
            stream.println(String.format("[%s] --> [%s]", gate.getInA(), gateName));
            stream.println(String.format("[%s] --> [%s]", gate.getInB(), gateName));
            stream.println(String.format("[%s] --> [%s]", gateName, gate.getOut()));
        }
    }

    private int getValue(final Wire wire, final String search) {
        return search.indexOf(wire.getName().charAt(0)) >= 0 ? Integer.parseInt(wire.getName().substring(1)) : -1;
    }

    public String getFullAdderChecksSolution() {
        final Set<WireGate> mismatches = new HashSet<>();

        final Set<WireGate> inGates = new HashSet<>();
        final Map<Wire, WireGate> outGates = new HashMap<>();

        final int min = 0;
        final int max = gates.stream().mapToInt(gate -> getValue(gate.getInA(), "xy")).max()
                .orElseThrow(NoSuchElementException::new);

        for (final WireGate gate : gates) {
            final int inVal = getValue(gate.getInA(), "xy"), outVal = getValue(gate.getOut(), "z");
            final boolean isIn = inVal >= 0, isOut = outVal >= 0;
            switch (gate.getGate()) {
            case XOR:
                if (isIn) {
                    final boolean first = inVal == min && outVal == min;
                    if (!first) {
                        inGates.add(gate);
                        if (isOut) {
                            mismatches.add(gate);
                        }
                    }
                } else {
                    if (!isOut) {
                        mismatches.add(gate);
                    }
                    outGates.put(gate.getInA(), gate);
                    outGates.put(gate.getInB(), gate);
                }
                break;
            case OR:
                if (isOut && outVal != max + 1) {
                    mismatches.add(gate);
                }
                break;
            case AND:
                if (isOut) {
                    mismatches.add(gate);
                }
                break;
            default:
                throw new IllegalArgumentException("gate: " + gate.getGate());
            }
        }
        for (final WireGate gate : inGates) {
            if (!mismatches.contains(gate) && !outGates.containsKey(gate.getOut())) {
                mismatches.add(gate);
                // find the one to switch with...
                // not sure how correct this is (e.g. checking gate type?) but it works for my input
                final Wire a = gate.getInA();
                final String expected = "z" + a.getName().substring(1);
                final Set<WireGate> filteredOut = outGates.values().stream()
                        .filter(item -> expected.equals(item.getOut().getName())).collect(Collectors.toSet());
                for (final WireGate out : filteredOut) {
                    mismatches.addAll(gates.stream()
                            .filter(item -> item.getOut() == out.getInA() || item.getOut() == out.getInB())
                            .filter(item -> item.getInA() == a || item.getInB() == a).collect(Collectors.toSet()));
                }
            }
        }
        return toString(mismatches);
    }

    public String getBruteForceSolution() {
        solver.init(getValue('x'), getValue('y'));
        return swap(1, 0);
    }

    private String swap(final int level, final int position) {
        for (int i = position; i < gates.size(); i++) {
            if (!gates.get(i).isSwapped()) {
                for (int j = i + 1; j < gates.size(); j++) {
                    if (!gates.get(j).isSwapped()) {
                        gates.get(i).swap(gates.get(j));
                        if (level < swapsNeeded) {
                            final String solution = swap(level + 1, i + 1);
                            if (solution != null) {
                                return solution;
                            }
                        } else {
                            final List<WireGate> solution = testAndReset();
                            if (solution != null) {
                                return toString(solution);
                            }
                        }
                        gates.get(i).reset();
                        gates.get(j).reset();
                    }
                }
            }
        }
        return null;
    }

    private String toString(final Collection<WireGate> solution) {
        return solution.stream().map(gate -> gate.getOut().getName()).sorted().collect(Collectors.joining(","));
    }

    private List<WireGate> testAndReset() {
        run();
        if (solver.test(getValue('z'))) {
            return gates.stream().filter(WireGate::isSwapped).collect(Collectors.toList());
        }
        wires.values().stream().forEach(Wire::reset);
        return null;
    }

    private void run() {
        final Queue<WireGate> queue = new LinkedList<>(gates);
        while (!queue.isEmpty()) {
            final WireGate gate = queue.poll();
            if (!gate.produce()) {
                queue.add(gate);
            }
        }
    }

    private String getValue(final char c) {
        final String format = String.valueOf(c) + "%02d";
        String key, solution = "";
        int index = 0;
        while (wires.containsKey(key = String.format(format, index++))) {
            solution = String.valueOf(wires.get(key).intValue()) + solution;
        }
        return solution;
    }

    private static class Wire {

        private final String name;
        private final Boolean initial;
        private Boolean value;

        public Wire(final String name, final Boolean initial) {
            this.name = name;
            this.initial = value = initial;
        }

        public boolean setValue(final Boolean value) {
            if (this.value == null && value != null) {
                this.value = value;
            }
            return value != null;
        }

        public Boolean getValue() {
            return value;
        }

        public int intValue() {
            if (value == null) {
                return -1;
            }
            return value ? 1 : 0;
        }

        public String getName() {
            return name;
        }

        public void reset() {
            value = initial;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class WireGate {

        private final Wire inA;
        private final Wire inB;
        private final Gate gate;
        private final Wire out;
        private Wire swapped;

        public WireGate(final Wire inA, final Wire inB, final Gate gate, final Wire out) {
            this.inA = inA;
            this.inB = inB;
            this.gate = gate;
            this.out = out;
        }

        public boolean produce() {
            final Boolean value = gate.produce(inA.getValue(), inB.getValue());
            return (swapped != null ? swapped : out).setValue(value);
        }

        public void swap(final WireGate other) {
            if (swapped != null || other.swapped != null) {
                throw new IllegalStateException();
            }
            swapped = other.out;
            other.swapped = out;
        }

        public boolean isSwapped() {
            return swapped != null;
        }

        public Wire getInA() {
            return inA;
        }

        public Wire getInB() {
            return inB;
        }

        public Gate getGate() {
            return gate;
        }

        public Wire getOut() {
            return out;
        }

        public void reset() {
            swapped = null;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s -> %s", inA, gate, inB, swapped != null ? swapped : out);
        }
    }

    private static enum Gate {

        AND {
            @Override
            public Boolean produce(final Boolean a, final Boolean b) {
                return a == null || b == null ? null : a && b;
            }
        },
        OR {
            @Override
            public Boolean produce(final Boolean a, final Boolean b) {
                return a == null || b == null ? null : a || b;
            }
        },
        XOR {
            @Override
            public Boolean produce(final Boolean a, final Boolean b) {
                return a == null || b == null ? null : a != b;
            }
        };

        public abstract Boolean produce(Boolean a, Boolean b);
    }

    protected abstract static class Solver {

        private String expected;

        public void init(final String x, final String y) {
            expected = calculateExpected(x, y);
        }

        protected abstract String calculateExpected(final String x, final String y);

        public boolean test(final String z) {
            return z.endsWith(expected);
        }
    }
}
