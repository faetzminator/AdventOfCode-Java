package ch.faetzminator.aoc2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.CharEnumAtPosition;
import ch.faetzminator.aocutil.grid.ElementAtPositionWithDirection;
import ch.faetzminator.aocutil.grid.GridFactory;
import ch.faetzminator.aocutil.grid.GridWithStart;
import ch.faetzminator.aocutil.grid.Position;

public class Day16b {

    public static void main(final String[] args) {
        final Day16b puzzle = new Day16b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.calculateLowestScore();
        PuzzleUtil.end(solution, timer);
    }

    private GridWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> lines) {
        map = new GridFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position))
                .create(lines, block -> block.getElement() == Block.START, block -> block.getElement() == Block.END);
    }

    public long calculateLowestScore() {
        final Queue<BlockAtPositionWithDirection> queue = new LinkedList<>();
        queue.add(new BlockAtPositionWithDirection(map.getStartElement(), Direction.EAST));
        map.getStartElement().setScore(Direction.EAST, 0L);

        while (!queue.isEmpty()) {
            final BlockAtPositionWithDirection last = queue.poll();
            final BlockAtPosition block = last.getElementAtPosition();
            final Direction direction = last.getDirection();

            final long score = block.getScore(direction);
            final Direction opposite = direction.getOpposite();
            for (final Direction d : Direction.values()) {
                final BlockAtPosition next = block.move(map, d);
                if (next.getElement() != Block.WALL && d != opposite) {
                    final int turned = d == direction ? 0 : 1;
                    if (next.setScore(d, score + 1L + 1000L * turned)) {
                        queue.add(new BlockAtPositionWithDirection(next, d));
                    }
                }
            }
        }

        // same story again, just opposite starting at end element...
        final Set<BlockAtPosition> result = new HashSet<>();

        // add only directions with the lowest score for the start element
        final BlockAtPosition endElement = map.getEndElement();
        final long endScore = endElement.getLowestScore();
        for (final Direction direction : Direction.values()) {
            if (endElement.getScore(direction) == endScore) {
                queue.add(new BlockAtPositionWithDirection(endElement, direction));
            }
        }
        result.add(endElement);

        while (!queue.isEmpty()) {
            final BlockAtPositionWithDirection last = queue.poll();
            final BlockAtPosition block = last.getElementAtPosition();
            final Direction direction = last.getDirection();
            final long expected = block.getScore(direction) - 1L;
            final BlockAtPosition next = block.move(map, direction.getOpposite());

            for (final Direction d : Direction.values()) {
                final Direction opposite = d.getOpposite();
                final long score = next.getScore(opposite);
                // finding corners - some might call it buggy, but it's just ugly!
                if (score == expected || (d != opposite && score == expected - 1000L)) {
                    next.mark();
                    queue.add(new BlockAtPositionWithDirection(next, opposite));
                    result.add(next);
                }
            }
        }
        return result.size();
    }

    private static class BlockAtPositionWithDirection extends ElementAtPositionWithDirection<BlockAtPosition> {

        public BlockAtPositionWithDirection(final BlockAtPosition elementAtPosition, final Direction direction) {
            super(elementAtPosition, direction);
        }
    }

    private static class BlockAtPosition extends CharEnumAtPosition<Block> {

        private final Map<Direction, Long> scores = new HashMap<>();
        private boolean marked;

        public BlockAtPosition(final Block block, final Position position) {
            super(block, position);
        }

        public boolean setScore(final Direction direction, final long score) {
            if (!scores.containsKey(direction) || scores.get(direction) > score) {
                scores.put(direction, score);
                return true;
            }
            return false;
        }

        public long getScore(final Direction direction) {
            return scores.containsKey(direction) ? scores.get(direction) : Long.MAX_VALUE;
        }

        public long getLowestScore() {
            long lowest = Long.MAX_VALUE;
            for (final long score : scores.values()) {
                if (score < lowest) {
                    lowest = score;
                }
            }
            return lowest;
        }

        public void mark() {
            marked = true;
        }

        @Override
        public char toPrintableChar() {
            return marked ? 'O' : super.toPrintableChar();
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
