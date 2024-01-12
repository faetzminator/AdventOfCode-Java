package ch.faetzminator.aoc2023;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.LRNode;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day08 {

    public static void main(final String[] args) {
        final Day08 puzzle = new Day08();

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

    public long calculateSteps() {
        int steps = 0;
        String next = "AAA";
        do {
            final LRNode<String> node = nodes.get(next);
            if (instructions[steps++ % instructions.length] == 'L') {
                next = node.getLeft();
            } else {
                next = node.getRight();
            }
        } while (!next.equals("ZZZ"));
        return steps;
    }
}
