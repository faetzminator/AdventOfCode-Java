package ch.faetzminator.aoc2023;

import java.util.Scanner;

public class Day15 {

    public static void main(String[] args) {
        Day15 puzzle = new Day15();

        String input;
        try (Scanner scanner = new Scanner(System.in)) {
            input = scanner.nextLine();
        }

        System.out.println("Calculating...");
        puzzle.parseInput(input);
        System.out.println("Solution: " + puzzle.getHashSum());
    }

    private long hashSum;

    public void parseInput(String str) {
        for (String value : str.split(",")) {
            hashSum += calculateHash(value);
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

    public long getHashSum() {
        return hashSum;
    }
}
