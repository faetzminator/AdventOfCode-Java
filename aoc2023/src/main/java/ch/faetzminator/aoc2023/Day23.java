package ch.faetzminator.aoc2023;

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
import ch.faetzminator.aocutil.map.CharEnumAtPosition;
import ch.faetzminator.aocutil.map.ElementAtPositionWithDirection;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.Position;

public class Day23 {

    public static void main(final String[] args) {
        final Day23 puzzle = new Day23();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.findLongestPath();
        PuzzleUtil.end(solution, timer);
    }

    private PMap<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position)).create(input);
    }

    public long findLongestPath() {
        final BlockAtPosition startBlock = map.getElementAt(new Position(1, 0));
        final Position endPos = new Position(map.getXSize() - 2, map.getYSize() - 1);

        final Queue<ElementAtPositionWithDirection<BlockAtPosition>> queue = new LinkedList<>();
        queue.add(new ElementAtPositionWithDirection<>(startBlock, Direction.SOUTH));
        startBlock.setDistance(0);

        while (!queue.isEmpty()) {
            final ElementAtPositionWithDirection<BlockAtPosition> lastMove = queue.poll();
            for (final Direction direction : lastMove.getElementAtPosition().getElement().getExits()) {
                if (direction != lastMove.getDirection().getOpposite()) {
                    final Position nextPos = lastMove.getElementAtPosition().getPosition().move(direction);
                    final BlockAtPosition nextBlock = map.getElementAt(nextPos);
                    if (nextBlock != null && nextBlock.getElement().canEnter(direction.getOpposite())) {
                        nextBlock.setDistance(lastMove.getElementAtPosition().getDistance() + 1);
                        queue.add(new ElementAtPositionWithDirection<>(nextBlock, direction));
                    }
                }
            }
        }

        return map.getElementAt(endPos).getDistance();
    }

    private static class BlockAtPosition extends CharEnumAtPosition<Block> {

        private int distance = -1;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public void setDistance(final int distance) {
            if (distance > this.distance) {
                this.distance = distance;
            }
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public char toPrintableChar() {
            if (distance > 0) {
                return (char) (distance % 10 + '0');
            }
            return super.toPrintableChar();
        }
    }

    private static enum Block implements CharEnum {

        PATH('.', false, Direction.values()),
        ROCK('#', false),
        SLOPE_UP('^', true, Direction.NORTH),
        SLOW_RIGHT('>', true, Direction.EAST),
        SLOPE_DOWN('v', true, Direction.SOUTH),
        SLOPE_LEFT('<', true, Direction.WEST);

        private final char character;
        private final Set<Direction> entries;
        private final Set<Direction> exits;

        private Block(final char character, final boolean inverseEntry, final Direction... exits) {
            this.character = character;
            this.exits = Set.of(exits);
            if (!inverseEntry) {
                entries = this.exits;
            } else {
                final Set<Direction> entries = new HashSet<>(List.of(Direction.values()));
                entries.removeAll(this.exits);
                this.entries = Set.copyOf(entries);
            }
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Block byChar(final char c) {
            return CharEnum.byChar(Block.class, c);
        }

        public boolean canEnter(final Direction fromDirection) {
            return entries.contains(fromDirection);
        }

        public Set<Direction> getExits() {
            return exits;
        }
    }
}
