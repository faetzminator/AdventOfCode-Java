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

public class Day18b {

    public static void main(final String[] args) {
        final Day18b puzzle = new Day18b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final String solution = puzzle.getPreventingByteCoordinates();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+)");

    private final static int SIZE = 71;
    private final static int TEST_SIZE = 7;

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

    public String getPreventingByteCoordinates() {
        final boolean testData = positions.size() < 50;
        final int size = testData ? TEST_SIZE : SIZE;

        final Grid<BlockAtPosition> map = new GridFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(position)).create(size, size);


        int workingIndex = -1;
        int brokenIndex = positions.size();
        int currentIndex = 0;

        while (workingIndex != brokenIndex - 1) {
            currentIndex = workingIndex + (brokenIndex - workingIndex) / 2;
            map.stream().forEach(BlockAtPosition::reset);
            for (int i = 0; i <= currentIndex; i++) {
                map.getAt(positions.get(i)).setCorrupted();
            }
            if (!isExitReachable(map)) {
                brokenIndex = currentIndex;
            } else {
                workingIndex = currentIndex;
            }
        }

        final Position position = positions.get(currentIndex);
        return new StringBuilder().append(position.getX()).append(',').append(position.getY()).toString();
    }

    private boolean isExitReachable(final Grid<BlockAtPosition> map) {

        final BlockAtPosition start = map.getAt(0, 0);
        final BlockAtPosition end = map.getAt(map.getXSize() - 1, map.getYSize() - 1);

        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(start);
        if (!start.step()) {
            return false;
        }

        while (!queue.isEmpty()) {
            final BlockAtPosition last = queue.poll();
            for (final Direction direction : Direction.values()) {
                final BlockAtPosition block = last.move(map, direction);
                if (block != null && block.step()) {
                    queue.add(block);
                }
            }
        }
        return end.isStepped();
    }

    private static class BlockAtPosition extends ElementAtPosition<Char> {

        private boolean corrupted;
        private boolean stepped;

        public BlockAtPosition(final Position position) {
            super(new Char('.'), position);
        }

        public void setCorrupted() {
            corrupted = true;
        }

        public boolean step() {
            if (!stepped && !corrupted) {
                stepped = true;
                return true;
            }
            return false;
        }

        public boolean isStepped() {
            return stepped;
        }

        public void reset() {
            corrupted = false;
            stepped = false;
        }

        @Override
        public char toPrintableChar() {
            return corrupted ? '#' : '.';
        }
    }
}
