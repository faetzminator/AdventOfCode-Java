package ch.faetzminator.aoc2025;

import java.util.List;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.ElementAtPosition;
import ch.faetzminator.aocutil.grid.Grid;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.Position;
import ch.faetzminator.aocutil.grid.PositionUtil;

public class Day04 {

    public static void main(final String[] args) {
        final Day04 puzzle = new Day04();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final int ADJACENT_LIMIT = 3;

    private PaperRollMap paperRollMap;

    public void parseLines(final List<String> input) {
        paperRollMap = new GridFactory<>(PaperRollAtPosition.class, PaperRollMap::new, PaperRollAtPosition::new)
                .create(input);
    }

    public long getSolution() {
        final long solution = paperRollMap.tryToAccess();
        return solution;
    }

    private static class PaperRollMap extends Grid<PaperRollAtPosition> {

        public PaperRollMap(final int xSize, final int ySize) {
            super(PaperRollAtPosition.class, xSize, ySize, new PaperRollAtPosition(PaperRoll.GROUND, null));
        }

        public long tryToAccess() {
            final List<PaperRollAtPosition> elements = stream().filter(this::tryToAccess).collect(Collectors.toList());
            elements.stream().forEach(PaperRollAtPosition::access);
            return elements.size();
        }

        private boolean tryToAccess(final PaperRollAtPosition element) {
            return element.isPaperRoll() && PositionUtil.adjacent8(element.getPosition()).stream()
                    .filter(position -> this.getAt(position).isPaperRoll()).count() <= ADJACENT_LIMIT;
        }
    }

    private static class PaperRollAtPosition extends ElementAtPosition<PaperRoll> {

        private boolean accessed;

        public PaperRollAtPosition(final char character, final Position position) {
            this(PaperRoll.byChar(character), position);
        }

        private PaperRollAtPosition(final PaperRoll element, final Position position) {
            super(element, position);
        }

        public boolean isPaperRoll() {
            return getElement() == PaperRoll.PAPERROLL;
        }

        public PaperRollAtPosition access() {
            if (getElement() != PaperRoll.PAPERROLL) {
                throw new RuntimeException();
            }
            accessed = true;
            return this;
        }

        @Override
        public PaperRoll getElement() {
            return accessed ? PaperRoll.GROUND : super.getElement();
        }

        @Override
        public char toPrintableChar() {
            return accessed ? 'x' : super.toPrintableChar();
        }
    }

    private static enum PaperRoll implements CharEnum {

        PAPERROLL('@'),
        GROUND('.');

        private final char character;

        private PaperRoll(final char character) {
            this.character = character;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static PaperRoll byChar(final char c) {
            return CharEnum.byChar(PaperRoll.class, c);
        }
    }
}
