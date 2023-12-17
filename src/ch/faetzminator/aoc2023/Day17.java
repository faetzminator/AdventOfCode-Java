package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day17 {

    public static void main(final String[] args) {
        final Day17 puzzle = new Day17();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        puzzle.parseLines(input);
        System.out.println("Solution: " + puzzle.calculateLeastHeatLoss());
    }

    private Map map;

    public void parseLines(final List<String> input) {
        map = new Map(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.setBlockAt(new Position(x, y), line.charAt(x) - '0');
            }
        }
    }

    private static final int MAX_LENGTH = 3;

    private long calculateLeastHeatLoss() {
        final Queue<QueueItem> queue = new LinkedList<>();
        final Position startPos = new Position(0, 0);
        queue.add(new QueueItem(startPos, new CacheKey(Direction.EAST, 0)));
        queue.add(new QueueItem(startPos, new CacheKey(Direction.SOUTH, 0)));

        while (!queue.isEmpty()) {
            final QueueItem queueItem = queue.poll();
            final Position current = queueItem.getPosition();
            final Direction direction = queueItem.getCacheKey().getDirection();
            final int length = queueItem.getCacheKey().getLength();

            final int heatLoss = map.getBlockAt(current).getHeatLoss(new CacheKey(direction, length));

            for (final Direction nextDirection : direction.getPossibleDirections(length < MAX_LENGTH)) {
                final int nextLength = direction == nextDirection ? length + 1 : 1;
                final Position nextPosition = current.move(nextDirection);
                final BlockAtPosition nextBlock = map.getBlockAt(nextPosition);
                final CacheKey key = new CacheKey(nextDirection, nextLength);
                if (nextBlock != null && nextBlock.setHeatLoss(key, heatLoss + nextBlock.getHeatLoss())) {
                    queue.add(new QueueItem(nextPosition, new CacheKey(nextDirection, nextLength)));
                }
            }
        }

        final Position endPos = new Position(map.getXSize() - 1, map.getYSize() - 1);
        return map.getBlockAt(endPos).getLowestHeatLoss();
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
        private final int heatLoss;
        private final java.util.Map<CacheKey, Integer> heatLossCache = new HashMap<>();

        public BlockAtPosition(final int heatLoss) {
            this.heatLoss = heatLoss;
        }

        public int getHeatLoss() {
            return heatLoss;
        }

        public int getHeatLoss(final CacheKey key) {
            final Integer value = heatLossCache.get(key);
            return value != null ? value.intValue() : 0;
        }

        public boolean setHeatLoss(final CacheKey key, final int newHeatLoss) {
            if (heatLossCache.containsKey(key)) {
                final int heatLoss = heatLossCache.get(key);
                if (heatLoss <= newHeatLoss) {
                    return false;
                }
            }
            heatLossCache.put(key, newHeatLoss);
            return true;
        }

        public int getLowestHeatLoss() {
            int lowest = Integer.MAX_VALUE;
            for (final int loss : heatLossCache.values()) {
                if (loss < lowest) {
                    lowest = loss;
                }
            }
            return lowest;
        }
    }

    private static class Map {

        private final BlockAtPosition[][] map;

        public Map(final int xSize, final int ySize) {
            map = new BlockAtPosition[ySize][xSize];
        }

        public void setBlockAt(final Position position, final int heatLoss) {
            map[position.getY()][position.getX()] = new BlockAtPosition(heatLoss);
        }

        public BlockAtPosition getBlockAt(final Position position) {
            return isInBounds(position) ? map[position.getY()][position.getX()] : null;
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
                    builder.append(element2.getHeatLoss());
                }
                builder.append('\n');
            }
            builder.setLength(builder.length() - 1);
            return builder.toString();
        }

        public int getXSize() {
            return map[0].length;
        }

        public int getYSize() {
            return map.length;
        }
    }

    private static enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Set<Direction> getPossibleDirections(final boolean inclOwn) {
            final Direction[] values = values();
            final int ordinal = ordinal();
            final Set<Direction> directions = new HashSet<>(Arrays.asList(values));
            if (!inclOwn) {
                directions.remove(this);
            }
            directions.remove(values[(ordinal + 2) % 4]);
            return directions;
        }
    }

    private static class CacheKey {
        private final Direction direction;
        private final int length;

        public CacheKey(final Direction direction, final int length) {
            this.direction = direction;
            this.length = length;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getLength() {
            return length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, length);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final CacheKey other = (CacheKey) obj;
            return direction == other.direction && length == other.length;
        }
    }

    private static class QueueItem {
        private final Position position;
        private final CacheKey cacheKey;

        public QueueItem(final Position position, final CacheKey cacheKey) {
            this.position = position;
            this.cacheKey = cacheKey;
        }

        public Position getPosition() {
            return position;
        }

        public CacheKey getCacheKey() {
            return cacheKey;
        }
    }
}
