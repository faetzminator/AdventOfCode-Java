package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final int INVALID_SOLUTION = Integer.MAX_VALUE;

    private long solution;

    public void parseLine(final String line) {
        final Input input = new Input(line);
        solution += play(input.getIndicatorLights(), input.getButtons());
    }

    private int play(final IndicatorLights lights, final Buttons buttons) {
        return play(lights, buttons, 0);
    }

    private int play(final IndicatorLights lastState, final Buttons buttons, final int startAtButton) {
        int min = INVALID_SOLUTION;
        for (int i = startAtButton; i < buttons.length(); i++) {
            final IndicatorLights state = lastState.copy();
            if (buttons.press(i, state)) {
                return 1;
            }
            final int solution = play(state, buttons, i + 1);
            if (solution != INVALID_SOLUTION) {
                min = Math.min(min, solution + 1);
            }
        }
        return min;
    }

    public long getSolution() {
        return solution;
    }

    public static enum IndicatorLight implements CharEnum {

        OFF('.'),
        ON('#');

        private final char character;

        private IndicatorLight(final char character) {
            this.character = character;
        }

        public IndicatorLight toggle() {
            return this == ON ? OFF : ON;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static IndicatorLight byChar(final char c) {
            return CharEnum.byChar(IndicatorLight.class, c);
        }
    }

    public static class IndicatorLights {

        private final IndicatorLight[] state;
        private final IndicatorLight[] targetState;

        public IndicatorLights(final String definition) {
            state = new IndicatorLight[definition.length()];
            targetState = new IndicatorLight[state.length];
            for (int i = 0; i < state.length; i++) {
                state[i] = IndicatorLight.OFF;
                targetState[i] = IndicatorLight.byChar(definition.charAt(i));
            }
        }

        private IndicatorLights(final IndicatorLight[] state, final IndicatorLight[] targetState) {
            this.state = state;
            this.targetState = targetState;
        }

        public void toggle(final int index) {
            state[index] = state[index].toggle();
        }

        public boolean isTargetState() {
            return Arrays.equals(state, targetState);
        }

        public IndicatorLights copy() {
            return new IndicatorLights(Arrays.copyOf(state, state.length), targetState);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(targetState.length + 2);
            builder.append('[');
            Arrays.stream(targetState).forEach(light -> builder.append(light.charValue()));
            return builder.append(']').toString();
        }
    }

    public static class Button {

        private final int[] affectedIndexes;

        public Button(final String definition) {
            final String[] definitions = definition.substring(1, definition.length() - 1).split(",");
            affectedIndexes = Arrays.stream(definitions).mapToInt(Integer::valueOf).toArray();
        }

        public void press(final IndicatorLights lights) {
            for (final int index : affectedIndexes) {
                lights.toggle(index);
            }
        }

        @Override
        public String toString() {
            final Stream<String> stream = Arrays.stream(affectedIndexes).mapToObj(String::valueOf);
            return "(" + stream.collect(Collectors.joining(",")) + ")";
        }
    }

    public static class Buttons {

        private final Button[] buttons;

        public Buttons(final String definition) {
            buttons = Arrays.stream(definition.split(" ")).map(Button::new).toArray(Button[]::new);
        }

        public boolean press(final int index, final IndicatorLights lights) {
            buttons[index].press(lights);
            return lights.isTargetState();
        }

        public int length() {
            return buttons.length;
        }

        @Override
        public String toString() {
            return Arrays.stream(buttons).map(Button::toString).collect(Collectors.joining(" "));
        }
    }

    public static class Input {

        private static final Pattern LINE_PATTERN = Pattern
                .compile("\\[(.*?)\\] (\\(\\d+(?:,\\d+)*\\)(?: \\(\\d+(?:,\\d+)*\\))*) \\{(\\d+(?:,\\d+)*)\\}");

        private final IndicatorLights indicatorLights;
        private final Buttons buttons;
        private final String joltages;

        public Input(final String input) {
            final Matcher matcher = LINE_PATTERN.matcher(input);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("line: " + input);
            }
            indicatorLights = new IndicatorLights(matcher.group(1));
            buttons = new Buttons(matcher.group(2));
            joltages = "{" + matcher.group(3) + "}";
        }

        public IndicatorLights getIndicatorLights() {
            return indicatorLights;
        }

        public Buttons getButtons() {
            return buttons;
        }

        @Override
        public String toString() {
            return indicatorLights + " " + buttons + " " + joltages;
        }
    }
}
