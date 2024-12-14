package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.MovingPosition;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.Position;

public class Day14 {

    public static void main(final String[] args) {
        final Day14 puzzle = new Day14();

        final List<String> input = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String lines : input) {
            puzzle.parseLine(lines);
        }
        final long solution = puzzle.getSafetyFactor();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");

    private final List<MovingPosition> robots = new ArrayList<>();

    public void parseLine(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final long px = Long.parseLong(matcher.group(1));
        final long py = Long.parseLong(matcher.group(2));
        final long vx = Long.parseLong(matcher.group(3));
        final long vy = Long.parseLong(matcher.group(4));
        robots.add(new MovingPosition(px, py, vx, vy));
    }

    private final static int WIDTH = 101;
    private final static int HEIGHT = 103;
    private final static int TEST_WIDTH = 11;
    private final static int TEST_HEIGHT = 7;
    private final static int STEPS = 100;

    public long getSafetyFactor() {
        final boolean testData = robots.size() < 50;
        final int w = testData ? TEST_WIDTH : WIDTH;
        final int h = testData ? TEST_HEIGHT : HEIGHT;

        final MovingHelper helper = new MovingHelper(w, h, STEPS);

        final PMap<Block> map = new PMap<>(Block.class, w, h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                map.setElementAt(x, y, new Block());
            }
        }

        for (final MovingPosition robot : robots) {
            map.getElementAt(helper.move(robot)).increment();
        }

        final int w1 = w / 2;
        final int h1 = h / 2;
        final long ul = countRobots(map, 0, w1, 0, h1);
        final long ur = countRobots(map, w1 + 1, w, 0, h1);
        final long ll = countRobots(map, 0, w1, h1 + 1, h);
        final long lr = countRobots(map, w1 + 1, w, h1 + 1, h);
        return ul * ur * ll * lr;
    }

    private long countRobots(final PMap<Block> map, final int xFrom, final int xTo, final int yFrom, final int yTo) {
        long sum = 0L;
        for (int y = yFrom; y < yTo; y++) {
            for (int x = xFrom; x < xTo; x++) {
                sum += map.getElementAt(x, y).getCount();
            }
        }
        return sum;
    }

    private static class MovingHelper {

        final int w;
        final int h;
        final long steps;

        public MovingHelper(final int w, final int h, final long steps) {
            this.w = w;
            this.h = h;
            this.steps = steps;
        }

        public Position move(final MovingPosition position) {
            final long newX = position.getPx() + position.getVx() * steps;
            final long newY = position.getPy() + position.getVy() * steps;
            // handle negative velocity by adding w/h by number of steps
            return new Position((int) ((newX + steps * w) % w), (int) ((newY + steps * h) % h));
        }
    }

    private static class Block implements CharPrintable {

        int count = 0;

        public int getCount() {
            return count;
        }

        public void increment() {
            count++;
        }

        @Override
        public char toPrintableChar() {
            return count > 0 ? (char) ('0' + count) : '.';
        }
    }
}
