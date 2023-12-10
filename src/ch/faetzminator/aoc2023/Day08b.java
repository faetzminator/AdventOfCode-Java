package ch.faetzminator.aoc2023;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08b {

    public static void main(String[] args) {
        Day08b puzzle = new Day08b();

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

    private String[] findStartKeys() {
        List<String> keys = new ArrayList<>();
        for (String key : nodes.keySet()) {
            if (key.endsWith("A")) {
                keys.add(key);
            }
        }
        return keys.toArray(new String[keys.size()]);
    }

    private boolean atEnd(int[] data) {
        for (int value : data) {
            if (value == 0) {
                return false;
            }
        }
        return true;
    }

    public long calculateSteps() {
        int steps = 0;
        String[] next = findStartKeys();

        int[] shift = new int[next.length];
        int[] loop = new int[next.length];

        do {
            for (int i = 0; i < next.length; i++) {
                Node node = nodes.get(next[i]);
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

        BigInteger currentLcm = BigInteger.ONE;
        for (int i = 0; i < loop.length; i++) {
            if (loop[i] != shift[i]) {
                throw new IllegalArgumentException("basic LCM won't work for given input");
            }
            currentLcm = lcm(currentLcm, BigInteger.valueOf(loop[i]));
        }

        return currentLcm.longValueExact();
    }

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        BigInteger gcd = number1.gcd(number2);
        BigInteger absProduct = number1.multiply(number2).abs();
        return absProduct.divide(gcd);
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
