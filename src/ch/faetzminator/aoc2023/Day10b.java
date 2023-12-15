package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day10b {

    public static void main(String[] args) {
        Day10b puzzle = new Day10b();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        System.out.println("Solution: " + puzzle.calculateEnclosedTiles());
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

    public int calculateEnclosedTiles() {

        Position current = pipeMap.getStartPosition();
        PipeAtPosition startPipe = pipeMap.getPipeAt(current);
        for (Direction direction : startPipe.getPipe().getDirections()) {
            int steps = calculateLoopSteps(startPipe, direction);
            // we should get twice -1 and twice the same number
            if (steps >= 0) {
                markTiles(startPipe, direction, null);
                markOutsideLoop();
                // we run trough it once more, to find the start outside direction
                Direction outsideDirection = markTiles(startPipe, direction, null);
                // and once more to fill it entirely
                markTiles(startPipe, direction, outsideDirection);

                return getEnclosedTilesCount();
            }
        }
        return -1;
    }

    private int getEnclosedTilesCount() {
        int in = 0, unknown = 0;

        for (int y = 0; y < pipeMap.getYSize(); y++) {
            for (int x = 0; x < pipeMap.getXSize(); x++) {
                PipeAtPosition pipe = pipeMap.getPipeAt(new Position(x, y));
                if (!pipe.isPartOfLoop()) {
                    if (Boolean.TRUE.equals(pipe.getInLoop())) {
                        in++;
                    } else if (pipe.getInLoop() == null) {
                        unknown++; // something wrong with the logic, unknown field
                    }
                }
            }
        }
        if (unknown > 0) {
            throw new IllegalArgumentException();
        }
        return in;
    }

    private int calculateLoopSteps(PipeAtPosition currentPipe, Direction direction) {
        int steps = 0;
        while (true) { // don't we love infinite loops?
            Position nextPos = currentPipe.getPosition().move(direction);
            if (!pipeMap.isInBounds(nextPos)) {
                return -1;
            }
            PipeAtPosition nextPipe = pipeMap.getPipeAt(nextPos);
            if (nextPipe.getPipe() == Pipe.START) {
                return steps + 1;
            }
            Direction inverseDirection = direction.getOpposite();
            Set<Direction> nextDirections = nextPipe.getPipe().getDirections();
            if (!nextDirections.contains(inverseDirection)) {
                return -1;
            }
            for (Direction nextDirection : nextDirections) {
                if (nextDirection != inverseDirection) {
                    // we know there can only be one other direction (apart from START)
                    steps++;
                    currentPipe = nextPipe;
                    direction = nextDirection;
                }
            }
        }
    }

    private Direction markTiles(PipeAtPosition currentPipe, Direction direction, Direction outsideDirection) {
        while (true) { // don't we love infinite loops?
            currentPipe.setPartOfLoop();
            if (outsideDirection == null) {
                outsideDirection = findOutsideDirection(currentPipe);
            }
            Position nextPos = currentPipe.getPosition().move(direction);
            if (pipeMap.isInBounds(nextPos)) {
                PipeAtPosition nextPipe = pipeMap.getPipeAt(nextPos);
                if (outsideDirection != null) {
                    markLoopSide(currentPipe, outsideDirection);
                    if (nextPipe.getPipe() != currentPipe.getPipe()) {
                        outsideDirection = findNewDirection(nextPipe, outsideDirection);
                    }
                }
                if (nextPipe.getPipe() == Pipe.START) {
                    return outsideDirection;
                }
                Direction inverseDirection = direction.getOpposite();
                Set<Direction> nextDirections = nextPipe.getPipe().getDirections();
                for (Direction nextDirection : nextDirections) {
                    if (nextDirection != inverseDirection) {
                        // we know there can only be one other direction (apart from START)
                        currentPipe = nextPipe;
                        direction = nextDirection;
                    }
                }
            }
        }
    }

    private Direction findNewDirection(PipeAtPosition nextPipe, Direction outsideDirection) {
        Direction newOutsideDirection = Direction.values()[(outsideDirection.ordinal() + 1)
                % Direction.values().length];

        switch (nextPipe.getPipe()) {
        case BEND1:
        case BEND2:
        case BEND3:
        case BEND4:
            if (nextPipe.getPipe().getDirections().contains(outsideDirection)) {
                for (Direction direction : nextPipe.getPipe().getDirections()) {
                    if (direction != outsideDirection) {
                        return direction;
                    }
                }
                throw new IllegalArgumentException();
            } else {
                if (nextPipe.getPipe().getDirections().contains(newOutsideDirection)) {
                    return newOutsideDirection.getOpposite();
                }
                return newOutsideDirection;
            }
        default:
            // this can be improved for sure for non bend pipes
        }

        if (!nextPipe.getPipe().getDirections().contains(newOutsideDirection)) {
            return newOutsideDirection;
        }
        return outsideDirection;
    }

    private Direction findOutsideDirection(PipeAtPosition pipe) {
        if (pipe.getPipe() != Pipe.VERTICAL && pipe.getPipe() != Pipe.HORIZONTAL) {
            return null;
        }
        for (Direction direction : Direction.values()) {
            PipeAtPosition neighbour = pipeMap.getPipeAt(pipe.getPosition().move(direction));
            if (neighbour != null && Boolean.FALSE.equals(neighbour.getInLoop())) {
                return direction;
            }
        }
        return null;
    }

    private void markOutsideLoop() {
        for (int x = 0; x < pipeMap.getXSize(); x++) {
            markLoopSide(pipeMap.getPipeAt(new Position(x, 0)), false);
            markLoopSide(pipeMap.getPipeAt(new Position(x, pipeMap.getYSize() - 1)), false);
        }
        for (int y = 0; y < pipeMap.getYSize(); y++) {
            markLoopSide(pipeMap.getPipeAt(new Position(0, y)), false);
            markLoopSide(pipeMap.getPipeAt(new Position(pipeMap.getXSize() - 1, y)), false);
        }
    }

    private void markLoopSide(PipeAtPosition pipe, Direction outsideDirection) {
        Position position = pipe.getPosition();
        switch (pipe.getPipe()) {
        case BEND1:
        case BEND2:
        case BEND3:
        case BEND4:
            boolean insideOut = pipe.getPipe().getDirections().contains(outsideDirection);
            Position newPosition = position;
            for (Direction direction : pipe.getPipe().getDirections()) {
                newPosition = newPosition.move(direction);
            }
            markLoopSide(pipeMap.getPipeAt(newPosition), !insideOut);

            newPosition = position;
            Set<Direction> opposite = new LinkedHashSet<>(Arrays.asList(Direction.values()));
            opposite.removeAll(pipe.getPipe().getDirections());
            for (Direction direction : opposite) {
                markLoopSide(pipeMap.getPipeAt(position.move(direction)), insideOut);
                newPosition = newPosition.move(direction);
            }
            markLoopSide(pipeMap.getPipeAt(newPosition), insideOut);
            break;
        default:
            // this can be improved for sure for non bend pipes
        }
    }

    private void markLoopSide(PipeAtPosition pipe, boolean inLoop) {
        Queue<PipeAtPosition> queue = new LinkedList<>();
        queue.add(pipe);
        while (!queue.isEmpty()) {
            pipe = queue.poll();
            if (pipe != null && !pipe.isPartOfLoop() && pipe.getInLoop() == null) {
                pipe.setInLoop(inLoop);
                queue.add(pipeMap.getPipeAt(pipe.getPosition().move(Direction.NORTH)));
                queue.add(pipeMap.getPipeAt(pipe.getPosition().move(Direction.EAST)));
                queue.add(pipeMap.getPipeAt(pipe.getPosition().move(Direction.SOUTH)));
                queue.add(pipeMap.getPipeAt(pipe.getPosition().move(Direction.WEST)));
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

    public class PipeAtPosition {
        private final Pipe pipe;
        private final Position position;

        private boolean partOfLoop;
        private Boolean inLoop;

        public PipeAtPosition(Pipe pipe, Position position) {
            this.pipe = pipe;
            this.position = position;
        }

        public Pipe getPipe() {
            return pipe;
        }

        public Position getPosition() {
            return position;
        }

        public void setPartOfLoop() {
            this.partOfLoop = true;
        }

        public void setInLoop(boolean inLoop) {
            this.inLoop = inLoop;
        }

        public boolean isPartOfLoop() {
            return partOfLoop;
        }

        public Boolean getInLoop() {
            return inLoop;
        }

        public char getCharacter() {
            if (partOfLoop) {
                return pipe.getCharacter();
            }
            if (Boolean.TRUE.equals(inLoop)) {
                return 'I';
            }
            if (Boolean.FALSE.equals(inLoop)) {
                return 'O';
            }
            return Pipe.GROUND.getCharacter();
        }
    }

    public class PipeMap {

        private final PipeAtPosition[][] map;
        private Position startPosition;

        public PipeMap(int xSize, int ySize) {
            map = new PipeAtPosition[ySize][xSize];
        }

        public void setPipeAt(Position position, Pipe pipe) {
            map[position.getY()][position.getX()] = new PipeAtPosition(pipe, position);
            if (pipe == Pipe.START) {
                if (startPosition != null) {
                    throw new IllegalArgumentException("duplicate start");
                }
                this.startPosition = position;
            }
        }

        public PipeAtPosition getPipeAt(Position position) {
            return isInBounds(position) ? map[position.getY()][position.getX()] : null;
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
