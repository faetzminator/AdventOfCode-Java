package ch.faetzminator.aoc2023;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15b {

    public static void main(String[] args) {
        Day15b puzzle = new Day15b();

        String input;
        try (Scanner scanner = new Scanner(System.in)) {
            input = scanner.nextLine();
        }

        System.out.println("Calculating...");
        puzzle.parseInput(input);
        System.out.println("Solution: " + puzzle.calculateFocusingPower());
    }

    private static final Pattern LABEL_PATTERN = Pattern.compile("(\\w+)(?:-|=(\\d+))");

    @SuppressWarnings("unchecked")
    private Map<String, Integer>[] boxes = new Map[256];

    public void parseInput(String str) {
        for (String value : str.split(",")) {
            Matcher matcher = LABEL_PATTERN.matcher(value);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("value: " + value);
            }
            String key = matcher.group(1);
            int box = calculateHash(key);
            if (boxes[box] == null) {
                boxes[box] = new LinkedHashMap<>();
            }
            if (value.endsWith("-")) {
                boxes[box].remove(key);
            } else {
                boxes[box].put(key, Integer.parseInt(matcher.group(2)));
            }
        }
    }

    public int calculateHash(String str) {
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash += c;
            hash *= 17;
            hash = hash % 256;
        }
        return hash;
    }

    public long calculateFocusingPower() {
        long focusingPower = 0;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                int slot = 1;
                for (Entry<String, Integer> entry : boxes[i].entrySet()) {
                    focusingPower += (i + 1) * (slot++) * entry.getValue();
                }
            }
        }
        return focusingPower;
    }
}
