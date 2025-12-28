package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    private int play(final IndicatorLights expectedState, final Buttons buttons) {
        return play(expectedState, buttons, new IndicatorLights(expectedState.length()), 0);
    }

    private int play(final IndicatorLights expectedState, final Buttons buttons, final IndicatorLights lastState,
            final int startAtButton) {

        int min = INVALID_SOLUTION;
        for (int i = startAtButton; i < buttons.length(); i++) {
            final IndicatorLights state = lastState.copy();
            buttons.get(i).press(state);
            if (state.equals(expectedState)) {
                return 1;
            }
            final int solution = play(expectedState, buttons, state, i + 1);
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

        private final IndicatorLight[] lights;

        public IndicatorLights(final int length) {
            lights = new IndicatorLight[length];
            for (int i = 0; i < lights.length; i++) {
                lights[i] = IndicatorLight.OFF;
            }
        }

        public IndicatorLights(final String definition) {
            this(definition.chars().mapToObj(c -> IndicatorLight.byChar((char) c)).toArray(IndicatorLight[]::new));
        }

        public IndicatorLights(final IndicatorLight[] lights) {
            this.lights = lights;
        }

        public void toggle(final int index) {
            lights[index] = lights[index].toggle();
        }

        public IndicatorLights copy() {
            return new IndicatorLights(Arrays.copyOf(lights, lights.length));
        }

        public int length() {
            return lights.length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(lights));
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final IndicatorLights other = (IndicatorLights) obj;
            return Arrays.equals(lights, other.lights);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(lights.length + 2);
            builder.append('[');
            Arrays.stream(lights).forEach(light -> builder.append(light.charValue()));
            return builder.append(']').toString();
        }
    }

    public static class Button {

        private final int[] affectedIndexes;

        public Button(final String definition) {
            this(definition.substring(1, definition.length() - 1).split(","));
        }

        private Button(final String[] definitions) {
            this(Arrays.stream(definitions).mapToInt(Integer::valueOf).toArray());
        }

        private Button(final int[] affectedIndexes) {
            this.affectedIndexes = affectedIndexes;
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
            this(Arrays.stream(definition.split(" ")).map(Button::new).toArray(Button[]::new));
        }

        public Buttons(final Button[] buttons) {
            this.buttons = buttons;
        }

        public Button get(final int index) {
            return buttons[index];
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
