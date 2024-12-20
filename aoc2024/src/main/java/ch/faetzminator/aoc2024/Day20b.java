package ch.faetzminator.aoc2024;

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

public class Day20b {

    public static void main(final String[] args) {
        final Day20b puzzle = new Day20b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getNumberOfCheats();
        PuzzleUtil.end(solution, timer);
    }

    private GridWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> lines) {
        map = new GridFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position))
                .create(lines, block -> block.getElement() == Block.START, block -> block.getElement() == Block.END);
    }

    public long getNumberOfCheats() {
        return getNumberOfCheats(100L);
    }

    protected long getNumberOfCheats(final long limit) {
        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(map.getStartElement());
        map.getStartElement().setDistance(0L);

        while (!queue.isEmpty()) {
            final BlockAtPosition last = queue.poll();
            final long distance = last.getDistance() + 1L;
            for (final Direction d : Direction.values()) {
                final BlockAtPosition next = last.move(map, d);
                if (next.getElement() != Block.WALL) {
                    if (next.setDistance(distance)) {
                        queue.add(next);
                    }
                }
            }
        }

        long sum = 0L;
        for (final BlockAtPosition block : map) {
            sum += countCheats(block, limit);
        }
        return sum;
    }

    private long countCheats(final BlockAtPosition start, final long limit) {
        if (start.getDistance() == Long.MAX_VALUE) {
            return 0L;
        }
        long sum = 0L;
        for (int yDiff = -20; yDiff <= 20; yDiff++) {
            final int yAbs = Math.abs(yDiff);
            for (int xDiff = -(20 - yAbs); xDiff <= (20 - yAbs); xDiff++) {
                final int x = start.getX() + xDiff;
                final int y = start.getY() + yDiff;
                final BlockAtPosition next = map.getAt(x, y);
                final int distance = Math.abs(xDiff) + Math.abs(yDiff);
                final long cheatDistance = getDistance(start, next) - distance;
                if (cheatDistance >= limit) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private long getDistance(final BlockAtPosition one, final BlockAtPosition other) {
        if (one == null || one.getDistance() == Long.MAX_VALUE || other == null
                || other.getDistance() == Long.MAX_VALUE) {
            return 0L;
        }
        return other.getDistance() - one.getDistance();
    }

    private static class BlockAtPosition extends CharEnumAtPosition<Block> {

        private long distance = Long.MAX_VALUE;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public boolean setDistance(final long distance) {
            if (this.distance > distance) {
                this.distance = distance;
                return true;
            }
            return false;
        }

        public long getDistance() {
            return distance;
        }

        @Override
        public char toPrintableChar() {
            if (distance != Long.MAX_VALUE) {
                return (char) ('0' + distance % 10);
            }
            return super.toPrintableChar();
        }
    }

    private static enum Block implements CharEnum {

        PATH('.'),
        WALL('#'),
        START('S'),
        END('E');

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
