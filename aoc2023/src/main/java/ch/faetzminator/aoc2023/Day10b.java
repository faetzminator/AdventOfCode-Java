package ch.faetzminator.aoc2023;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.CharEnumAtPosition;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.GridWithStart;
import ch.faetzminator.aocutil.grid.Position;

public class Day10b {

    public static void main(final String[] args) {
        final Day10b puzzle = new Day10b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.calculateEnclosedTiles();
        PuzzleUtil.end(solution, timer);
    }

    private GridWithStart<PipeAtPosition> pipeMap;

    public void parseLines(final List<String> input) {
        pipeMap = new GridFactory<>(PipeAtPosition.class,
                (character, position) -> new PipeAtPosition(Pipe.byChar(character), position))
                .create(input, element -> element.getElement() == Pipe.START);
    }

    public int calculateEnclosedTiles() {

        final Position current = pipeMap.getStartPosition();
        final PipeAtPosition startPipe = pipeMap.getAt(current);
        for (final Direction direction : startPipe.getElement().getDirections()) {
            final int steps = calculateLoopSteps(startPipe, direction);
            // we should get twice -1 and twice the same number
            if (steps >= 0) {
                markTiles(startPipe, direction, null);
                markOutsideLoop();
                // we run trough it once more, to find the start outside direction
                final Direction outsideDirection = markTiles(startPipe, direction, null);
                // and once more to fill it entirely
                markTiles(startPipe, direction, outsideDirection);

                return getEnclosedTilesCount();
            }
        }
        return -1;
    }

