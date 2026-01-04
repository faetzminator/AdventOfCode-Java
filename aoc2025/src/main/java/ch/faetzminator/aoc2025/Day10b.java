package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day10b {

    public static void main(final String[] args) {
        final Day10b puzzle = new Day10b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines, timer);
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final int INVALID_SOLUTION = Integer.MAX_VALUE;

    private int total;
    private int processed;

    private long solution;

    public void parseLines(final List<String> lines, final Timer timer) {
        total = lines.size();
        lines.parallelStream().forEach(line -> parseLine(line, timer));
    }

    public void parseLine(final String line) {
        parseLine(line, null);
    }

    private void parseLine(final String line, final Timer timer) {
        final Input input = new Input(line);
        final int s = play(input.getJoltages(), input.getButtons());
        if (timer != null) {
            System.out.println(
                    "Result " + (++processed) + "/" + total + ": " + s + " (" + timer.getElapsedFormatted() + ") ");
        }
        solution += s;
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

    // 155, 145, 40, 45, ...
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
            if (sol == INVALID_SOLUTION) {
                return sol;
            }
            return sol + pressesNeeded;
        }
        // for all other buttons, we do recursion for 0 to maxPresses, proceeding with button n-1
        final int maxPresses = buttons.get(lookingAtButton).maxPresses(lastState);
        if (lastState.getDiff(relevantJoltageIndexes[0]) < maxPresses) {
            return INVALID_SOLUTION;
        }
        final Joltages state = lastState.copy();
        int min = play(state, buttons, relevantJoltageIndexes, lookingAtButton - 1);
        for (int presses = 1; presses <= maxPresses; presses++) {
            buttons.get(lookingAtButton).press(state);
            final int sol = play(state, buttons, relevantJoltageIndexes, lookingAtButton - 1);
            if (sol < min) {
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
        return solution;
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

        public void press(final Joltages joltages) {
            for (final int index : affectedIndexes) {
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

        private final String indicatorLights;
        private final Buttons buttons;
        private final Joltages joltages;

        public Input(final String input) {
            final Matcher matcher = LINE_PATTERN.matcher(input);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("line: " + input);
            }
            indicatorLights = "[" + matcher.group(1) + "]";
            buttons = new Buttons(matcher.group(2));
            joltages = new Joltages(matcher.group(3));
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
