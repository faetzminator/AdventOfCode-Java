package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.Char;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.ElementAtPosition;
import ch.faetzminator.aocutil.grid.Grid;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.Position;

public class Day18 {

    public static void main(final String[] args) {
        final Day18 puzzle = new Day18();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getMinimumNumberOfSteps();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+)");

    private final static int SIZE = 71;
    private final static int TEST_SIZE = 7;
    private final static int STEPS = 1024;
    private final static int TEST_STEPS = 12;

    private final List<Position> positions = new ArrayList<>();

    public void parseLine(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final int x = Integer.parseInt(matcher.group(1));
        final int y = Integer.parseInt(matcher.group(2));
        positions.add(new Position(x, y));
    }

    public long getMinimumNumberOfSteps() {
        final boolean testData = positions.size() < 50;
        final int size = testData ? TEST_SIZE : SIZE;
        final int steps = testData ? TEST_STEPS : STEPS;

        final Grid<BlockAtPosition> map = new GridFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(position)).create(size, size);

        for (int i = 0; i < steps; i++) {
            map.getAt(positions.get(i)).setCorrupted();
        }

        final BlockAtPosition start = map.getAt(0, 0);
        final BlockAtPosition end = map.getAt(map.getXSize() - 1, map.getYSize() - 1);

        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(start);
        start.setSteps(0);

        while (!queue.isEmpty()) {
            final BlockAtPosition last = queue.poll();
            final int nextSteps = last.getSteps() + 1;
            for (final Direction direction : Direction.values()) {
                final BlockAtPosition block = last.move(map, direction);
                if (block != null && block.setSteps(nextSteps)) {
                    queue.add(block);
                }
            }
        }

        return end.getSteps();
    }

    private static class BlockAtPosition extends ElementAtPosition<Char> {

        private boolean corrupted;
        private int steps = Integer.MAX_VALUE;

        public BlockAtPosition(final Position position) {
            super(new Char('.'), position);
        }

        public void setCorrupted() {
            corrupted = true;
        }

        public int getSteps() {
            return steps;
        }

        public boolean setSteps(final int steps) {
            if (!corrupted && this.steps > steps) {
                this.steps = steps;
                return true;
            }
            return false;
        }

        @Override
        public char toPrintableChar() {
            return corrupted ? '#' : '.';
        }
    }
}
