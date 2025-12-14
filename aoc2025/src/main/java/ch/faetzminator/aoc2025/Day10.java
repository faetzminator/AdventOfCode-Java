package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day10 {

    public static void main(final String[] args) {
        final Day10 puzzle = new Day10();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern
            .compile("\\[(.*?)\\] (\\(\\d+(?:,\\d+)*\\)(?: \\(\\d+(?:,\\d+)*\\))*) \\{(\\d+(?:,\\d+)*)\\}");

    private long solution;

    public void parseLine(final String input) {
        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }

        final String[] inputs = input.split(" ");
        final String indicatorLightsStr = matcher.group(1);
        final boolean[] expected = new boolean[indicatorLightsStr.length()];
        for (int i = 0; i < indicatorLightsStr.length(); i++) {
            expected[i] = IndicatorLight.byChar(indicatorLightsStr.charAt(i)).isOn();
        }

        final String[] buttonsStrs = matcher.group(2).split(" ");

        // System.out.println("machine " + String.valueOf(chars));
        final int[][] buttons = new int[inputs.length - 2][];
        for (int i = 1; i < inputs.length - 1; i++) {
            final String[] str = inputs[i].substring(1, inputs[i].length() - 1).split(",");
            buttons[i - 1] = new int[str.length];
            for (int j = 0; j < str.length; j++) {
                buttons[i - 1][j] = Integer.parseInt(str[j]);
            }
            // System.out.println("button " + Arrays.toString(buttons[i - 1]));
        }
        solution += play(expected, buttons);
    }

    private int play(final boolean[] expected, final int[][] buttons) {
        return play(expected, buttons, new boolean[expected.length], 0);
    }

    private int play(final boolean[] expected, final int[][] buttons, final boolean[] state, final int pos) {
        int min = Integer.MAX_VALUE;
        for (int i = pos; i < buttons.length; i++) {
            final boolean[] newState = Arrays.copyOf(state, state.length);
            flip(newState, buttons[i]);
            if (Arrays.equals(expected, newState)) {
                return 1;
            }
            final int sol = play(expected, buttons, newState, i + 1);
            if (sol != Integer.MAX_VALUE) {
                min = Math.min(min, sol + 1);
            }
        }
        return min;
    }

    private void flip(final boolean[] state, final int[] positions) {
        for (final int position : positions) {
            state[position] = !state[position];
        }
    }

    public long getSolution() {
        return solution;
    }

    public static enum IndicatorLight implements CharEnum {

        OFF('.', false),
        ON('#', true);

        private final char character;
        private final boolean on;

        private IndicatorLight(final char character, final boolean on) {
            this.character = character;
            this.on = on;
        }

        public boolean isOn() {
            return on;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static IndicatorLight byChar(final char c) {
            return CharEnum.byChar(IndicatorLight.class, c);
        }
    }
}
