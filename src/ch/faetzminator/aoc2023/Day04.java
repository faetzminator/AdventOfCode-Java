package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    public static void main(String[] args) {
        Day04 puzzle = new Day04();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        for (String line : input) {
            puzzle.addScratchcard(line);
        }
        System.out.println("Solution: " + puzzle.getWinningSum());
    }

    private long winningSum;

    private static final Pattern LINE_PATTERN = Pattern.compile("Card +(\\d+): +(.*?) \\| +(.*?)");

    public void addScratchcard(String str) {
        Matcher matcher = LINE_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + str);
        }

        Set<String> winningNumbers = new LinkedHashSet<>(Arrays.asList(matcher.group(2).split(" +")));
        Set<String> numbers = new HashSet<>(Arrays.asList(matcher.group(3).split(" +")));

        winningNumbers.retainAll(numbers);
        int hits = winningNumbers.size();
        if (hits > 0) {
            winningSum += (int) Math.pow(2, hits - 1);
        }
    }

    public long getWinningSum() {
        return winningSum;
    }
}
