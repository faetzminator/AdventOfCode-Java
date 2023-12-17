package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day16b {

    public static void main(final String[] args) {
        final Day16b puzzle = new Day16b();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        puzzle.beam();
        System.out.println("Solution: " + puzzle.getHighestEnergizedSum());
    }

    private Contraption contraption;
    private long highestEnergizedSum;

    public void parseLines(final List<String> input) {
        contraption = new Contraption(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                contraption.setPartAt(new Position(x, y), Part.byChar(line.charAt(x)));
            }
        }
    }

    public void beam() {
        for (int y = 0; y < contraption.getYSize(); y++) {
            beam(contraption.getPartAt(new Position(0, y)), Direction.EAST);
            updateEnergizedSumAndReset();
            beam(contraption.getPartAt(new Position(contraption.getXSize() - 1, y)), Direction.WEST);
            updateEnergizedSumAndReset();
        }
        for (int x = 0; x < contraption.getXSize(); x++) {
            beam(contraption.getPartAt(new Position(x, 0)), Direction.SOUTH);
            updateEnergizedSumAndReset();
            beam(contraption.getPartAt(new Position(x, contraption.getYSize() - 1)), Direction.NORTH);
            updateEnergizedSumAndReset();
        }
    }

    private void beam(PartAtPosition current, Direction direction) {
        final Set<PartAtPositionWithDirection> processed = new HashSet<>();
        final Queue<PartAtPositionWithDirection> queue = new LinkedList<>();
        queue.add(new PartAtPositionWithDirection(current, direction));

        while (!queue.isEmpty()) {
            final PartAtPositionWithDirection x = queue.poll();
            processed.add(x);
            current = x.getPartAtPosition();
            direction = x.getDirection();
            current.setEnergized();
            for (final Direction newDirection : current.getPart().getDirections(direction)) {
                final PartAtPosition next = contraption.getPartAt(current.getPosition().move(newDirection));
                if (next != null) {
                    final PartAtPositionWithDirection y = new PartAtPositionWithDirection(next, newDirection);
                    if (!processed.contains(y)) {
                        queue.add(y);
                    }
                }
            }
        }
    }

    private void updateEnergizedSumAndReset() {
        long sum = 0;
        for (int y = 0; y < contraption.getYSize(); y++) {
            for (int x = 0; x < contraption.getXSize(); x++) {
                final PartAtPosition partAtPosition = contraption.getPartAt(new Position(x, y));
                if (partAtPosition.isEnergized()) {
                    sum++;
                }
                partAtPosition.reset();
            }
        }
        if (sum > highestEnergizedSum) {
            highestEnergizedSum = sum;
        }
    }

    public long getHighestEnergizedSum() {
        return highestEnergizedSum;
    }

    private static class Position {

        private final int x;
        private final int y;

        public Position(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Position move(final Direction direction) {
            switch (direction) {
            case NORTH:
                return new Position(x, y - 1);
            case EAST:
                return new Position(x + 1, y);
            case SOUTH:
                return new Position(x, y + 1);
            case WEST:
                return new Position(x - 1, y);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final Position other = (Position) obj;
            return x == other.x && y == other.y;
        }
    }

    private static class PartAtPositionWithDirection {
        private final PartAtPosition partAtPosition;
        private final Direction direction;

        public PartAtPositionWithDirection(final PartAtPosition partAtPosition, final Direction direction) {
            this.partAtPosition = partAtPosition;
            this.direction = direction;
        }

        public PartAtPosition getPartAtPosition() {
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

    private static class PartAtPosition {
        private final Part part;
        private final Position position;

        private boolean energized;

        public PartAtPosition(final Part part, final Position position) {
            this.part = part;
            this.position = position;
        }

        public Part getPart() {
            return part;
        }

        public Position getPosition() {
            return position;
        }

        public void setEnergized() {
            energized = true;
        }

        public boolean isEnergized() {
            return energized;
        }

        public void reset() {
            energized = false;
        }

        public char getCharacter() {
            return part == Part.EMPTY && isEnergized() ? '#' : part.getCharacter();
        }

        @Override
        public int hashCode() {
            return Objects.hash(energized, part, position);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final PartAtPosition other = (PartAtPosition) obj;
            return energized == other.energized && part == other.part && Objects.equals(position, other.position);
        }
    }

    private static class Contraption {

        private final PartAtPosition[][] map;

        public Contraption(final int xSize, final int ySize) {
            map = new PartAtPosition[ySize][xSize];
        }

        public void setPartAt(final Position position, final Part part) {
            map[position.getY()][position.getX()] = new PartAtPosition(part, position);
        }

        public PartAtPosition getPartAt(final Position position) {
            return isInBounds(position) ? map[position.getY()][position.getX()] : null;
        }

        public boolean isInBounds(final Position position) {
            return position.getX() >= 0 && position.getY() >= 0 && position.getX() < map[0].length
                    && position.getY() < map.length;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            for (final PartAtPosition[] element : map) {
                for (final PartAtPosition element2 : element) {
                    builder.append(element2.getCharacter());
                }
                builder.append('\n');
            }
            builder.setLength(builder.length() - 1);
            return builder.toString();
        }

        public int getXSize() {
            return map[0].length;
        }

        public int getYSize() {
            return map.length;
        }
    }

    private static enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }

    private static enum Part {

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

        private Part(final char character, final Direction... directions) {
            this.character = character;
        }

        public char getCharacter() {
            return character;
        }

        public static Part byChar(final char c) {
            for (final Part part : values()) {
                if (part.getCharacter() == c) {
                    return part;
                }
            }
            throw new IllegalArgumentException("part not found for " + c);
        }

        public abstract Set<Direction> getDirections(Direction direction);
    }
}
