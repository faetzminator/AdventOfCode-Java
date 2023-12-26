package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PMap;
import ch.faetzminator.aocutil.Position;

public class Day17b {

    public static void main(final String[] args) {
        final Day17b puzzle = new Day17b();

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

    private PMap<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMap<>(BlockAtPosition.class, input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.setElementAt(new Position(x, y), new BlockAtPosition(line.charAt(x) - '0'));
            }
        }
    }

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 10;

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

            final int heatLoss = map.getElementAt(current).getHeatLoss(new CacheKey(direction, length));

            for (final Direction nextDirection : getPossibleDirections(direction, length >= MIN_LENGTH,
                    length < MAX_LENGTH)) {
                final int nextLength = direction == nextDirection ? length + 1 : 1;
                final Position nextPosition = current.move(nextDirection);
                final BlockAtPosition nextBlock = map.getElementAt(nextPosition);
                final CacheKey key = new CacheKey(nextDirection, nextLength);
                if (nextBlock != null && nextBlock.setHeatLoss(key, heatLoss + nextBlock.getHeatLoss())) {
                    queue.add(new QueueItem(nextPosition, new CacheKey(nextDirection, nextLength)));
                }
            }
        }

        final Position endPos = new Position(map.getXSize() - 1, map.getYSize() - 1);
        return map.getElementAt(endPos).getLowestHeatLoss();
    }

    private static class BlockAtPosition implements CharPrintable {
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
            for (final Entry<CacheKey, Integer> entry : heatLossCache.entrySet()) {
                if (entry.getKey().getLength() >= MIN_LENGTH && entry.getValue() < lowest) {
                    lowest = entry.getValue();
                }
            }
            return lowest;
        }

        @Override
        public char toPrintableChar() {
            return (char) (heatLoss % 10 + '0');
        }
    }

    private static Set<Direction> getPossibleDirections(final Direction value, final boolean inclOthers,
            final boolean inclOwn) {

        if (!inclOthers) {
            return inclOwn ? Set.of(value) : Set.of();
        }
        final Set<Direction> directions = new HashSet<>(Arrays.asList(Direction.values()));
        directions.remove(value.getOpposite());
        if (!inclOwn) {
            directions.remove(value);
        }
        return directions;
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
