package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.ElementAtPosition;
import ch.faetzminator.aocutil.PMapWithStart;
import ch.faetzminator.aocutil.Position;

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

    private PMapWithStart<BlockAtPosition> map;

    public void parseLines(final List<String> input) {
        map = new PMapWithStart<>(BlockAtPosition.class, input.get(0).length(), input.size(),
                element -> element.getElement() == Block.START);

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                final BlockAtPosition block = new BlockAtPosition(new Position(x, y), Block.byChar(line.charAt(x)));
                map.setElementAt(block.getPosition(), block);
            }
        }
    }

    private static final int MAX_LENGTH = 64;

    private long countReachableGardenPlots() {
        long sum = 0;

        final Queue<BlockAtPosition> queue = new LinkedList<>();
        queue.add(map.getElementAt(map.getStartPosition()));

        while (!queue.isEmpty()) {
            final BlockAtPosition current = queue.poll();
            for (final Direction direction : Direction.values()) {
                final Position nextPos = current.getPosition().move(direction);
                final BlockAtPosition nextBlock = map.getElementAt(nextPos);
                final int nextDistance = current.getDistance() + 1;
                if (nextBlock != null && nextBlock.getElement() != Block.ROCK
                        && nextBlock.getDistance() < nextDistance) {
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

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        private int distance = -1;

        public BlockAtPosition(final Position position, final Block block) {
            super(block, position);
        }

        public void setDistance(final int distance) {
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public char toPrintableChar() {
            if (getElement() == Block.GARDEN_PLOT && distance > 0) {
                return (char) (distance % 10 + '0');
            }
            return super.toPrintableChar();
        }
    }

    private static enum Block implements CharEnum {

        START('S'), GARDEN_PLOT('.'), ROCK('#');

        private final char character;

        private Block(final char character) {
            this.character = character;
        }

        @Override
        public char getCharacter() {
            return character;
        }

        public static Block byChar(final char c) {
            return CharEnum.byChar(Block.class, c);
        }
    }
}
