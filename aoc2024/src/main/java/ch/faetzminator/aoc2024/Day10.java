package ch.faetzminator.aoc2024;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.Position;

public class Day10 {

    public static void main(final String[] args) {
        final Day10 puzzle = new Day10();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getTrailheadSum();
        PuzzleUtil.end(solution, timer);
    }

    private PMap<HeightAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(HeightAtPosition.class,
                (character, position) -> new HeightAtPosition(new Height(character), position)).create(input);
    }

    public long getTrailheadSum() {
        long sum = 0L;
        for (final HeightAtPosition element : map.stream().filter(HeightAtPosition::isStartPoint)
                .collect(Collectors.toList())) {
            sum += getTrailheadSum(element);
        }
        return sum;
    }

    private long getTrailheadSum(final HeightAtPosition start) {
        final Queue<HeightAtPosition> queue = new LinkedList<>();
        queue.add(start);
        final Set<HeightAtPosition> ends = new HashSet<>();

        while (!queue.isEmpty()) {
            final HeightAtPosition current = queue.poll();
            for (final Direction direction : Direction.values()) {
                final Position nextPosition = current.getPosition().move(direction);
                final HeightAtPosition next = map.getElementAt(nextPosition);
                if (current.isNextStep(next)) {
                    if (next.isEndPoint()) {
                        ends.add(next);
                    } else {
                        queue.add(next);
                    }
                }
            }
        }
        return ends.size();
    }

    private static class HeightAtPosition extends ElementAtPosition<Height> {

        public HeightAtPosition(final Height height, final Position position) {
            super(height, position);
        }

        public boolean isNextStep(final HeightAtPosition other) {
            return other != null && other.getElement().getHeight() == getElement().getHeight() + 1;
        }

        public boolean isStartPoint() {
            return getElement().getHeight() == '0';
        }

        public boolean isEndPoint() {
            return getElement().getHeight() == '9';
        }
    }

    private static class Height implements CharPrintable {

        private final char height;

        public Height(final char height) {
            this.height = height;
        }

        public char getHeight() {
            return height;
        }

        @Override
        public char toPrintableChar() {
            return height;
        }
    }
}
