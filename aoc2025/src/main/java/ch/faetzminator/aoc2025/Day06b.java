package ch.faetzminator.aoc2025;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day06b {

    public static void main(final String[] args) {
        final Day06b puzzle = new Day06b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLastLine(lines.remove(lines.size() - 1));
        puzzle.parseLines(lines);
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

    public void parseLines(final List<String> lines) {
        int at = operations.length - 1;
        for (int j = lines.get(0).length() - 1; j >= 0; j--) {
            final char[] chars = new char[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                chars[i] = lines.get(i).charAt(j);
            }
            final String input = String.valueOf(chars).trim();
            if (!input.isEmpty()) {
                results[at] = operations[at].calculate(results[at], Long.parseLong(input));
            } else {
                at--;
            }
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
