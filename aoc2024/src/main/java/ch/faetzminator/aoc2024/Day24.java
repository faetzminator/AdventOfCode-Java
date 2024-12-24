package ch.faetzminator.aoc2024;

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
    private final Queue<WireGate> gates = new LinkedList<>();

    public void parseWire(final String input) {
        final Matcher matcher = WIRE_LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final Wire wire = new Wire();
        wire.setValue("1".equals(matcher.group(2)));
        wires.put(matcher.group(1), wire);
    }

    private Wire get(final String name) {
        if (!wires.containsKey(name)) {
            wires.put(name, new Wire());
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
        while (!gates.isEmpty()) {
            final WireGate gate = gates.poll();
            if (!gate.produce()) {
                gates.add(gate);
            }
        }
        return getValue('z');
    }

    private long getValue(final char c) {
        final String format = String.valueOf(c) + "%02d";
        String key, solution = "";
        int index = 0;
        while (wires.containsKey(key = String.format(format, index++))) {
            solution = String.valueOf(wires.get(key).intValue()) + solution;
        }
        return Long.parseLong(solution, 2);
    }

    private static class Wire {

        private Boolean value;

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
