package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.LRNode;
import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day08b {

    public static void main(final String[] args) {
        final Day08b puzzle = new Day08b();

        final List<String> lines;
        final String instructions;
        try (Scanner scanner = new Scanner(System.in)) {
            instructions = ScannerUtil.readNonBlankLine(scanner);
            ScannerUtil.readBlankLine(scanner);
            lines = ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        puzzle.parseInstructions(instructions);
        for (final String line : lines) {
            puzzle.addNode(line);
        }
        final long solution = puzzle.calculateSteps();
        PuzzleUtil.end(solution, timer);
    }

    private char[] instructions;
    private final Map<String, LRNode<String>> nodes = new HashMap<>();

    public void parseInstructions(final String str) {
        if (!str.matches("[LR]+")) {
            throw new IllegalArgumentException("instructions: " + str);
        }
        instructions = str.toCharArray();
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

    public void addNode(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        nodes.put(matcher.group(1), new LRNode<>(matcher.group(2), matcher.group(3)));
    }

    private String[] findStartKeys() {
        final List<String> keys = new ArrayList<>();
        for (final String key : nodes.keySet()) {
            if (key.endsWith("A")) {
                keys.add(key);
            }
        }
        return keys.toArray(new String[keys.size()]);
    }

    private boolean atEnd(final int[] data) {
        for (final int value : data) {
            if (value == 0) {
                return false;
            }
        }
        return true;
    }

    public long calculateSteps() {
        int steps = 0;
        final String[] next = findStartKeys();

        final int[] shift = new int[next.length];
        final int[] loop = new int[next.length];

        do {
            for (int i = 0; i < next.length; i++) {
                final LRNode<String> node = nodes.get(next[i]);
                if (instructions[steps % instructions.length] == 'L') {
                    next[i] = node.getLeft();
                } else {
                    next[i] = node.getRight();
                }
                if (next[i].endsWith("Z")) {
                    if (shift[i] == 0) {
                        shift[i] = steps + 1;
                    } else if (loop[i] == 0) {
                        loop[i] = steps + 1 - shift[i];
                    }
                }
            }
            steps++;
        } while (!atEnd(loop));

        long solution = 1L;
        for (int i = 0; i < loop.length; i++) {
            if (loop[i] != shift[i]) {
                throw new IllegalArgumentException("basic LCM won't work for given input");
            }
            solution = MathUtil.lcm(solution, loop[i]);
        }
        return solution;
    }
}
