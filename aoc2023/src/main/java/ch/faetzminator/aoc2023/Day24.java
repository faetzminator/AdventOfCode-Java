package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.MovingPosition;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day24 {

    public static void main(final String[] args) {
        final Day24 puzzle = new Day24();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseHailstone(line);
        }
        final long solution = puzzle.calculateIntersections(MIN, MAX);
        PuzzleUtil.end(solution, timer);
    }

    private final List<MovingPosition> hailstones = new ArrayList<>();

    private static final Pattern LINE_PATTERN = Pattern
            .compile("(-?\\d+), +(-?\\d+), +(-?\\d+) +@ +(-?\\d+), +(-?\\d+), +(-?\\d+)");

    public void parseHailstone(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final long px = Long.parseLong(matcher.group(1));
        final long py = Long.parseLong(matcher.group(2));
        final long vx = Long.parseLong(matcher.group(4));
        final long vy = Long.parseLong(matcher.group(5));
        final MovingPosition hailstone = new MovingPosition(px, py, vx, vy);
        hailstones.add(hailstone);
    }

    private static final double MIN = 200000000000000.0;
    private static final double MAX = 400000000000000.0;

    public long calculateIntersections(final double min, final double max) {
        long sum = 0;

        for (int i = 0; i < hailstones.size(); i++) {
            final MovingPosition one = hailstones.get(i);
            for (int j = i + 1; j < hailstones.size(); j++) {
                final MovingPosition another = hailstones.get(j);
                if (one.getM() != another.getM()) {
                    final double x = one.getCollisionX(another);
                    final double y = one.getY(x);
                    if (!one.inPast(x) && !another.inPast(x) && x >= min && x <= max && y >= min && y <= max) {
                        sum++;
                    }
                }
            }
        }

        return sum;
    }
}
