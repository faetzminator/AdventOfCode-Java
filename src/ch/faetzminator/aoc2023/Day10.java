package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day10 {

    public static void main(final String[] args) {
        final Day10 puzzle = new Day10();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        System.out.println("Solution: " + puzzle.calculateLoopSteps());
    }

    private PipeMap pipeMap;

    public void parseLines(final List<String> input) {
        pipeMap = new PipeMap(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                pipeMap.setPipeAt(new Position(x, y), Pipe.byChar(line.charAt(x)));
            }
        }
    }

    public int calculateLoopSteps() {

        final Position current = pipeMap.getStartPosition();
        final Pipe startPipe = pipeMap.getPipeAt(current);
        for (final Direction direction : startPipe.getDirections()) {
            final int steps = calculateLoopSteps(current, startPipe, direction);
            // we should get twice -1 and twice the same number
            if (steps >= 0) {
                return steps / 2;
            }
        }
        return -1;
    }

    private int calculateLoopSteps(Position currentPos, Pipe currentPipe, Direction direction) {
        int steps = 0;
        while (true) { // don't we love infinite loops?
            final Position nextPos = currentPos.move(direction);
            if (!pipeMap.isInBounds(nextPos)) {
                return -1;
            }
            final Pipe nextPipe = pipeMap.getPipeAt(nextPos);
            if (nextPipe == Pipe.START) {
                return steps + 1;
            }
            final Direction inverseDirection = direction.getOpposite();
            final Set<Direction> nextDirections = nextPipe.getDirections();
            if (!nextDirections.contains(inverseDirection)) {
                return -1;
            }
            for (final Direction nextDirection : nextDirections) {
                if (nextDirection != inverseDirection) {
                    // we know there can only be one other direction (apart from START)
                    steps++;
                    currentPos = nextPos;
                    currentPipe = nextPipe;
                    direction = nextDirection;
                }
            }
        }
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
    }

    private static class PipeMap {

        private final Pipe[][] map;
        private Position startPosition;

        public PipeMap(final int xSize, final int ySize) {
            map = new Pipe[ySize][xSize];
        }

        public void setPipeAt(final Position position, final Pipe pipe) {
            map[position.getY()][position.getX()] = pipe;
            if (pipe == Pipe.START) {
                if (startPosition != null) {
                    throw new IllegalArgumentException("duplicate start");
                }
                startPosition = position;
            }
        }

        public Pipe getPipeAt(final Position position) {
            return map[position.getY()][position.getX()];
        }

        public Position getStartPosition() {
            if (startPosition == null) {
                throw new IllegalArgumentException("start not set");
            }
            return startPosition;
        }

        public boolean isInBounds(final Position position) {
            return position.getX() >= 0 && position.getY() >= 0 && position.getX() < map[0].length
                    && position.getY() < map.length;
        }
    }

    private static enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction getOpposite() {
            switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            }
            throw new IllegalArgumentException();
        }
    }

    private static enum Pipe {

        VERTICAL('|', Direction.NORTH, Direction.SOUTH), HORIZONTAL('-', Direction.EAST, Direction.WEST),
        BEND1('L', Direction.NORTH, Direction.EAST), BEND2('J', Direction.NORTH, Direction.WEST),
        BEND3('7', Direction.SOUTH, Direction.WEST), BEND4('F', Direction.SOUTH, Direction.EAST), GROUND('.'),
        START('S', Direction.values());

        private final char character;
        private final Set<Direction> directions;

        private Pipe(final char character, final Direction... directions) {
            this.character = character;
            this.directions = new LinkedHashSet<>(Arrays.asList(directions));
        }

        public char getCharacter() {
            return character;
        }

        public Set<Direction> getDirections() {
            return directions;
        }

        public static Pipe byChar(final char c) {
            for (final Pipe pipe : values()) {
                if (pipe.getCharacter() == c) {
                    return pipe;
                }
            }
            throw new IllegalArgumentException("pipe not found for " + c);
        }
    }
}
