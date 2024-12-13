package ch.faetzminator.aoc2024;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.faetzminator.aocutil.Char;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.CharAtPosition;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.Position;

public class Day12b {

    public static void main(final String[] args) {
        final Day12b puzzle = new Day12b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getPriceSum();
        PuzzleUtil.end(solution, timer);
    }

    private PMap<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(new Char(character), position)).create(input);
    }

    public long getPriceSum() {
        long sum = 0L;
        for (final BlockAtPosition block : map) {
            if (!block.isFenced()) {
                sum += getPrice(block);
            }
        }
        return sum;

    }

    private long getPrice(final BlockAtPosition start) {
        long area = 0L, corners = 0L;
        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(start);
        start.setFenced();
        do {
            final BlockAtPosition current = queue.poll();
            area++;
            for (final Direction direction : Direction.values()) {
                final BlockAtPosition next = current.move(map, direction);
                final boolean fencable = current.isFencable(next);
                if (fencable) {
                    if (!next.isFenced()) {
                        queue.add(next);
                        // we need to set it fenced already so others in queue won't pick it up
                        next.setFenced();
                    }
                }
                // for a map, where C = current, N = next and 1 is the corner element and 2 clockwise moved
                // N1
                // C2
                final Direction clockwiseDirection = direction.getClockwise();
                final BlockAtPosition corner = next != null ? next.move(map, clockwiseDirection) : null;
                // first we need to check if N or 1 are not fencable, so outside
                if (!current.isFencable(corner) || !fencable) {
                    final BlockAtPosition clockwise = current.move(map, clockwiseDirection);
                    // then both elements N and 2 need to match
                    if (fencable == current.isFencable(clockwise)) {
                        corners++;
                    }
                }
            }
        } while (!queue.isEmpty());
        return area * corners;
    }


    private static class BlockAtPosition extends CharAtPosition<Char> {

        private boolean fenced;

        public BlockAtPosition(final Char block, final Position position) {
            super(block, position);
        }

        public boolean isFencable(final BlockAtPosition other) {
            return other != null && other.charValue() == charValue();
        }

        public void setFenced() {
            fenced = true;
        }

        public boolean isFenced() {
            return fenced;
        }
    }
}