    private int getEnclosedTilesCount() {
        int in = 0, unknown = 0;
        for (final PipeAtPosition pipe : pipeMap) {
            if (!pipe.isPartOfLoop()) {
                if (Boolean.TRUE.equals(pipe.getInLoop())) {
                    in++;
                } else if (pipe.getInLoop() == null) {
                    unknown++; // something wrong with the logic, unknown field
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
            final Position nextPos = currentPipe.getPosition().move(direction);
            if (!pipeMap.isInBounds(nextPos)) {
                return -1;
            }
            final PipeAtPosition nextPipe = pipeMap.getAt(nextPos);
            if (nextPipe.getElement() == Pipe.START) {
                return steps + 1;
            }
            final Direction inverseDirection = direction.getOpposite();
            final Set<Direction> nextDirections = nextPipe.getElement().getDirections();
            if (!nextDirections.contains(inverseDirection)) {
                return -1;
            }
            for (final Direction nextDirection : nextDirections) {
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
            final Position nextPos = currentPipe.getPosition().move(direction);
            if (pipeMap.isInBounds(nextPos)) {
                final PipeAtPosition nextPipe = pipeMap.getAt(nextPos);
                if (outsideDirection != null) {
                    markLoopSide(currentPipe, outsideDirection);
                    if (nextPipe.getElement() != currentPipe.getElement()) {
                        outsideDirection = findNewDirection(nextPipe, outsideDirection);
                    }
                }
                if (nextPipe.getElement() == Pipe.START) {
                    return outsideDirection;
                }
                final Direction inverseDirection = direction.getOpposite();
                final Set<Direction> nextDirections = nextPipe.getElement().getDirections();
                for (final Direction nextDirection : nextDirections) {
                    if (nextDirection != inverseDirection) {
                        // we know there can only be one other direction (apart from START)
                        currentPipe = nextPipe;
                        direction = nextDirection;
                    }
                }
            }
        }
    }

    private Direction findNewDirection(final PipeAtPosition nextPipe, final Direction outsideDirection) {
        final Direction newOutsideDirection = Direction.values()[(outsideDirection.ordinal() + 1)
                % Direction.values().length];

        switch (nextPipe.getElement()) {
        case BEND1:
        case BEND2:
        case BEND3:
        case BEND4:
            if (nextPipe.getElement().getDirections().contains(outsideDirection)) {
                for (final Direction direction : nextPipe.getElement().getDirections()) {
                    if (direction != outsideDirection) {
                        return direction;
                    }
                }
                throw new IllegalArgumentException();
            } else {
                if (nextPipe.getElement().getDirections().contains(newOutsideDirection)) {
                    return newOutsideDirection.getOpposite();
                }
                return newOutsideDirection;
            }
        default:
            // this can be improved for sure for non bend pipes
        }

        if (!nextPipe.getElement().getDirections().contains(newOutsideDirection)) {
            return newOutsideDirection;
        }
        return outsideDirection;
    }

    private Direction findOutsideDirection(final PipeAtPosition pipe) {
        if (pipe.getElement() != Pipe.VERTICAL && pipe.getElement() != Pipe.HORIZONTAL) {
            return null;
        }
        for (final Direction direction : Direction.values()) {
            final PipeAtPosition neighbour = pipeMap.getAt(pipe.getPosition().move(direction));
            if (neighbour != null && Boolean.FALSE.equals(neighbour.getInLoop())) {
                return direction;
            }
        }
        return null;
    }

    private void markOutsideLoop() {
        for (int x = 0; x < pipeMap.getXSize(); x++) {
            markLoopSide(pipeMap.getAt(new Position(x, 0)), false);
            markLoopSide(pipeMap.getAt(new Position(x, pipeMap.getYSize() - 1)), false);
        }
        for (int y = 0; y < pipeMap.getYSize(); y++) {
            markLoopSide(pipeMap.getAt(new Position(0, y)), false);
            markLoopSide(pipeMap.getAt(new Position(pipeMap.getXSize() - 1, y)), false);
        }
    }

    private void markLoopSide(final PipeAtPosition pipe, final Direction outsideDirection) {
        final Position position = pipe.getPosition();
        switch (pipe.getElement()) {
        case BEND1:
        case BEND2:
        case BEND3:
        case BEND4:
            final boolean insideOut = pipe.getElement().getDirections().contains(outsideDirection);
            Position newPosition = position;
            for (final Direction direction : pipe.getElement().getDirections()) {
                newPosition = newPosition.move(direction);
            }
            markLoopSide(pipeMap.getAt(newPosition), !insideOut);

            newPosition = position;
            final Set<Direction> opposite = new LinkedHashSet<>(Arrays.asList(Direction.values()));
            opposite.removeAll(pipe.getElement().getDirections());
            for (final Direction direction : opposite) {
                markLoopSide(pipeMap.getAt(position.move(direction)), insideOut);
                newPosition = newPosition.move(direction);
            }
            markLoopSide(pipeMap.getAt(newPosition), insideOut);
            break;
        default:
            // this can be improved for sure for non bend pipes
        }
    }

    private void markLoopSide(PipeAtPosition pipe, final boolean inLoop) {
        final Queue<PipeAtPosition> queue = new LinkedList<>();
        queue.add(pipe);
        while (!queue.isEmpty()) {
            pipe = queue.poll();
            if (pipe != null && !pipe.isPartOfLoop() && pipe.getInLoop() == null) {
                pipe.setInLoop(inLoop);
                queue.add(pipeMap.getAt(pipe.getPosition().move(Direction.NORTH)));
                queue.add(pipeMap.getAt(pipe.getPosition().move(Direction.EAST)));
                queue.add(pipeMap.getAt(pipe.getPosition().move(Direction.SOUTH)));
                queue.add(pipeMap.getAt(pipe.getPosition().move(Direction.WEST)));
            }
        }
    }

    private static class PipeAtPosition extends CharEnumAtPosition<Pipe> {

        private boolean partOfLoop;
        private Boolean inLoop;

        public PipeAtPosition(final Pipe pipe, final Position position) {
            super(pipe, position);
        }

        public void setPartOfLoop() {
            partOfLoop = true;
        }

        public void setInLoop(final boolean inLoop) {
            this.inLoop = inLoop;
        }

        public boolean isPartOfLoop() {
            return partOfLoop;
        }

        public Boolean getInLoop() {
            return inLoop;
        }

        @Override
        public char toPrintableChar() {
            if (partOfLoop) {
                return super.toPrintableChar();
            }
            if (Boolean.TRUE.equals(inLoop)) {
                return 'I';
            }
            if (Boolean.FALSE.equals(inLoop)) {
                return 'O';
            }
            return Pipe.GROUND.toPrintableChar();
        }
    }

    private static enum Pipe implements CharEnum {

        VERTICAL('|', Direction.NORTH, Direction.SOUTH),
        HORIZONTAL('-', Direction.EAST, Direction.WEST),
        BEND1('L', Direction.NORTH, Direction.EAST),
        BEND2('J', Direction.NORTH, Direction.WEST),
        BEND3('7', Direction.SOUTH, Direction.WEST),
        BEND4('F', Direction.SOUTH, Direction.EAST),
        GROUND('.'),
        START('S', Direction.values());

        private final char character;
        private final Set<Direction> directions;

        private Pipe(final char character, final Direction... directions) {
            this.character = character;
            this.directions = new LinkedHashSet<>(Arrays.asList(directions));
        }

        @Override
        public char charValue() {
            return character;
        }

        public Set<Direction> getDirections() {
            return directions;
        }

        public static Pipe byChar(final char c) {
            return CharEnum.byChar(Pipe.class, c);
        }
    }
}
