package ch.faetzminator.aoc2024;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.Char;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.CharAtPosition;
import ch.faetzminator.aocutil.grid.Grid;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.Position;

public class Day10 {

    public static void main(final String[] args) {
        final Day10 puzzle = new Day10();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getTrailheadSum();
        PuzzleUtil.end(solution, timer);
    }

    private Grid<HeightAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new GridFactory<>(HeightAtPosition.class,
                (character, position) -> new HeightAtPosition(new Char(character), position)).create(input);
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
                final HeightAtPosition next = map.getAt(nextPosition);
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

    private static class HeightAtPosition extends CharAtPosition<Char> {

        public HeightAtPosition(final Char height, final Position position) {
            super(height, position);
        }

        public boolean isNextStep(final HeightAtPosition other) {
            return other != null && other.charValue() == charValue() + 1;
        }

        public boolean isStartPoint() {
            return charValue() == '0';
        }

        public boolean isEndPoint() {
            return charValue() == '9';
        }
    }
}
