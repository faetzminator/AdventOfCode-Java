package ch.faetzminator.aoc2023;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.CharEnumAtPosition;
import ch.faetzminator.aocutil.map.ElementAtPositionWithDirection;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.Position;

public class Day16 {

    public static void main(final String[] args) {
        final Day16 puzzle = new Day16();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        puzzle.beam();
        final long solution = puzzle.getEnergizedSum();
        PuzzleUtil.end(solution, timer);
    }

    private PMap<PartAtPosition> contraption;

    public void parseLines(final List<String> input) {
        contraption = new PMapFactory<>(PartAtPosition.class,
                (character, position) -> new PartAtPosition(Part.byChar(character), position)).create(input);

    }

    public void beam() {
        beam(contraption.getElementAt(new Position(0, 0)), Direction.EAST);
    }

    private void beam(PartAtPosition current, Direction direction) {
        final Set<ElementAtPositionWithDirection<PartAtPosition>> processed = new HashSet<>();
        final Queue<ElementAtPositionWithDirection<PartAtPosition>> queue = new LinkedList<>();
        queue.add(new ElementAtPositionWithDirection<>(current, direction));

        while (!queue.isEmpty()) {
            final ElementAtPositionWithDirection<PartAtPosition> x = queue.poll();
            processed.add(x);
            current = x.getElementAtPosition();
            direction = x.getDirection();
            current.setEnergized();
            for (final Direction newDirection : current.getElement().getDirections(direction)) {
                final PartAtPosition next = contraption.getElementAt(current.getPosition().move(newDirection));
                if (next != null) {
                    final ElementAtPositionWithDirection<PartAtPosition> y = new ElementAtPositionWithDirection<>(next,
                            newDirection);
                    if (!processed.contains(y)) {
                        queue.add(y);
                    }
                }
            }
        }
    }

    public long getEnergizedSum() {
        return contraption.stream().filter(PartAtPosition::isEnergized).count();
    }

    private static class PartAtPosition extends CharEnumAtPosition<Part> {

        private boolean energized;

        public PartAtPosition(final Part part, final Position position) {
            super(part, position);
        }

        public void setEnergized() {
            energized = true;
        }

        public boolean isEnergized() {
            return energized;
        }

        @Override
        public char toPrintableChar() {
            return getElement() == Part.EMPTY && isEnergized() ? '#' : super.toPrintableChar();
        }
    }

    private static enum Part implements CharEnum {

        EMPTY('.') {
            @Override
            public Set<Direction> getDirections(final Direction direction) {
                return Set.of(direction);
            }
        },
        MIRROR1('/') {
            @Override
            public Set<Direction> getDirections(final Direction direction) {
                switch (direction) {
                case NORTH:
                    return Set.of(Direction.EAST);
                case EAST:
                    return Set.of(Direction.NORTH);
                case WEST:
                    return Set.of(Direction.SOUTH);
                case SOUTH:
                    return Set.of(Direction.WEST);
                default:
                    throw new IllegalArgumentException();
                }
            }
        },
        MIRROR2('\\') {
            @Override
            public Set<Direction> getDirections(final Direction direction) {
                switch (direction) {
                case NORTH:
                    return Set.of(Direction.WEST);
                case EAST:
                    return Set.of(Direction.SOUTH);
                case WEST:
                    return Set.of(Direction.NORTH);
                case SOUTH:
                    return Set.of(Direction.EAST);
                default:
                    throw new IllegalArgumentException();
                }
            }
        },
        SPLITTER1('|') {
            @Override
            public Set<Direction> getDirections(final Direction direction) {
                switch (direction) {
                case NORTH:
                case SOUTH:
                    return Set.of(direction);
                case EAST:
                case WEST:
                    return Set.of(Direction.NORTH, Direction.SOUTH);
                default:
                    throw new IllegalArgumentException();
                }
            }
        },
        SPLITTER2('-') {
            @Override
            public Set<Direction> getDirections(final Direction direction) {
                switch (direction) {
                case EAST:
                case WEST:
                    return Set.of(direction);
                case NORTH:
                case SOUTH:
                    return Set.of(Direction.EAST, Direction.WEST);
                default:
                    throw new IllegalArgumentException();
                }
            }
        };

        private final char character;

        private Part(final char character) {
            this.character = character;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Part byChar(final char c) {
            return CharEnum.byChar(Part.class, c);
        }

        public abstract Set<Direction> getDirections(Direction direction);
    }
}
