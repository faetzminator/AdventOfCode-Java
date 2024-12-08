package ch.faetzminator.aoc2024;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.Position;

public class Day08 {

    public static void main(final String[] args) {
        final Day08 puzzle = new Day08();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getAntinodeSum();
        PuzzleUtil.end(solution, timer);
    }

    private PMap<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(new Block(character), position)).create(input);
    }

    public long getAntinodeSum() {
        final Map<Character, List<BlockAtPosition>> antennas = map.stream().filter(BlockAtPosition::isAntenna)
                .collect(Collectors.groupingBy(BlockAtPosition::toPrintableChar));

        for (final List<BlockAtPosition> blocks : antennas.values()) {
            final Position[] positions = blocks.stream().map(BlockAtPosition::getPosition).toArray(Position[]::new);
            for (int i = 0; i < positions.length; i++) {
                for (int j = i + 1; j < positions.length; j++) {
                    Position one = positions[i], other = positions[j];
                    final int xDist = other.getX() - one.getX();
                    final int yDist = other.getY() - one.getY();
                    one = new Position(one.getX() - xDist, one.getY() - yDist);
                    if (map.isInBounds(one)) {
                        map.getElementAt(one).setAntinode();
                    }
                    other = new Position(other.getX() + xDist, other.getY() + yDist);
                    if (map.isInBounds(other)) {
                        map.getElementAt(other).setAntinode();
                    }
                }
            }
        }
        return map.stream().filter(BlockAtPosition::isAntinode).count();
    }

    private final static char PATH = '.';
    private final static char ANTINODE = '#';

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private boolean antinode;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public boolean isAntenna() {
            return getElement().isAntenna();
        }

        public boolean isAntinode() {
            return antinode;
        }

        public void setAntinode() {
            antinode = true;
        }

        @Override
        public char toPrintableChar() {
            if (isAntinode() && !isAntenna()) {
                return ANTINODE;
            }
            return super.toPrintableChar();
        }
    }

    private static class Block implements CharPrintable {

        private final char character;

        public Block(final char character) {
            this.character = character;
        }

        public boolean isAntenna() {
            return character != PATH && character != ANTINODE;
        }

        @Override
        public char toPrintableChar() {
            return character;
        }
    }
}
