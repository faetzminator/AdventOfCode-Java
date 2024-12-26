package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day24 {

    public static void main(final String[] args) {
        final Day24 puzzle = new Day24();
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
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
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

    public long getSolution() {
        run();
        return Long.parseLong(getValue('z'), 2);
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
        private Boolean value;

        public Wire(final String name, final Boolean initial) {
            this.name = name;
            value = initial;
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

        public WireGate(final Wire inA, final Wire inB, final Gate gate, final Wire out) {
            this.inA = inA;
            this.inB = inB;
            this.gate = gate;
            this.out = out;
        }

        public boolean produce() {
            return out.setValue(gate.produce(inA.getValue(), inB.getValue()));
        }

        @Override
        public String toString() {
            return String.format("%s %s %s -> %s", inA, gate, inB, out);
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
}
