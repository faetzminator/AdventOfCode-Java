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

public class Day14b {

    public static void main(final String[] args) {
        final boolean print = args.length > 0 && Boolean.parseBoolean(args[0]);
        final Day14b puzzle = new Day14b();
        final List<String> input = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String lines : input) {
            puzzle.parseLine(lines);
        }
        final long solution = puzzle.getXmasTreeTime(print);
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
    private final static int MAX_STEPS = WIDTH * HEIGHT;

    public long getXmasTreeTime(final boolean print) {

        final MovingHelper helper = new MovingHelper(WIDTH, HEIGHT);

        final PMap<Block> map = new PMap<>(Block.class, WIDTH, HEIGHT);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                map.setElementAt(x, y, new Block());
            }
        }

        for (int steps = 0; steps < MAX_STEPS; steps++) {
            map.forEach(Block::reset);
            helper.setSteps(steps);

            for (final MovingPosition robot : robots) {
                map.getElementAt(helper.move(robot)).setOccupied();
            }
            if (containsHorizontalLine(map, 10)) {
                if (print) {
                    System.out.println(map);
                }
                return steps;
            }
        }
        return -1L;
    }

    private boolean containsHorizontalLine(final PMap<Block> map, final int limit) {
        int count = 0;
        // we simply ignore that we find lines in two rows
        for (final Block element : map) {
            if (element.isOccupied()) {
                if (++count == limit) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    private static class MovingHelper {

        private final int w;
        private final int h;
        private long steps;

        public MovingHelper(final int w, final int h) {
            this.w = w;
            this.h = h;
        }

        public void setSteps(final long steps) {
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

        private boolean occupied;

        public boolean isOccupied() {
            return occupied;
        }

        public void setOccupied() {
            occupied = true;
        }

        public void reset() {
            occupied = false;
        }

        @Override
        public char toPrintableChar() {
            return occupied ? '*' : '.';
        }
    }
}
