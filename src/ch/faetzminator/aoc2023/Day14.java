package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day14 {

    public static void main(final String[] args) {
        final Day14 puzzle = new Day14();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        puzzle.tiltNorth();
        System.out.println("Solution: " + puzzle.calculateLoadSum());
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("[#.O]+");
    private static final char ROUNDED_ROCK = 'O';
    private static final char EMPTY_SPACE = '.';

    private char[][] platform;

    public void parseLines(final List<String> lines) {
        platform = new char[lines.size()][];

        for (int y = 0; y < lines.size(); y++) {
            final String line = lines.get(y);
            if (!LINE_PATTERN.matcher(line).matches()) {
                throw new IllegalArgumentException("line: " + line);
            }
            platform[y] = line.toCharArray();
        }
    }

    public void tiltNorth() {
        for (int y = 1; y < platform.length; y++) {
            for (int x = 0; x < platform[y].length; x++) {
                if (platform[y][x] == ROUNDED_ROCK) {
                    int destY = y;
                    while (destY > 0 && platform[destY - 1][x] == EMPTY_SPACE) {
                        destY--;
                    }
                    if (destY < y) {
                        platform[destY][x] = platform[y][x];
                        platform[y][x] = EMPTY_SPACE;
                    }
                }
            }
        }
    }

    public long calculateLoadSum() {
        long sum = 0;
        for (int y = 0; y < platform.length; y++) {
            for (int x = 0; x < platform[y].length; x++) {
                if (platform[y][x] == ROUNDED_ROCK) {
                    sum += platform.length - y;
                }
            }
        }
        return sum;
    }
}
