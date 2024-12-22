package ch.faetzminator.aoc2024;

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

public class Day21b {

    public static void main(final String[] args) {
        final Day21b puzzle = new Day21b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.initialize();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getComplexitySum();
        PuzzleUtil.end(solution, timer);
    }

    private static final int DIRECTIONAL_KEYPADS = 25;

    private long complexitySum;
    private Keypad keypad;

    public void initialize() {
        final char[][] directional = new char[][] { { ' ', '^', 'A' }, { '<', 'v', '>' } };
        for (int i = 0; i < DIRECTIONAL_KEYPADS; i++) {
            keypad = new Keypad(keypad, createPositionMap(directional));
        }
        keypad = new Keypad(keypad, createPositionMap(
                new char[][] { { '7', '8', '9' }, { '4', '5', '6' }, { '1', '2', '3' }, { ' ', '0', 'A' } }));
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
        final long moves = keypad.getFewestButtonPresses(input);
        complexitySum += moves * Long.parseLong(matcher.group(1));
    }

    public long getComplexitySum() {
        return complexitySum;
    }

    private static class Keypad {

        private final Keypad next;
        private char current;
        private final Map<Character, Position> positions;
        private final Set<Position> occupied;

        public Keypad(final Keypad next, final Map<Character, Position> positions) {
            this.next = next;
            this.positions = positions;
            occupied = new HashSet<>(positions.values());
            current = 'A';
        }

        private static void append(final StringBuilder builder, final char character, int length) {
            while (length-- > 0) {
                builder.append(character);
            }
        }

        public String press(final char character) {
            if (!positions.containsKey(character)) {
                throw new IllegalArgumentException("char " + character);
            }
            final Position position = positions.get(current);
            final Position next = positions.get(current = character);
            final boolean xFirst = occupied.contains(new Position(next.getX(), position.getY()));
            final boolean yFirst = occupied.contains(new Position(position.getX(), next.getY()));
            if (yFirst && (!xFirst || next.getX() - position.getX() > 0)) {
                final StringBuilder one = new StringBuilder();
                append(one, 'v', next.getY() - position.getY());
                append(one, '^', position.getY() - next.getY());
                append(one, '>', next.getX() - position.getX());
                append(one, '<', position.getX() - next.getX());
                return one.append('A').toString();
            }
            final StringBuilder one = new StringBuilder();
            append(one, '>', next.getX() - position.getX());
            append(one, '<', position.getX() - next.getX());
            append(one, 'v', next.getY() - position.getY());
            append(one, '^', position.getY() - next.getY());
            return one.append('A').toString();
        }

        private final Map<String, Long> cache = new HashMap<>();

        public long getFewestButtonPresses(final String input) {
            if (cache.containsKey(input)) {
                return cache.get(input);
            }
            long length = 0L;
            for (final char character : input.toCharArray()) {
                final String moves = press(character);
                if (next != null) {
                    length += next.getFewestButtonPresses(moves);
                } else {
                    length += moves.length();
                }
            }
            cache.put(input, length);
            return length;
        }
    }
}
