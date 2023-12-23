package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

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

    private Map map;

    public void parseLines(final List<String> input) {
        map = new Map(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.setBlockAt(new Position(x, y), Block.byChar(line.charAt(x)));
            }
        }
    }

    public long findLongestPath() {
        final BlockAtPosition startBlock = map.getBlockAt(new Position(1, 0));
        final Position endPos = new Position(map.getXSize() - 2, map.getYSize() - 1);

        final Queue<BlockAtPositionWithDirection> queue = new LinkedList<>();
        queue.add(new BlockAtPositionWithDirection(startBlock, Direction.SOUTH));
        startBlock.setDistance(0);

        while (!queue.isEmpty()) {
            final BlockAtPositionWithDirection lastMove = queue.poll();
            for (final Direction direction : lastMove.getBlockAtPosition().getBlock().getExits()) {
                if (direction != lastMove.getDirection().getOpposite()) {
                    final Position nextPos = lastMove.getBlockAtPosition().getPosition().move(direction);
                    final BlockAtPosition nextBlock = map.getBlockAt(nextPos);
                    if (nextBlock != null && nextBlock.getBlock().canEnter(direction.getOpposite())) {
                        nextBlock.setDistance(lastMove.getBlockAtPosition().getDistance() + 1);
                        queue.add(new BlockAtPositionWithDirection(nextBlock, direction));
                    }
                }
            }
        }

        return map.getBlockAt(endPos).getDistance();
    }

    private static class Position {

        private final int x;
        private final int y;

        public Position(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Position move(final Direction direction) {
            switch (direction) {
            case NORTH:
                return new Position(x, y - 1);
            case EAST:
                return new Position(x + 1, y);
            case SOUTH:
                return new Position(x, y + 1);
            case WEST:
                return new Position(x - 1, y);
            }
            throw new IllegalArgumentException();
        }
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

    private static class BlockAtPosition {

        private final Position position;
        private final Block block;
        private int distance = -1;

        public BlockAtPosition(final Position position, final Block block) {
            this.position = position;
            this.block = block;
        }

        public Position getPosition() {
            return position;
        }

        public Block getBlock() {
            return block;
        }

        public void setDistance(final int distance) {
            if (distance > this.distance) {
                this.distance = distance;
            }
        }

        public int getDistance() {
            return distance;
        }
    }

    private static class Map {

        private final BlockAtPosition[][] map;

        public Map(final int xSize, final int ySize) {
            map = new BlockAtPosition[ySize][xSize];
        }

        public void setBlockAt(final Position position, final Block block) {
            map[position.getY()][position.getX()] = new BlockAtPosition(position, block);
        }

        public BlockAtPosition getBlockAt(final Position position) {
            return isInBounds(position) ? map[position.getY()][position.getX()] : null;
        }

        public boolean isInBounds(final Position position) {
            return position.getX() >= 0 && position.getY() >= 0 && position.getX() < map[0].length
                    && position.getY() < map.length;
        }

        public int getXSize() {
            return map[0].length;
        }

        public int getYSize() {
            return map.length;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            for (final BlockAtPosition[] element : map) {
                for (final BlockAtPosition element2 : element) {
                    if (element2.getDistance() > 0) {
                        builder.append(element2.getDistance() % 10);
                    } else {
                        builder.append(element2.getBlock().getCharacter());
                    }
                }
                builder.append('\n');
            }
            builder.setLength(builder.length() - 1);
            return builder.toString();
        }
    }

    private static enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction getOpposite() {
            return values()[(ordinal() + 2) % 4];
        }
    }

    private static enum Block {

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

        public char getCharacter() {
            return character;
        }

        public static Block byChar(final char c) {
            for (final Block block : values()) {
                if (block.getCharacter() == c) {
                    return block;
                }
            }
            throw new IllegalArgumentException("char: " + c);
        }

        public boolean canEnter(final Direction fromDirection) {
            return entries.contains(fromDirection);
        }

        public Set<Direction> getExits() {
            return exits;
        }
    }
}
