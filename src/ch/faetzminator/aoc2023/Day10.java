package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day10 {

    public static void main(String[] args) {
        Day10 puzzle = new Day10();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        System.out.println("Solution: " + puzzle.calculateLoopSteps());
    }

    private PipeMap pipeMap;

    public void parseLines(List<String> input) {
        pipeMap = new PipeMap(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                pipeMap.setPipeAt(new Position(x, y), Pipe.byChar(line.charAt(x)));
            }
        }
    }

    public int calculateLoopSteps() {

        Position current = pipeMap.getStartPosition();
        Pipe startPipe = pipeMap.getPipeAt(current);
        for (Direction direction : startPipe.getDirections()) {
            int steps = calculateLoopSteps(current, startPipe, direction);
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
            Position nextPos = currentPos.move(direction);
            if (!pipeMap.isInBounds(nextPos)) {
                return -1;
            }
            Pipe nextPipe = pipeMap.getPipeAt(nextPos);
            if (nextPipe == Pipe.START) {
                return steps + 1;
            }
            Direction inverseDirection = direction.getOpposite();
            Set<Direction> nextDirections = nextPipe.getDirections();
            if (!nextDirections.contains(inverseDirection)) {
                return -1;
            }
            for (Direction nextDirection : nextDirections) {
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

    public class PipeMap {

        private final Pipe[][] map;
        private Position startPosition;

        public PipeMap(int xSize, int ySize) {
            map = new Pipe[ySize][xSize];
        }

        public void setPipeAt(Position position, Pipe pipe) {
            map[position.getY()][position.getX()] = pipe;
            if (pipe == Pipe.START) {
                if (startPosition != null) {
                    throw new IllegalArgumentException("duplicate start");
                }
                this.startPosition = position;
            }
        }

        public Pipe getPipeAt(Position position) {
            return map[position.getY()][position.getX()];
        }

        public Position getStartPosition() {
            if (startPosition == null) {
                throw new IllegalArgumentException("start not set");
            }
            return startPosition;
        }

        public boolean isInBounds(Position position) {
            return position.getX() >= 0 && position.getY() >= 0 && position.getX() < map[0].length
                    && position.getY() < map.length;
        }
    }

    public enum Direction {
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

    public enum Pipe {

        VERTICAL('|', Direction.NORTH, Direction.SOUTH), HORIZONTAL('-', Direction.EAST, Direction.WEST),
        BEND1('L', Direction.NORTH, Direction.EAST), BEND2('J', Direction.NORTH, Direction.WEST),
        BEND3('7', Direction.SOUTH, Direction.WEST), BEND4('F', Direction.SOUTH, Direction.EAST), GROUND('.'),
        START('S', Direction.values());

        private final char character;
        private final Set<Direction> directions;

        private Pipe(char character, Direction... directions) {
            this.character = character;
            this.directions = new LinkedHashSet<>(Arrays.asList(directions));
        }

        public char getCharacter() {
            return character;
        }

        public Set<Direction> getDirections() {
            return directions;
        }

        public static Pipe byChar(char c) {
            for (Pipe pipe : values()) {
                if (pipe.getCharacter() == c) {
                    return pipe;
                }
            }
            throw new IllegalArgumentException("pipe not found for " + c);
        }
    }
}
