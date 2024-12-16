package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
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

public class Day15b {

    public static void main(final String[] args) {
        final Day15b puzzle = new Day15b();
        final List<String> mapLines;
        final List<String> moveLines;
        try (Scanner scanner = new Scanner(System.in)) {
            mapLines = ScannerUtil.readNonBlankLines(scanner);
            moveLines = ScannerUtil.readNonBlankLines(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        puzzle.parseMap(mapLines);
        puzzle.parseMoves(moveLines);
        puzzle.move();
        final long solution = puzzle.getCoordinatesSum();
        PuzzleUtil.end(solution, timer);
    }

    private GridWithStart<Block> map;
    private final List<Move> moves = new ArrayList<>();

    public void parseMap(final List<String> lines) {
        final List<String> expandedLines = new ArrayList<>();
        for (final String line : lines) {
            expandedLines.add(line.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@."));
        }
        map = new GridFactory<>(Block.class, (character, position) -> Block.byChar(character)).create(expandedLines,
                block -> block == Block.ROBOT);
    }

    public void parseMoves(final List<String> lines) {
        for (final String line : lines) {
            for (final char character : line.toCharArray()) {
                moves.add(Move.byChar(character));
            }
        }
    }

    public void move() {
        Position position = map.getStartPosition();
        for (final Move move : moves) {
            final Direction direction = move.getDirection();
            if (tryMove(position, direction)) {
                position = position.move(direction);
            }
        }
    }

    private boolean tryMove(final Position startPosition, final Direction direction) {
        final MoveData data = getMovableBlocks(startPosition, direction);
        if (data.canMove()) {
            for (final Position position : data.getPositionsToUnset()) {
                map.setAt(position, Block.PATH);
            }
            for (final BlockAtPosition blockAtPosition : data.getBlocksToMove()) {
                map.setAt(blockAtPosition.getPosition().move(direction), blockAtPosition.getElement());
            }
        }
        return data.canMove();
    }

    private MoveData getMovableBlocks(final Position startPosition, final Direction direction) {
        final List<BlockAtPosition> blocksToMove = new ArrayList<>();
        final List<Position> positionsToUnset = new ArrayList<>();
        positionsToUnset.add(startPosition);

        final Direction oppositeDirection = direction.getOpposite();

        Collection<BlockAtPosition> lastBlocks = Arrays
                .asList(new BlockAtPosition(map.getAt(startPosition), startPosition));

        final Set<Block> seenBlocks = new HashSet<>();
        do {
            seenBlocks.clear();

            final Collection<BlockAtPosition> blocks = new LinkedHashSet<>();
            final Collection<BlockAtPosition> newBlocks = new LinkedHashSet<>();
            for (final BlockAtPosition lastBlock : lastBlocks) {
                blocksToMove.add(lastBlock);
                final Position position = lastBlock.getPosition().move(direction);
                final Block block = map.getAt(position);
                if (block == Block.ROCK) {
                    return new MoveData();
                }
                if (block == Block.PATH) {
                    continue;
                }
                blocks.add(new BlockAtPosition(block, position));
                seenBlocks.add(block);

                final Direction neighbourDirection = block.getNeighbourDirection();
                if (neighbourDirection != null && neighbourDirection != direction
                        && neighbourDirection != oppositeDirection) {

                    final Position neighbourPosition = position.move(neighbourDirection);
                    newBlocks.add(new BlockAtPosition(map.getAt(neighbourPosition), neighbourPosition));
                }
            }
            newBlocks.removeAll(blocks);
            for (final BlockAtPosition newBlock : newBlocks) {
                positionsToUnset.add(newBlock.getPosition());
            }
            blocks.addAll(newBlocks);

            lastBlocks = blocks;
            seenBlocks.remove(Block.PATH);
        } while (!seenBlocks.isEmpty());

        return new MoveData(blocksToMove, positionsToUnset);
    }

    public long getCoordinatesSum() {
        long sum = 0L;
        for (int y = 0; y < map.getYSize(); y++) {
            for (int x = 0; x < map.getXSize(); x++) {
                if (map.getAt(x, y) == Block.BOX_LEFT) {
                    sum += 100 * y + x;
                }
            }
        }
        return sum;
    }

    private static class MoveData {

        private final List<BlockAtPosition> blocksToMove;
        private final List<Position> positionsToUnset;

        public MoveData() {
            this(null, null);
        }

        public MoveData(final List<BlockAtPosition> blocksToMove, final List<Position> positionsToUnset) {
            this.blocksToMove = blocksToMove;
            this.positionsToUnset = positionsToUnset;
        }

        public List<BlockAtPosition> getBlocksToMove() {
            return blocksToMove;
        }

        public List<Position> getPositionsToUnset() {
            return positionsToUnset;
        }

        public boolean canMove() {
            return blocksToMove != null;
        }
    }

    private static class BlockAtPosition extends CharEnumAtPosition<Block> {

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }
    }

    private static enum Block implements CharEnum {

        PATH('.', null),
        ROCK('#', null),
        BOX_LEFT('[', Direction.EAST),
        BOX_RIGHT(']', Direction.WEST),
        ROBOT('@', null);

        private final char character;
        private final Direction neighbourDirection;

        private Block(final char character, final Direction neighbourDirection) {
            this.character = character;
            this.neighbourDirection = neighbourDirection;
        }

        public Direction getNeighbourDirection() {
            return neighbourDirection;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Block byChar(final char c) {
            return CharEnum.byChar(Block.class, c);
        }
    }

    private static enum Move implements CharEnum {

        SLOPE_UP('^', Direction.NORTH),
        SLOW_RIGHT('>', Direction.EAST),
        SLOPE_DOWN('v', Direction.SOUTH),
        SLOPE_LEFT('<', Direction.WEST);

        private final char character;
        private final Direction direction;

        private Move(final char character, final Direction direction) {
            this.character = character;
            this.direction = direction;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Move byChar(final char c) {
            return CharEnum.byChar(Move.class, c);
        }

        public Direction getDirection() {
            return direction;
        }
    }
}
