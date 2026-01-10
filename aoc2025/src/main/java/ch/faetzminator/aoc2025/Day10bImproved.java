package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day10bImproved {

    /**
     * Improved version of brute-forcing as per tenthmascot.
     *
     * @see https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
     */
    public static void main(final String[] args) {
        final Day10bImproved puzzle = new Day10bImproved();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final int INVALID_SOLUTION = Integer.MAX_VALUE;

    private final AtomicLong solution = new AtomicLong();

    public void parseLines(final List<String> lines) {
        lines.parallelStream().forEach(this::parseLine);
    }

    public void parseLine(final String line) {
        final Input input = new Input(line);
        solution.getAndAdd(play(input.getIndicatorLights(), input.getJoltages(), input.getButtons()));
    }

    private int play(final IndicatorLights lights, final Joltages joltages, final Buttons buttons) {
        if (lights.isTargetState()) { // pitfall - target state got even joltages only
            // continue to part 2, no part 1 needed
            return play(joltages, buttons);
        }
        return play(lights, joltages, buttons, 0) + 1;
    }

    private int play(final IndicatorLights lastState, final Joltages lastJoltages, final Buttons buttons,
            final int startAtButton) {
        int min = INVALID_SOLUTION;
        for (int i = startAtButton; i < buttons.length(); i++) {
            final IndicatorLights state = lastState.copy();
            final Joltages joltages = lastJoltages.copy();

            // pitfall - target state might have low values, pressing could overshoot
            final int press = Math.min(1, buttons.get(i).maxPresses(joltages));
            if (press == 1 && buttons.press(i, state, joltages)) {
                // continue to part 2
                min = Math.min(min, play(joltages, buttons));
            }
            final int solution = play(state, joltages, buttons, i + 1);
            if (solution != INVALID_SOLUTION) {
                min = Math.min(min, solution + press);
            }
        }
        return min;
    }

    private int play(final Joltages joltages, final Buttons buttons) {
        return play(joltages, buttons, IntStream.range(0, joltages.length()).toArray());
    }

    private int[] filterAndSort(final Joltages state, final Buttons buttons, final int[] relevantJoltageIndexes) {
        final int[] joltageIndexByButtonRefCount = new int[state.length()];
        for (final Button button : buttons) {
            IntStream.of(button.getAffectedIndexes()).forEach(index -> joltageIndexByButtonRefCount[index]++);
        }
        // filter out joltage indexes referenced by left-over buttons and sort by lowest references
        return Arrays.stream(relevantJoltageIndexes).filter(o -> joltageIndexByButtonRefCount[o] > 0).boxed()
                .sorted(Comparator.comparingInt(o -> joltageIndexByButtonRefCount[o])).mapToInt(v -> v).toArray();
    }

    private int play(final Joltages state, final Buttons buttons, int[] relevantJoltageIndexes) {
        relevantJoltageIndexes = filterAndSort(state, buttons, relevantJoltageIndexes);
        if (relevantJoltageIndexes.length == 0) {
            return INVALID_SOLUTION;
        }

        final PartiallyRelevantButtons splitButtons = splitRelevantButtons(buttons, relevantJoltageIndexes[0]);
        return play(state, splitButtons, relevantJoltageIndexes, splitButtons.length() - 1);
    }

    private int play(final Joltages lastState, final PartiallyRelevantButtons buttons,
            final int[] relevantJoltageIndexes, final int lookingAtButton) {

        if (lookingAtButton == 0) {
            // last button, we have to increment until we hit expected count
            final int pressesNeeded = lastState.getDiff(relevantJoltageIndexes[0]);
            if (buttons.get(lookingAtButton).maxPresses(lastState) < pressesNeeded) {
                return INVALID_SOLUTION;
            }
            final Joltages state = lastState.copy();
            if (buttons.get(lookingAtButton).press(state, pressesNeeded)) {
                return pressesNeeded;
            }
            if (relevantJoltageIndexes.length == 1) {
                return INVALID_SOLUTION;
            }
            final int[] newJoltageIndexes = Arrays.copyOfRange(relevantJoltageIndexes, 1,
                    relevantJoltageIndexes.length);
            final int sol = play(state, buttons.getIrrelevantButtons(), newJoltageIndexes);
            if (sol != INVALID_SOLUTION) {
                return sol + pressesNeeded;
            }
            return INVALID_SOLUTION;
        }
        // for all other buttons, we do recursion for 0 to maxPresses, proceeding with button n-1
        final int maxPresses = buttons.get(lookingAtButton).maxPresses(lastState);
        if (lastState.getDiff(relevantJoltageIndexes[0]) < maxPresses) {
            return INVALID_SOLUTION;
        }
        final Joltages state = lastState.copy();
        int min = play(state, buttons, relevantJoltageIndexes, lookingAtButton - 1);
        for (int presses = 2; presses <= maxPresses; presses += 2) {
            buttons.get(lookingAtButton).press(state, 2);
            final int sol = play(state, buttons, relevantJoltageIndexes, lookingAtButton - 1);
            if (sol != INVALID_SOLUTION) {
                min = Math.min(min, sol + presses);
            }
        }
        return min;
    }

    private PartiallyRelevantButtons splitRelevantButtons(final Buttons buttons, final int lookingAtJoltage) {
        int relevantSize = 0;
        int irrelevantSize = 0;
        final Button[] relevantButtons = new Button[buttons.length()];
        final Button[] irrelevantButtons = new Button[buttons.length()];
        for (final Button button : buttons) {
            if (IntStream.of(button.getAffectedIndexes()).anyMatch(index -> index == lookingAtJoltage)) {
                relevantButtons[relevantSize++] = button;
            } else {
                irrelevantButtons[irrelevantSize++] = button;
            }
        }
        // sort by button "influence" - reduces the tries a bit
        final Button[] relevantButtonsSorted = Arrays.copyOf(relevantButtons, relevantSize);
        Arrays.sort(relevantButtonsSorted, Comparator.comparingInt(o -> o.getAffectedIndexes().length));
        return new PartiallyRelevantButtons(relevantButtonsSorted, Arrays.copyOf(irrelevantButtons, irrelevantSize));
    }

    public long getSolution() {
        return solution.get();
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

        public IndicatorLights(final Joltages joltages) {
            state = new IndicatorLight[joltages.length()];
            targetState = new IndicatorLight[state.length];
            for (int i = 0; i < state.length; i++) {
                state[i] = IndicatorLight.OFF;
                targetState[i] = joltages.getDiff(i) % 2 == 0 ? IndicatorLight.OFF : IndicatorLight.ON;
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

    public static class Joltages {

        private final int[] state;
        private final int[] targetState;

        public Joltages(final String definition) {
            targetState = Arrays.stream(definition.split(",")).mapToInt(Integer::parseInt).toArray();
            state = new int[targetState.length];
        }

        private Joltages(final int[] state, final int[] targetState) {
            this.state = state;
            this.targetState = targetState;
        }

        public int getDiff(final int index) {
            return targetState[index] - state[index];
        }

        public void increment(final int index, final int by) {
            state[index] += by;
        }

        public int length() {
            return state.length;
        }

        public boolean isTargetState() {
            return Arrays.equals(state, targetState);
        }

        public Joltages copy() {
            return new Joltages(Arrays.copyOf(state, state.length), targetState);
        }

        @Override
        public String toString() {
            final Stream<String> stream = Arrays.stream(targetState).mapToObj(String::valueOf);
            return "{" + stream.collect(Collectors.joining(",")) + "}";
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

        public void press(final IndicatorLights lights, final Joltages joltages) {
            for (final int index : affectedIndexes) {
                lights.toggle(index);
                joltages.increment(index, 1);
            }
        }

        public boolean press(final Joltages joltages, final int times) {
            for (final int index : affectedIndexes) {
                joltages.increment(index, times);
            }
            return joltages.isTargetState();
        }

        public int maxPresses(final Joltages joltages) {
            int min = Integer.MAX_VALUE;
            for (final int index : affectedIndexes) {
                min = Math.min(min, joltages.getDiff(index));
            }
            return min;
        }

        public int[] getAffectedIndexes() {
            return affectedIndexes;
        }

        @Override
        public String toString() {
            final Stream<String> stream = Arrays.stream(affectedIndexes).mapToObj(String::valueOf);
            return "(" + stream.collect(Collectors.joining(",")) + ")";
        }
    }

    public static class Buttons implements Iterable<Button> {

        private final Button[] buttons;

        public Buttons(final String definition) {
            this(Arrays.stream(definition.split(" ")).map(Button::new).toArray(Button[]::new));
        }

        public Buttons(final Button[] buttons) {
            this.buttons = buttons;
        }

        @Override
        public Iterator<Button> iterator() {
            return Arrays.stream(buttons).iterator();
        }

        public Stream<Button> stream() {
            return Arrays.stream(buttons);
        }

        public Button get(final int index) {
            return buttons[index];
        }

        public boolean press(final int index, final IndicatorLights lights, final Joltages joltages) {
            buttons[index].press(lights, joltages);
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

    public static class PartiallyRelevantButtons extends Buttons {

        private final Button[] irrelevantButtons;

        public PartiallyRelevantButtons(final Button[] buttons, final Button[] irrelevantButtons) {
            super(buttons);
            this.irrelevantButtons = irrelevantButtons;
        }

        public Buttons getIrrelevantButtons() {
            return new Buttons(irrelevantButtons);
        }
    }

    public static class Input {

        private static final Pattern LINE_PATTERN = Pattern
                .compile("\\[(.*?)\\] (\\(\\d+(?:,\\d+)*\\)(?: \\(\\d+(?:,\\d+)*\\))*) \\{(\\d+(?:,\\d+)*)\\}");

        private final IndicatorLights indicatorLights;
        private final Buttons buttons;
        private final Joltages joltages;

        public Input(final String input) {
            final Matcher matcher = LINE_PATTERN.matcher(input);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("line: " + input);
            }
            buttons = new Buttons(matcher.group(2));
            joltages = new Joltages(matcher.group(3));
            indicatorLights = new IndicatorLights(joltages);
        }

        public IndicatorLights getIndicatorLights() {
            return indicatorLights;
        }

        public Joltages getJoltages() {
            return joltages;
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
