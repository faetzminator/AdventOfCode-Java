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

public class Day04b {

    public static void main(String[] args) {
        Day04b puzzle = new Day04b();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.init(input.size());
        for (String line : input) {
            puzzle.addScratchcard(line);
        }
        System.out.println("Solution: " + puzzle.getScratchcardSum());
    }

    private int[] cards;
    private int size;

    private static final Pattern LINE_PATTERN = Pattern.compile("Card +(\\d+): +(.*?) \\| +(.*?)");

    public void init(int totalCards) {
        cards = new int[totalCards];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = 1;
        }
    }

    public void addScratchcard(String str) {
        Matcher matcher = LINE_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + str);
        }

        Set<String> winningNumbers = new LinkedHashSet<>(Arrays.asList(matcher.group(2).split(" +")));
        Set<String> numbers = new HashSet<>(Arrays.asList(matcher.group(3).split(" +")));

        winningNumbers.retainAll(numbers);
        int hits = winningNumbers.size();

        int position = size++;
        for (int i = hits; i > 0; i--) {
            cards[position + i] += cards[position];
        }
    }

    public long getScratchcardSum() {
        long sum = 0;
        for (int size : cards) {
            sum += size;
        }
        return sum;
    }
}
