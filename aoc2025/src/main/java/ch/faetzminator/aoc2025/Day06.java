package ch.faetzminator.aoc2025;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day06 {

    public static void main(final String[] args) {
        final Day06 puzzle = new Day06();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLastLine(lines.remove(lines.size() - 1));
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

    private Operation[] operations;
    private long[] results;

    public void parseLastLine(final String input) {
        final String[] inputs = SPLIT_PATTERN.split(input.trim());
        operations = new Operation[inputs.length];
        results = new long[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i].length() != 1) {
                throw new IllegalArgumentException(inputs[i]);
            }
            operations[i] = Operation.byChar(inputs[i].charAt(0));
            results[i] = operations[i].getInitialValue();
        }
    }

    public void parseLine(final String input) {
        final String[] inputs = SPLIT_PATTERN.split(input.trim());
        for (int i = 0; i < inputs.length; i++) {
            results[i] = operations[i].calculate(results[i], Long.parseLong(inputs[i]));
        }
    }

    public long getSolution() {
        long sum = 0L;
        for (final long value : results) {
            sum += value;
        }
        return sum;
    }

    private static enum Operation implements CharEnum {

        ADD('+', 0L, numbers -> numbers[0] + numbers[1]),
        MULTIPLY('*', 1L, numbers -> numbers[0] * numbers[1]);

        private final char character;
        private final long initialValue;
        private final Function<Long[], Long> numberFn;

        private Operation(final char character, final long initialValue, final Function<Long[], Long> numberFn) {
            this.character = character;
            this.initialValue = initialValue;
            this.numberFn = numberFn;
        }

        @Override
        public char charValue() {
            return character;
        }

        public long getInitialValue() {
            return initialValue;
        }

        public long calculate(final long one, final long other) {
            return numberFn.apply(new Long[] { one, other });
        }

        public static Operation byChar(final char c) {
            return CharEnum.byChar(Operation.class, c);
        }
    }
}
