package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.Point;

public class Day18 {

    public static void main(final String[] args) {
        final Day18 puzzle = new Day18();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        final long sum = puzzle.parseLines(input);
        System.out.println("Solution: " + sum);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("([UDLR]) (\\d+) \\(#[0-9a-f]{6}\\)");

    // additional 1 needed
    private long extraThickShoelaces = 1;

    public long parseLines(final List<String> lines) {
        double shoelaceSum = 0.0;

        final Point firstPoint = new Point(0, 0);
        Point lastPoint = firstPoint;
        for (final String line : lines) {
            final Matcher matcher = LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("line: " + line);
            }
            final Point point = getNextPoint(lastPoint, matcher);
            shoelaceSum += lastPoint.getXD() * point.getYD() - point.getXD() * lastPoint.getYD();
            lastPoint = point;
        }
        shoelaceSum += lastPoint.getXD() * firstPoint.getYD() - firstPoint.getXD() * lastPoint.getYD();

        return extraThickShoelaces + (long) Math.abs(shoelaceSum / 2.0);
    }

    private Point getNextPoint(final Point lastPoint, final Matcher matcher) {
        final char direction = matcher.group(1).charAt(0);
        final int length = Integer.parseInt(matcher.group(2));
        int x = 0, y = 0;
        switch (direction) {
        case 'U':
            y -= length;
            break;
        case 'D':
            y += length;
            extraThickShoelaces += length;
            break;
        case 'L':
            x -= length;
            break;
        case 'R':
            x += length;
            extraThickShoelaces += length;
            break;
        default:
            throw new IllegalArgumentException();
        }
        return new Point(lastPoint.getX() + x, lastPoint.getY() + y);
    }
}
