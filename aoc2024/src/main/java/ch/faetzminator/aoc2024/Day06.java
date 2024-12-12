package ch.faetzminator.aoc2024;

import java.util.List;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.PMapWithStart;
import ch.faetzminator.aocutil.map.Position;

public class Day06 {

    public static void main(final String[] args) {
        final Day06 puzzle = new Day06();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.walk();
        PuzzleUtil.end(solution, timer);
    }

    private PMapWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position))
                .create(input, element -> element.getElement() == Block.START);
    }

    public long walk() {
        Direction direction = Direction.NORTH;
        BlockAtPosition block = map.getStartElement();
        block.setVisited();

        while (true) {
            BlockAtPosition nextBlock;
            Position nextPos;
            do {
                nextPos = block.getPosition().move(direction);
                nextBlock = map.getElementAt(nextPos);
                if (nextBlock == null) {
                    // we're outside of the map, so let's count
                    return map.stream().filter(BlockAtPosition::isVisited).count();
                }
                if (nextBlock.getElement() == Block.OBSTRUCTION) {
                    direction = direction.getClockwise();
                }
            } while (nextBlock.getElement() == Block.OBSTRUCTION);
            nextBlock.setVisited();
            block = nextBlock;
        }
    }

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private boolean visited;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public void setVisited() {
            visited = true;
        }

        public boolean isVisited() {
            return visited;
        }

        @Override
        public char toPrintableChar() {
            if (visited) {
                return 'X';
            }
            return super.toPrintableChar();
        }
    }

    private static enum Block implements CharEnum {

        PATH('.'), OBSTRUCTION('#'), START('^');

        private final char character;

        private Block(final char character) {
            this.character = character;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Block byChar(final char c) {
            return CharEnum.byChar(Block.class, c);
        }
    }
}
