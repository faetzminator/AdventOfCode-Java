package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.CharEnumAtPosition;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.PMapWithStart;
import ch.faetzminator.aocutil.map.Position;

public class Day15 {

    public static void main(final String[] args) {
        final Day15 puzzle = new Day15();
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

    private PMapWithStart<Block> map;
    private final List<Move> moves = new ArrayList<>();

    public void parseMap(final List<String> lines) {
        map = new PMapFactory<>(Block.class, (character, position) -> Block.byChar(character)).create(lines,
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
            map.setElementAt(startPosition, Block.PATH);
            for (final BlockAtPosition blockAtPosition : data.getBlocksToMove()) {
                map.setElementAt(blockAtPosition.getPosition().move(direction), blockAtPosition.getElement());
            }
        }
        return data.canMove();
    }

    private MoveData getMovableBlocks(final Position startPosition, final Direction direction) {
        final List<BlockAtPosition> blocksToMove = new ArrayList<>();

        Position position = startPosition;
        Block block = map.getElementAt(startPosition);
        do {
            blocksToMove.add(new BlockAtPosition(block, position));
            position = position.move(direction);
            block = map.getElementAt(position);
            if (block == Block.ROCK) {
                return new MoveData();
            }
        } while (block != Block.PATH);

        return new MoveData(blocksToMove);
    }

    public long getCoordinatesSum() {
        long sum = 0L;
        for (int y = 0; y < map.getYSize(); y++) {
            for (int x = 0; x < map.getXSize(); x++) {
                if (map.getElementAt(x, y) == Block.BOX) {
                    sum += 100 * y + x;
                }
            }
        }
        return sum;
    }

    private static class MoveData {

        private final List<BlockAtPosition> blocksToMove;

        public MoveData() {
            this(null);
        }

        public MoveData(final List<BlockAtPosition> blocksToMove) {
            this.blocksToMove = blocksToMove;
        }

        public List<BlockAtPosition> getBlocksToMove() {
            return blocksToMove;
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

        PATH('.'),
        ROCK('#'),
        BOX('O'),
        ROBOT('@');

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
