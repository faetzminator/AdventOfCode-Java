package ch.faetzminator.aoc2024;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.PMapWithStart;
import ch.faetzminator.aocutil.map.Position;

public class Day06b {

    public static void main(final String[] args) {
        final Day06b puzzle = new Day06b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.walkAllPossibilities();
        PuzzleUtil.end(solution, timer);
    }

    private PMapWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position))
                .create(input, element -> element.getElement() == Block.START);
    }

    public long walkAllPossibilities() {
        // only loop trough elements which are walked trough - and reset
        walk();
        final Set<BlockAtPosition> elements = map.stream().filter(BlockAtPosition::isVisited)
                .collect(Collectors.toSet());
        map.stream().forEach(BlockAtPosition::reset);
        // rather don't override the start element
        elements.remove(map.getStartElement());

        return elements.stream().filter(element -> {
            map.setElementAt(element.getPosition(), new BlockAtPosition(Block.OBSTRUCTION, element.getPosition()));
            final boolean keep = walk();
            // reset original state
            map.setElementAt(element.getPosition(), element);
            map.stream().forEach(BlockAtPosition::reset);
            return keep;
        }).count();
    }

    private boolean walk() {
        Direction direction = Direction.NORTH;
        BlockAtPosition block = map.getStartElement();
        block.setVisited(direction);

        while (true) {
            BlockAtPosition nextBlock;
            Position nextPos;
            do {
                nextPos = block.getPosition().move(direction);
                nextBlock = map.getElementAt(nextPos);
                if (nextBlock == null) {
                    // we're outside of the map - no loop
                    return false;
                }
                if (nextBlock.isVisited(direction)) {
                    // we're stuck in a loop
                    return true;
                }
                if (nextBlock.getElement() == Block.OBSTRUCTION) {
                    direction = direction.getClockwise();
                }
            } while (nextBlock.getElement() == Block.OBSTRUCTION);
            if (nextBlock.isVisited(direction)) {
                return true;
            }
            nextBlock.setVisited(direction);
            block = nextBlock;
        }
    }

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private final Set<Direction> visitedDirections = new HashSet<>();

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public void setVisited(final Direction direction) {
            visitedDirections.add(direction);
        }

        public boolean isVisited() {
            return !visitedDirections.isEmpty();
        }

        public boolean isVisited(final Direction direction) {
            return visitedDirections.contains(direction);
        }

        public void reset() {
            visitedDirections.clear();
        }

        @Override
        public char toPrintableChar() {
            if (!visitedDirections.isEmpty()) {
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
        public char getCharacter() {
            return character;
        }

        public static Block byChar(final char c) {
            return CharEnum.byChar(Block.class, c);
        }
    }
}
