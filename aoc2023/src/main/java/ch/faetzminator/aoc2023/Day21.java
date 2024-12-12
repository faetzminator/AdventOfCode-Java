package ch.faetzminator.aoc2023;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.PMapWithStart;
import ch.faetzminator.aocutil.map.Position;

public class Day21 {

    public static void main(final String[] args) {
        final Day21 puzzle = new Day21();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.countReachableGardenPlots();
        PuzzleUtil.end(solution, timer);
    }

    private PMapWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position))
                .create(input, element -> element.getElement() == Block.START);
    }

    private static final int MAX_LENGTH = 64;

    public long countReachableGardenPlots() {
        long sum = 1L;
        final Queue<BlockAtPosition> queue = new LinkedList<>();
        final BlockAtPosition startBlock = map.getElementAt(map.getStartPosition());
        startBlock.setDistance(0);
        queue.add(startBlock);

        while (!queue.isEmpty()) {
            final BlockAtPosition current = queue.poll();
            final int nextDistance = current.getDistance() + 1;
            if (nextDistance <= MAX_LENGTH) {
                for (final Direction direction : Direction.values()) {
                    final Position nextPos = current.getPosition().move(direction);
                    final BlockAtPosition nextBlock = map.getElementAt(nextPos);
                    if (nextBlock != null && nextBlock.getElement() != Block.ROCK && !nextBlock.hasDistance()) {
                        nextBlock.setDistance(nextDistance);
                        queue.add(nextBlock);
                        if (nextDistance % 2 == 0) {
                            sum++;
                        }
                    }
                }
            }
        }

        return sum;
    }

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private static final int UNSET = -1;

        private int distance = UNSET;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public void setDistance(final int distance) {
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        public boolean hasDistance() {
            return distance != UNSET;
        }

        @Override
        public char toPrintableChar() {
            if (getElement() == Block.GARDEN_PLOT && distance % 2 == 0) {
                return (char) (distance % 10 + '0');
            }
            return super.toPrintableChar();
        }
    }

    private static enum Block implements CharEnum {

        START('S'), GARDEN_PLOT('.'), ROCK('#');

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
