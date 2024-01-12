package ch.faetzminator.aoc2023;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.PMapWithStart;
import ch.faetzminator.aocutil.map.Position;

public class Day10 {

    public static void main(final String[] args) {
        final Day10 puzzle = new Day10();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.calculateLoopSteps();
        PuzzleUtil.end(solution, timer);
    }

    private PMapWithStart<Pipe> pipeMap;

    public void parseLines(final List<String> input) {
        pipeMap = new PMapWithStart<>(Pipe.class, input.get(0).length(), input.size(),
                element -> element == Pipe.START);

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                pipeMap.setElementAt(new Position(x, y), Pipe.byChar(line.charAt(x)));
            }
        }
    }

    public int calculateLoopSteps() {

        final Position current = pipeMap.getStartPosition();
        final Pipe startPipe = pipeMap.getElementAt(current);
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
            final Pipe nextPipe = pipeMap.getElementAt(nextPos);
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

    private static enum Pipe implements CharEnum {

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

        @Override
        public char getCharacter() {
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
