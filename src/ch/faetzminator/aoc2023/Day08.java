package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {

    public static void main(String[] args) {
        Day08 puzzle = new Day08();

        List<String> input = new ArrayList<>();
        String instructions;

        try (Scanner scanner = new Scanner(System.in)) {
            instructions = scanner.nextLine(); // special handling
            if (!scanner.nextLine().isEmpty()) {
                throw new IllegalArgumentException();
            }

            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        puzzle.parseInstructions(instructions);
        for (String line : input) {
            puzzle.addNode(line);
        }
        System.out.println("Solution: " + puzzle.calculateSteps());
    }

    private char[] instructions;
    private Map<String, Node> nodes = new HashMap<>();

    public void parseInstructions(String str) {
        if (!str.matches("[LR]+")) {
            throw new IllegalArgumentException("instructions: " + str);
        }
        instructions = str.toCharArray();
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

    public void addNode(String line) {
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        nodes.put(matcher.group(1), new Node(matcher.group(2), matcher.group(3)));
    }

    public int calculateSteps() {
        int steps = 0;
        String next = "AAA";
        do {
            Node node = nodes.get(next);
            if (instructions[steps++ % instructions.length] == 'L') {
                next = node.getLeft();
            } else {
                next = node.getRight();
            }
        } while (!next.equals("ZZZ"));
        return steps;
    }

    public class Node {

        private final String left;
        private final String right;

        public Node(String left, String right) {
            this.left = left;
            this.right = right;
        }

        public String getLeft() {
            return left;
        }

        public String getRight() {
            return right;
        }
    }
}
