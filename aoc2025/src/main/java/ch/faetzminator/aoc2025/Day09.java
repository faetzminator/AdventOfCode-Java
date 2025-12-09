package ch.faetzminator.aoc2025;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.Point;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day09 {

    public static void main(final String[] args) {
        final Day09 puzzle = new Day09();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+)");

    private final List<Point> points = new ArrayList<>();

    public void parseLine(final String input) {
        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final int x = Integer.parseInt(matcher.group(1));
        final int y = Integer.parseInt(matcher.group(2));
        points.add(new Point(x, y));
    }

    public long getSolution() {
        long area = -1L;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                area = Math.max(area, getArea(points.get(i), points.get(j)));
            }
        }
        return area;
    }

    private long getArea(final Point a, final Point b) {
        final long width = Math.abs(a.getX() - b.getX()) + 1;
        final long height = Math.abs(a.getY() - b.getY()) + 1;
        return width * height;
    }
}
