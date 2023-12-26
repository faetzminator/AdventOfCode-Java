package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.ElementAtPosition;
import ch.faetzminator.aocutil.PMap;
import ch.faetzminator.aocutil.Position;

public class Day23 {

    public static void main(final String[] args) {
        final Day23 puzzle = new Day23();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        final long solution = puzzle.findLongestPath();
        System.out.println("Solution: " + solution);
    }

    private PMap<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMap<>(BlockAtPosition.class, input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                final BlockAtPosition block = new BlockAtPosition(new Position(x, y), Block.byChar(line.charAt(x)));
                map.setElementAt(block.getPosition(), block);
            }
        }
    }

    public long findLongestPath() {
        final BlockAtPosition startBlock = map.getElementAt(new Position(1, 0));
        final Position endPos = new Position(map.getXSize() - 2, map.getYSize() - 1);

        final Queue<BlockAtPositionWithDirection> queue = new LinkedList<>();
        queue.add(new BlockAtPositionWithDirection(startBlock, Direction.SOUTH));
        startBlock.setDistance(0);

        while (!queue.isEmpty()) {
            final BlockAtPositionWithDirection lastMove = queue.poll();
            for (final Direction direction : lastMove.getBlockAtPosition().getElement().getExits()) {
                if (direction != lastMove.getDirection().getOpposite()) {
                    final Position nextPos = lastMove.getBlockAtPosition().getPosition().move(direction);
                    final BlockAtPosition nextBlock = map.getElementAt(nextPos);
                    if (nextBlock != null && nextBlock.getElement().canEnter(direction.getOpposite())) {
                        nextBlock.setDistance(lastMove.getBlockAtPosition().getDistance() + 1);
                        queue.add(new BlockAtPositionWithDirection(nextBlock, direction));
                    }
                }
            }
        }

        return map.getElementAt(endPos).getDistance();
    }

    private static class BlockAtPositionWithDirection {
        private final BlockAtPosition blockAtPosition;
        private final Direction direction;

        public BlockAtPositionWithDirection(final BlockAtPosition blockAtPosition, final Direction direction) {
            this.blockAtPosition = blockAtPosition;
            this.direction = direction;
        }

        public BlockAtPosition getBlockAtPosition() {
            return blockAtPosition;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private int distance = -1;

        public BlockAtPosition(final Position position, final Block block) {
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

        PATH('.', false, Direction.values()), ROCK('#', false), SLOPE_UP('^', true, Direction.NORTH),
        SLOW_RIGHT('>', true, Direction.EAST), SLOPE_DOWN('v', true, Direction.SOUTH),
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
        public char getCharacter() {
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
