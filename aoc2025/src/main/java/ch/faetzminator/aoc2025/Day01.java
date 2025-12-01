package ch.faetzminator.aoc2025;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day01 {

    public static void main(final String[] args) {
        final Day01 puzzle = new Day01();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("(.)(\\d+)");

    private final static int POSITIONS = 100;

    private int position = 50;
    private long solution = 0;

    public void parseLine(final String input) {
        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final Direction direction = Direction.byChar(matcher.group(1).charAt(0));
        final int moveBy = direction.parseNumber(matcher.group(2));

        position = Math.floorMod(position + moveBy, POSITIONS);
        if (position == 0) {
            solution++;
        }
    }

    public long getSolution() {
        return solution;
    }

    private static enum Direction implements CharEnum {

        LEFT('L', str -> -Integer.parseInt(str)),
        RIGHT('R', Integer::parseInt);

        private final char character;
        private final Function<String, Integer> numberFn;

        private Direction(final char character, final Function<String, Integer> numberFn) {
            this.character = character;
            this.numberFn = numberFn;
        }

        @Override
        public char charValue() {
            return character;
        }

        public int parseNumber(final String str) {
            return numberFn.apply(str);
        }

        public static Direction byChar(final char c) {
            return CharEnum.byChar(Direction.class, c);
        }
    }
}
