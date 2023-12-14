package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day14b {

    public static void main(String[] args) {
        Day14b puzzle = new Day14b();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        puzzle.tiltManyTimes();
        System.out.println("Solution: " + puzzle.calculateLoadSum());
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("[#.O]+");
    private static final char ROUNDED_ROCK = 'O';
    private static final char CUBE_SHAPED_ROCK = '#';
    private static final char EMPTY_SPACE = '.';

    private char[][] platform;

    public void parseLines(List<String> lines) {
        platform = new char[lines.size()][];

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            if (!LINE_PATTERN.matcher(line).matches()) {
                throw new IllegalArgumentException("line: " + line);
            }
            platform[y] = line.toCharArray();
        }
    }

    private Map<String, Integer> seen = new HashMap<>();

    public void tiltManyTimes() {
        final int loops = 1000000000;
        for (int i = 0; i < loops; i++) {
            String checksum = getChecksum();
            if (seen.containsKey(checksum)) {
                int originalI = i;
                System.out.println(checksum);
                System.out.println("Rep " + i + " from " + seen.get(checksum));
                int diff = i - seen.get(checksum);
                do {
                    i += diff;
                } while (i < loops);
                i -= diff;
                seen.clear();
                if (originalI < i) {
                    System.out.println("jumped " + originalI + " to " + i + " with step " + diff);
                }
            } else {
                tiltNorth();
                tiltWest();
                tiltSouth();
                tiltEast();
                seen.put(checksum, i);
            }

//            for (char[] line : platform) {
//                System.out.println(String.copyValueOf(line));
//            }
//            System.out.println();
        }
    }

    private String getChecksum() {
        StringBuilder builder = new StringBuilder();
        for (char[] line : platform) {
            builder.append(line);
        }
        return builder.toString();
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

    private void tiltWest() {
        for (int y = 0; y < platform.length; y++) {
            for (int x = 1; x < platform[y].length; x++) {
                if (platform[y][x] == ROUNDED_ROCK) {
                    int destX = x;
                    while (destX > 0 && platform[y][destX - 1] == EMPTY_SPACE) {
                        destX--;
                    }
                    if (destX < x) {
                        platform[y][destX] = platform[y][x];
                        platform[y][x] = EMPTY_SPACE;
                    }
                }
            }
        }
    }

    private void tiltSouth() {
        for (int y = platform.length - 2; y >= 0; y--) {
            for (int x = 0; x < platform[y].length; x++) {
                if (platform[y][x] == ROUNDED_ROCK) {
                    int destY = y;
                    while (destY < platform.length - 1 && platform[destY + 1][x] == EMPTY_SPACE) {
                        destY++;
                    }
                    if (destY > y) {
                        platform[destY][x] = platform[y][x];
                        platform[y][x] = EMPTY_SPACE;
                    }
                }
            }
        }
    }

    private void tiltEast() {
        for (int y = 0; y < platform.length; y++) {
            for (int x = platform[y].length - 2; x >= 0; x--) {
                if (platform[y][x] == ROUNDED_ROCK) {
                    int destX = x;
                    while (destX < platform[y].length - 1 && platform[y][destX + 1] == EMPTY_SPACE) {
                        destX++;
                    }
                    if (destX > x) {
                        platform[y][destX] = platform[y][x];
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
