package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
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
                (character, position) -> new BlockAtPosition(new Block(character), position)).create(input);
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
        long area = 0L;
        final Map<Direction, List<Position>> fences = new HashMap<>();
        for (final Direction direction : Direction.values()) {
            fences.put(direction, new ArrayList<>());
        }
        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(start);
        start.setFenced();
        do {
            final BlockAtPosition current = queue.poll();
            area++;
            for (final Direction direction : Direction.values()) {
                final Position nextPosition = current.getPosition().move(direction);
                final BlockAtPosition next = map.getElementAt(nextPosition);
                if (current.isFencable(next)) {
                    if (!next.isFenced()) {
                        queue.add(next);
                        // we need to set it fenced already so others in queue won't pick it up
                        next.setFenced();
                    }
                } else {
                    switch (direction) {
                    case NORTH:
                    case WEST:
                        fences.get(direction).add(current.getPosition());
                        break;
                    case SOUTH:
                    case EAST:
                        fences.get(direction).add(nextPosition);
                        break;
                    default:
                        throw new IllegalArgumentException();
                    }
                }
            }
        } while (!queue.isEmpty());

        return area * countSides(fences);
    }

    private final Comparator<? super Position> northComparator = (one, other) -> {
        if (other.getY() != one.getY()) {
            return one.getY() - other.getY();
        }
        return one.getX() - other.getX();
    };
    private final Comparator<? super Position> westComparator = (one, other) -> {
        if (other.getX() != one.getX()) {
            return one.getX() - other.getX();
        }
        return one.getY() - other.getY();
    };

    private long countSides(final Map<Direction, List<Position>> fences) {
        long sides = 0L;
        for (final Direction direction : Direction.values()) {
            switch (direction) {
            case NORTH:
            case SOUTH:
                Collections.sort(fences.get(direction), northComparator);
                sides += countNorth(fences.get(direction));
                break;
            case WEST:
            case EAST:
                Collections.sort(fences.get(direction), westComparator);
                sides += countWest(fences.get(direction));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        return sides;
    }

    private long countNorth(final List<Position> fences) {
        Position lastPos = fences.get(0);
        long sides = 1;
        for (final Position pos : fences) {
            // we're a bit lazy here comparing index 0 against 0 with help of abs()
            if (pos.getY() != lastPos.getY() || Math.abs(pos.getX() - lastPos.getX()) > 1) {
                sides++;
            }
            lastPos = pos;
        }
        return sides;
    }

    private long countWest(final List<Position> fences) {
        Position lastPos = fences.get(0);
        long sides = 1;
        for (final Position pos : fences) {
            // we're a bit lazy here comparing index 0 against 0 with help of abs()
            if (pos.getX() != lastPos.getX() || Math.abs(pos.getY() - lastPos.getY()) > 1) {
                sides++;
            }
            lastPos = pos;
        }
        return sides;
    }

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private boolean fenced;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public boolean isFencable(final BlockAtPosition other) {
            return other != null && other.getElement().getCharacter() == getElement().getCharacter();
        }

        public void setFenced() {
            fenced = true;
        }

        public boolean isFenced() {
            return fenced;
        }
    }

    private static class Block implements CharPrintable {

        private final char character;

        private Block(final char character) {
            this.character = character;
        }

        public char getCharacter() {
            return character;
        }

        @Override
        public char toPrintableChar() {
            return character;
        }
    }
}
