package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 {

    public static void main(final String[] args) {
        final Day24 puzzle = new Day24();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (final String line : input) {
            puzzle.parseHailstone(line);
        }
        final long solution = puzzle.calculateIntersections();
        System.out.println("Solution: " + solution);
    }

    private final List<Hailstone> hailstones = new ArrayList<>();

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
        final Hailstone hailstone = new Hailstone(px, py, vx, vy);
        hailstones.add(hailstone);
    }

    private static final double MIN = 200000000000000.0;
    private static final double MAX = 400000000000000.0;

    public long calculateIntersections() {
        long sum = 0;

        for (int i = 0; i < hailstones.size(); i++) {
            final Hailstone one = hailstones.get(i);
            for (int j = i + 1; j < hailstones.size(); j++) {
                final Hailstone another = hailstones.get(j);
                if (one.getM() != another.getM()) {
                    final double x = one.getCollisionX(another);
                    final double y = one.getY(x);
                    if (!one.inPast(x) && !another.inPast(x) && x >= MIN && x <= MAX && y >= MIN && y <= MAX) {
                        sum++;
                    }
                }
            }
        }

        return sum;
    }

    private static class Hailstone {

        private final long px;
        private final long vx;

        private final double m;
        private final double b;

        public Hailstone(final long px, final long py, final long vx, final long vy) {
            this.px = px;
            this.vx = vx;

            m = ((double) vy) / ((double) vx);
            b = -(m * px) + py;
        }

        public double getM() {
            return m;
        }

        public double getB() {
            return b;
        }

        public double getCollisionX(final Hailstone other) {
            return (other.getB() - b) / (m - other.getM());
        }

        public double getY(final double x) {
            return m * x + b;
        }

        public boolean inPast(final double x) {
            return vx > 0 ? x < px : x > px;
        }
    }
}
