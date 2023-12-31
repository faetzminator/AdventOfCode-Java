package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.MovingPosition;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day24b {

    public static void main(final String[] args) {
        final Day24b puzzle = new Day24b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseHailstone(line);
        }
        final long solution = puzzle.throwStone();
        PuzzleUtil.end(solution, timer);
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
        final long pz = Long.parseLong(matcher.group(3));
        final long vx = Long.parseLong(matcher.group(4));
        final long vy = Long.parseLong(matcher.group(5));
        final long vz = Long.parseLong(matcher.group(6));
        final Hailstone hailstone = new Hailstone(px, py, pz, vx, vy, vz);
        hailstones.add(hailstone);
    }

    private static long[] gaussianElimination(final boolean forZ, final Hailstone one, final Hailstone... more) {
        final double[][] m = new double[more.length][4];
        final double[] b = new double[more.length];

        for (int i = 0; i < 4; i++) {
            final Hailstone other = more[i];
            m[i][0] = forZ ? one.getPz() - other.getPz() : one.getPy() - other.getPy();
            m[i][1] = one.getPx() - other.getPx();
            m[i][2] = forZ ? other.getVz() - one.getVz() : other.getVy() - one.getVy();
            m[i][3] = one.getVx() - other.getVx();
            b[i] = one.getVxD() * (forZ ? one.getPzD() : one.getPyD())
                    - one.getPxD() * (forZ ? one.getVzD() : one.getVyD())
                    - other.getVxD() * (forZ ? other.getPzD() : other.getPyD())
                    + other.getPxD() * (forZ ? other.getVzD() : other.getVyD());
        }

        final double[] result = MathUtil.gaussianElimination(m, b);
        // round half-up
        return new long[] { (long) (result[2] + 0.5d), (long) (result[3] + 0.5d) };
    }

    public long throwStone() {
        final long[] xyResult = gaussianElimination(false, hailstones.get(0), hailstones.get(1), hailstones.get(2),
                hailstones.get(3), hailstones.get(4));
        final long[] xzResult = gaussianElimination(true, hailstones.get(0), hailstones.get(1), hailstones.get(2),
                hailstones.get(3), hailstones.get(4));

        return xyResult[0] + xyResult[1] + xzResult[1];
    }

    private static class Hailstone extends MovingPosition {

        private final long pz;
        private final long vz;

        public Hailstone(final long px, final long py, final long pz, final long vx, final long vy, final long vz) {
            super(px, py, vx, vy);
            this.pz = pz;
            this.vz = vz;
        }

        public long getPz() {
            return pz;
        }

        public double getPzD() {
            return pz;
        }

        public long getVz() {
            return vz;
        }

        public double getVzD() {
            return vz;
        }
    }
}
