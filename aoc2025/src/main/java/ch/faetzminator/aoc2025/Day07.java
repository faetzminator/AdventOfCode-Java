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

public class Day07 {

    public static void main(final String[] args) {
        final Day07 puzzle = new Day07();
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
            for (final Position position : queue.poll().beam()) {
                final TachyonAtPosition next = tachyonMap.getAt(position);
                if (next != null) {
                    if (processed.add(next)) {
                        queue.add(next);
                    }
                }
            }
        }

        return tachyonMap.stream().filter(element -> element.isBeamed() && element.getElement() == Tachyon.SPLITTER)
                .count();
    }

    private static class TachyonAtPosition extends ElementAtPosition<Tachyon> {

        private boolean beamed;
        private final List<Position> nextPositions;

        private TachyonAtPosition(final char character, final Position position) {
            super(Tachyon.byChar(character), position);
            if (getElement() == Tachyon.SPLITTER) {
                nextPositions = List.of(getPosition().move(Direction.EAST), getPosition().move(Direction.WEST));
            } else {
                nextPositions = List.of(getPosition().move(Direction.SOUTH));
            }
        }

        public List<Position> beam() {
            beamed = true;
            return nextPositions;
        }

        public boolean isBeamed() {
            return beamed;
        }

        @Override
        public char toPrintableChar() {
            if (beamed && getElement() == Tachyon.EMPTY_SPACE) {
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
