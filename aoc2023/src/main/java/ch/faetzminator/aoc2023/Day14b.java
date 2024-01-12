package ch.faetzminator.aoc2023;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day14b {

    public static void main(final String[] args) {
        final Day14b puzzle = new Day14b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        puzzle.tiltManyTimes();
        final long solution = puzzle.calculateLoadSum();
        PuzzleUtil.end(solution, timer);
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

    private final Map<String, Integer> seen = new HashMap<>();

    public void tiltManyTimes() {
        final int loops = 1000000000;
        for (int i = 0; i < loops; i++) {
            final String checksum = getChecksum();
            if (seen.containsKey(checksum)) {
                final int loopLen = i - seen.get(checksum);
                final int offset = i % loopLen;
                // thanks to int the below works well
                i = loopLen * ((loops - offset) / loopLen) + offset;
                seen.clear();
            }
            seen.put(checksum, i);
            // the below can be done better for sure
            tiltNorth();
            tiltWest();
            tiltSouth();
            tiltEast();
        }
    }

    private String getChecksum() {
        final StringBuilder builder = new StringBuilder();
        for (final char[] line : platform) {
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
