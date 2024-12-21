package ch.faetzminator.aoc2024;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.Position;

public class Day21 {

    public static void main(final String[] args) {
        final Day21 puzzle = new Day21();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.initialize();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getComplexitySum();
        PuzzleUtil.end(solution, timer);
    }

    private static final int DIRECTIONAL_KEYPADS = 2;

    private long complexitySum;
    private Keypad keypad;

    public void initialize() {
        keypad = new Keypad(createPositionMap(
                new char[][] { { '7', '8', '9' }, { '4', '5', '6' }, { '1', '2', '3' }, { ' ', '0', 'A' } }));
        final char[][] directional = new char[][] { { ' ', '^', 'A' }, { '<', 'v', '>' } };
        for (int i = 0; i < DIRECTIONAL_KEYPADS; i++) {
            keypad = new Keypad(keypad, createPositionMap(directional));
        }
    }

    private Map<Character, Position> createPositionMap(final char[][] characters) {
        final Map<Character, Position> positions = new HashMap<>();
        for (int y = 0; y < characters.length; y++) {
            for (int x = 0; x < characters[0].length; x++) {
                if (characters[y][x] != ' ') {
                    positions.put(characters[y][x], new Position(x, y));
                }
            }
        }
        return positions;
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+)A");

    public void parseLine(final String input) {

        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final long moves = countMoves(input);
        complexitySum += moves * Long.parseLong(matcher.group(1));
    }

    private long countMoves(final String input) {
        keypad.reset();
        return keypad.getShortestLength(input);
    }

    public long getComplexitySum() {
        return complexitySum;
    }

    private static class Keypad {

        private final Keypad next;
        private Position position;
        private final Map<Character, Position> positions;
        private final Set<Position> occupied;

        public Keypad(final Map<Character, Position> positions) {
            this(null, positions);
        }

        public Keypad(final Keypad next, final Map<Character, Position> positions) {
            this.next = next;
            this.positions = positions;
            occupied = new HashSet<>(positions.values());
            reset();
        }

        public void reset() {
            position = positions.get('A');
            if (next != null) {
                next.reset();
            }
        }

        private static void append(final StringBuilder builder, final char character, int length) {
            while (length-- > 0) {
                builder.append(character);
            }
        }

        public Collection<String> press(final char character) {
            final Set<String> solutions = new HashSet<>();
            if (!positions.containsKey(character)) {
                throw new IllegalArgumentException("char " + character);
            }
            final Position next = positions.get(character);
            if (occupied.contains(new Position(next.getX(), position.getY()))) {
                final StringBuilder one = new StringBuilder();
                append(one, '>', next.getX() - position.getX());
                append(one, '<', position.getX() - next.getX());
                append(one, 'v', next.getY() - position.getY());
                append(one, '^', position.getY() - next.getY());
                solutions.add(one.append('A').toString());
            }
            if (occupied.contains(new Position(position.getX(), next.getY()))) {
                final StringBuilder one = new StringBuilder();
                append(one, 'v', next.getY() - position.getY());
                append(one, '^', position.getY() - next.getY());
                append(one, '>', next.getX() - position.getX());
                append(one, '<', position.getX() - next.getX());
                solutions.add(one.append('A').toString());
            }
            position = next;
            if (solutions.isEmpty()) {
                throw new IllegalArgumentException("no solution");
            }
            return solutions;
        }

        private Collection<String> press(final String input) {
            Set<String> solutions = new HashSet<>(Arrays.asList(""));
            for (final char character : input.toCharArray()) {
                final Set<String> newSolutions = new HashSet<>();
                for (final String option : press(character)) {
                    for (final String solution : solutions) {
                        newSolutions.add(solution + option);
                    }
                }
                solutions = newSolutions;
            }
            return solutions;
        }

        private Collection<String> pressAll(Collection<String> inputs) {
            if (next != null) {
                inputs = next.pressAll(inputs);
            }
            final Set<String> all = new HashSet<>();
            for (final String input : inputs) {
                all.addAll(press(input));
            }
            int minLength = Integer.MAX_VALUE;
            for (final String one : all) {
                if (one.length() < minLength) {
                    minLength = one.length();
                }
            }
            final Set<String> toRemove = new HashSet<>();
            for (final String one : all) {
                if (one.length() > minLength) {
                    toRemove.add(one);
                }
            }
            all.removeAll(toRemove);
            return all;
        }

        public long getShortestLength(final String input) {
            return pressAll(Arrays.asList(input)).iterator().next().length();
        }
    }
}
