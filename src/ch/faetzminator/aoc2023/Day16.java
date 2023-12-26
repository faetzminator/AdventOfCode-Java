package ch.faetzminator.aoc2023;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.ElementAtPosition;
import ch.faetzminator.aocutil.PMap;
import ch.faetzminator.aocutil.Position;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

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
        contraption = new PMap<>(PartAtPosition.class, input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                final PartAtPosition part = new PartAtPosition(Part.byChar(line.charAt(x)), new Position(x, y));
                contraption.setElementAt(part.getPosition(), part);
            }
        }
    }

    public void beam() {
        beam(contraption.getElementAt(new Position(0, 0)), Direction.EAST);
    }

    private void beam(PartAtPosition current, Direction direction) {
        final Set<PartAtPositionWithDirection> processed = new HashSet<>();
        final Queue<PartAtPositionWithDirection> queue = new LinkedList<>();
        queue.add(new PartAtPositionWithDirection(current, direction));

        while (!queue.isEmpty()) {
            final PartAtPositionWithDirection x = queue.poll();
            processed.add(x);
            current = x.getElementAtPosition();
            direction = x.getDirection();
            current.setEnergized();
            for (final Direction newDirection : current.getElement().getDirections(direction)) {
                final PartAtPosition next = contraption.getElementAt(current.getPosition().move(newDirection));
                if (next != null) {
                    final PartAtPositionWithDirection y = new PartAtPositionWithDirection(next, newDirection);
                    if (!processed.contains(y)) {
                        queue.add(y);
                    }
                }
            }
        }
    }

    public long getEnergizedSum() {
        long sum = 0;
        for (int y = 0; y < contraption.getYSize(); y++) {
            for (int x = 0; x < contraption.getXSize(); x++) {
                if (contraption.getElementAt(new Position(x, y)).isEnergized()) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private static class PartAtPositionWithDirection {
        private final PartAtPosition partAtPosition;
        private final Direction direction;

        public PartAtPositionWithDirection(final PartAtPosition partAtPosition, final Direction direction) {
            this.partAtPosition = partAtPosition;
            this.direction = direction;
        }

        public PartAtPosition getElementAtPosition() {
            return partAtPosition;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, partAtPosition);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final PartAtPositionWithDirection other = (PartAtPositionWithDirection) obj;
            return direction == other.direction && Objects.equals(partAtPosition, other.partAtPosition);
        }
    }

    private static class PartAtPosition extends ElementAtPosition<Part> {

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
        public char getCharacter() {
            return character;
        }

        public static Part byChar(final char c) {
            return CharEnum.byChar(Part.class, c);
        }

        public abstract Set<Direction> getDirections(Direction direction);
    }
}
