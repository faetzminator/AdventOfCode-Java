package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day21 {

    public static void main(final String[] args) {
        final Day21 puzzle = new Day21();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        final long solution = puzzle.countReachableGardenPlots();
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

    private static final int MAX_LENGTH = 64;

    private long countReachableGardenPlots() {
        long sum = 0;

        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(map.getBlockAt(map.getStartPosition()));

        while (!queue.isEmpty()) {
            final BlockAtPosition current = queue.poll();
            for (final Direction direction : Direction.values()) {
                final Position nextPos = current.getPosition().move(direction);
                final BlockAtPosition nextBlock = map.getBlockAt(nextPos);
                final int nextDistance = current.getDistance() + 1;
                if (nextBlock != null && nextBlock.getBlock() != Block.ROCK && nextBlock.getDistance() < nextDistance) {
                    nextBlock.setDistance(nextDistance);
                    if (nextDistance < MAX_LENGTH) {
                        queue.add(nextBlock);
                    } else {
                        sum++;
                    }
                }
            }
        }

        return sum;
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
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }
    }

    private static class Map {

        private final BlockAtPosition[][] map;
        private Position startPosition;

        public Map(final int xSize, final int ySize) {
            map = new BlockAtPosition[ySize][xSize];
        }

        public void setBlockAt(final Position position, final Block block) {
            map[position.getY()][position.getX()] = new BlockAtPosition(position, block);
            if (block == Block.START) {
                if (startPosition != null) {
                    throw new IllegalArgumentException("duplicate start");
                }
                map[position.getY()][position.getX()].setDistance(0);
                startPosition = position;
            }
        }

        public BlockAtPosition getBlockAt(final Position position) {
            return isInBounds(position) ? map[position.getY()][position.getX()] : null;
        }

        public Position getStartPosition() {
            if (startPosition == null) {
                throw new IllegalArgumentException("start not set");
            }
            return startPosition;
        }

        public boolean isInBounds(final Position position) {
            return position.getX() >= 0 && position.getY() >= 0 && position.getX() < map[0].length
                    && position.getY() < map.length;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            for (final BlockAtPosition[] element : map) {
                for (final BlockAtPosition element2 : element) {
                    if (element2.getBlock() == Block.GARDEN_PLOT && element2.getDistance() > 0) {
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
    }

    private static enum Block {

        START('S'), GARDEN_PLOT('.'), ROCK('#');

        private final char character;

        private Block(final char character) {
            this.character = character;
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
    }
}
