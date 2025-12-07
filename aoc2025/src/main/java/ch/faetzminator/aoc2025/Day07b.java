package ch.faetzminator.aoc2025;

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
import ch.faetzminator.aocutil.grid.ElementAtPosition;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.GridWithStart;
import ch.faetzminator.aocutil.grid.Position;

public class Day07b {

    public static void main(final String[] args) {
        final Day07b puzzle = new Day07b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private GridWithStart<TachyonAtPosition> tachyonMap;

    public void parseLines(final List<String> input) {
        tachyonMap = new GridFactory<>(TachyonAtPosition.class, TachyonAtPosition::new).create(input,
                element -> element.getElement() == Tachyon.START);
    }

    public long getSolution() {
        final Queue<TachyonAtPosition> queue = new LinkedList<>();
        final Set<TachyonAtPosition> processed = new HashSet<>();

        queue.add(tachyonMap.getStartElement());
        while (!queue.isEmpty()) {
            final TachyonAtPosition current = queue.poll();
            for (final Position position : current.beam()) {
                final TachyonAtPosition next = tachyonMap.getAt(position);
                if (next != null) {
                    next.addPrevious(current);
                    if (processed.add(next)) {
                        queue.add(next);
                    }
                }
            }
        }

        long solution = 0L;
        final int y = tachyonMap.getYSize() - 1;
        for (int x = 0; x < tachyonMap.getXSize(); x++) {
            solution += tachyonMap.getAt(x, y).getBeamCount();
        }
        return solution;
    }

    private static class TachyonAtPosition extends ElementAtPosition<Tachyon> {

        private long beamCount;
        private final Set<TachyonAtPosition> previousElements = new HashSet<>();
        private final List<Position> nextPositions;

        private TachyonAtPosition(final char character, final Position position) {
            super(Tachyon.byChar(character), position);
            beamCount = getElement() == Tachyon.START ? 1L : 0L;
            if (getElement() == Tachyon.SPLITTER) {
                nextPositions = List.of(position.move(Direction.EAST), position.move(Direction.WEST));
            } else {
                nextPositions = List.of(position.move(Direction.SOUTH));
            }
        }

        public void addPrevious(final TachyonAtPosition previous) {
            previousElements.add(previous);
        }

        public List<Position> beam() {
            return nextPositions;
        }

        public long getBeamCount() {
            for (final TachyonAtPosition previous : previousElements) {
                beamCount += previous.getBeamCount();
            }
            previousElements.clear(); // just a bit ugly
            return beamCount;
        }

        @Override
        public char toPrintableChar() {
            if (beamCount > 0 && getElement() == Tachyon.EMPTY_SPACE) {
                return '|';
            }
            return super.toPrintableChar();
        }
    }

    private static enum Tachyon implements CharEnum {

        START('S'),
        SPLITTER('^'),
        EMPTY_SPACE('.');

        private final char character;

        private Tachyon(final char character) {
            this.character = character;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Tachyon byChar(final char c) {
            return CharEnum.byChar(Tachyon.class, c);
        }
    }
}
