package ch.faetzminator.aoc2023;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.CharEnumAtPosition;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.GridWithStart;
import ch.faetzminator.aocutil.grid.Position;

public class Day21b {

    public static void main(final String[] args) {
        final Day21b puzzle = new Day21b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.countReachableGardenPlots(MAX_LENGTH);
        PuzzleUtil.end(solution, timer);
    }

    private GridWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new GridFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position))
                .create(input, element -> element.getElement() == Block.START);
    }

    private static final int MAX_LENGTH = 26501365;

    public long countReachableGardenPlots(final int maxLength) {

        // any EVEN field (e.g. the middle containing start position) needs the odd sum
        // since MAX_LENGTH is odd - vice versa for any ODD field (e.g. 0/1, -1/0 etc)
        final int EVEN = 1;
        final int ODD = 0;

        final int length = map.getXSize();
        if (length != map.getYSize()) {
            throw new IllegalArgumentException("needs same x and y length");
        }
        final long multiplier = maxLength / length;
        if (maxLength % length - length / 2 != 0) {
            throw new IllegalArgumentException("remainer 0 needed");
        }

        final long[] entireField = countReachableGardenPlots(map.getStartPosition(), Integer.MAX_VALUE);

        long result = 0L;
        // add entirely reachable fields
        result += (multiplier - 1L) * (multiplier - 1L) * entireField[EVEN];
        result += multiplier * multiplier * entireField[ODD];

        final int left = 0, middle = length / 2, right = length - 1;
        final int distance = length;
        // add corners
        // note: each corner is EVEN but it's ODD to start point (middle = +65)
        result += countReachableGardenPlots(new Position(middle, left), distance)[ODD];
        result += countReachableGardenPlots(new Position(middle, right), distance)[ODD];
        result += countReachableGardenPlots(new Position(left, middle), distance)[ODD];
        result += countReachableGardenPlots(new Position(right, middle), distance)[ODD];

        final int longDistance = length + length / 2;
        final int shortDistance = length / 2;
        // add other outer fields
        for (final int x : new int[] { left, right }) {
            for (final int y : new int[] { left, right }) {
                final Position position = new Position(x, y);
                result += (multiplier - 1L) * countReachableGardenPlots(position, longDistance)[EVEN];
                result += multiplier * countReachableGardenPlots(position, shortDistance)[ODD];
            }
        }

        return result;
    }

    private long[] countReachableGardenPlots(final Position startPosition, final int limit) {

        final long[] sums = new long[2];
        final Queue<BlockAtPosition> queue = new LinkedList<>();
        final BlockAtPosition startBlock = map.getAt(startPosition);
        startBlock.setDistance(0);
        queue.add(startBlock);
        sums[0]++;

        while (!queue.isEmpty()) {
            final BlockAtPosition current = queue.poll();
            final int nextDistance = current.getDistance() + 1;
            if (nextDistance < limit) {
                for (final Direction direction : Direction.values()) {
                    final Position nextPos = current.getPosition().move(direction);
                    final BlockAtPosition nextBlock = map.getAt(nextPos);
                    if (nextBlock != null && nextBlock.getElement() != Block.ROCK && !nextBlock.hasDistance()) {
                        nextBlock.setDistance(nextDistance);
                        queue.add(nextBlock);
                        sums[nextDistance % 2]++;
                    }
                }
            }
        }

        reset();
        return sums;
    }

    private void reset() {
        map.stream().forEach(BlockAtPosition::reset);
    }

    private static class BlockAtPosition extends CharEnumAtPosition<Block> {

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

        public void reset() {
            distance = UNSET;
        }

        @Override
        public char toPrintableChar() {
            if (getElement() == Block.GARDEN_PLOT && hasDistance()) {
                return distance % 2 == 0 ? '*' : '+';
            }
            return super.toPrintableChar();
        }
    }

    private static enum Block implements CharEnum {

        START('S'),
        GARDEN_PLOT('.'),
        ROCK('#');

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
