package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day16 {

    public static void main(String[] args) {
        Day16 puzzle = new Day16();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        puzzle.beam();
        System.out.println("Solution: " + puzzle.getEnergizedSum());
    }

    private Contraption contraption;

    public void parseLines(List<String> input) {
        contraption = new Contraption(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                contraption.setPartAt(new Position(x, y), Part.byChar(line.charAt(x)));
            }
        }
    }

    public void beam() {
        beam(contraption.getPartAt(new Position(0, 0)), Direction.EAST);
    }

    private void beam(PartAtPosition current, Direction direction) {
        Set<PartAtPositionWithDirection> processed = new HashSet<>();
        Queue<PartAtPositionWithDirection> queue = new LinkedList<>();
        queue.add(new PartAtPositionWithDirection(current, direction));

        while (!queue.isEmpty()) {
            PartAtPositionWithDirection x = queue.poll();
            processed.add(x);
            current = x.getPartAtPosition();
            direction = x.getDirection();
            current.setEnergized();
            for (Direction newDirection : current.getPart().getDirections(direction)) {
                PartAtPosition next = contraption.getPartAt(current.getPosition().move(newDirection));
                if (next != null) {
                    PartAtPositionWithDirection y = new PartAtPositionWithDirection(next, newDirection);
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
                if (contraption.getPartAt(new Position(x, y)).isEnergized()) {
                    sum++;
                }
            }
        }
        return sum;
    }

    public class Position {

        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Position move(Direction direction) {
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
    }

    public class PartAtPositionWithDirection {
        private final PartAtPosition partAtPosition;
        private final Direction direction;

        public PartAtPositionWithDirection(PartAtPosition partAtPosition, Direction direction) {
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
            return 31 + Objects.hash(direction, partAtPosition);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            PartAtPositionWithDirection other = (PartAtPositionWithDirection) obj;
            return direction == other.direction && Objects.equals(partAtPosition, other.partAtPosition);
        }
    }

    public class PartAtPosition {
        private final Part part;
        private final Position position;

        private boolean energized;

        public PartAtPosition(Part part, Position position) {
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
            this.energized = true;
        }

        public boolean isEnergized() {
            return energized;
        }

        public char getCharacter() {
            return part == Part.EMPTY && isEnergized() ? '#' : part.getCharacter();
        }
    }

    public class Contraption {

        private final PartAtPosition[][] map;

        public Contraption(int xSize, int ySize) {
            map = new PartAtPosition[ySize][xSize];
        }

        public void setPartAt(Position position, Part part) {
            map[position.getY()][position.getX()] = new PartAtPosition(part, position);
        }

        public PartAtPosition getPartAt(Position position) {
            return isInBounds(position) ? map[position.getY()][position.getX()] : null;
        }

        public boolean isInBounds(Position position) {
            return position.getX() >= 0 && position.getY() >= 0 && position.getX() < map[0].length
                    && position.getY() < map.length;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    builder.append(map[y][x].getCharacter());
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

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }

    public enum Part {

        EMPTY('.') {
            @Override
            public Set<Direction> getDirections(Direction direction) {
                return Set.of(direction);
            }
        },
        MIRROR1('/') {
            @Override
            public Set<Direction> getDirections(Direction direction) {
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
            public Set<Direction> getDirections(Direction direction) {
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
            public Set<Direction> getDirections(Direction direction) {
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
            public Set<Direction> getDirections(Direction direction) {
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

        private Part(char character, Direction... directions) {
            this.character = character;
        }

        public char getCharacter() {
            return character;
        }

        public static Part byChar(char c) {
            for (Part part : values()) {
                if (part.getCharacter() == c) {
                    return part;
                }
            }
            throw new IllegalArgumentException("part not found for " + c);
        }

        public abstract Set<Direction> getDirections(Direction direction);
    }
}
