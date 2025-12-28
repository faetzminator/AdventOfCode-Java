package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

    public void parseLines(final List<String> input, final Timer timer) {
        total = input.size();
        input.parallelStream().forEach(line -> parseLine(line, timer));
    }

    public void parseLine(final String line) {
        parseLine(line, null);
    }

    private void parseLine(final String line, final Timer timer) {
        final Input input = new Input(line);
        final int s = play(input.getJoltages(), input.getButtons());
        if (timer != null) {
            System.out.println(
                    "Result " + (++processed) + "/" + total + ": " + s + " (" + timer.getElapsedFormatted() + ")");
        }
        solution += s;
    }

    private int play(final Joltages expectedState, final Buttons buttons) {
        final int[] pos = new int[expectedState.length()];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = i;
        }
        return play(expectedState, buttons.getButtons(), new Joltages(expectedState.length()), pos);
    }

    // 155, 145, 40, 45, ...
    private int play(final Joltages expectedState, final Button[] buttons, final Joltages state, final int[] pos) {
        final int[] expectedByButtons = new int[expectedState.length()];
        for (final Button button : buttons) {
            for (final int index : button.getAffectedIndexes()) {
                expectedByButtons[index]++;
            }
        }

        final int[] newPos = Arrays.stream(pos).boxed().filter(o -> expectedByButtons[o] > 0)
                .sorted(Comparator.comparingInt(o -> expectedByButtons[o])).mapToInt(v -> v).toArray();
        if (newPos.length == 0) {
            return INVALID_SOLUTION;
        }

        final Button[][] splitButtons = splitRelevantButtons(buttons, newPos[0]);
        if (splitButtons[0].length == 0) {
            throw new IllegalArgumentException();
        }

        Arrays.sort(splitButtons[0], Comparator.comparingInt(o -> o.getAffectedIndexes().length));

        return play(expectedState, splitButtons[0], splitButtons[1], state, newPos, splitButtons[0].length - 1);
    }

    private int play(final Joltages expectedState, final Button[] relevantButtons, final Button[] irrelevantButtons,
            final Joltages lastState, final int[] pos, final int lookingAt) {
        final Joltages state = lastState.copy();
        if (lookingAt == 0) {
            // last button, we have to increment until we hit expected count
            final int myTries = expectedState.get(pos[0]) - state.get(pos[0]);
            for (int i = 0; i < myTries; i++) {
                if (!increment(expectedState, state, relevantButtons[lookingAt])) {
                    return INVALID_SOLUTION;
                }
            }
            if (state.equals(expectedState)) {
                return myTries;
            }
            if (pos.length == 1) {
                return INVALID_SOLUTION;
            }
            final int[] newPos = new int[pos.length - 1];
            System.arraycopy(pos, 1, newPos, 0, newPos.length);
            final int sol = play(expectedState, irrelevantButtons, state, newPos);
            if (sol == INVALID_SOLUTION) {
                return sol;
            }
            return sol + myTries;
        }
        if (lookingAt < 0) {
            throw new IllegalArgumentException();
        }
        int min = play(expectedState, relevantButtons, irrelevantButtons, state, pos, lookingAt - 1);
        final int maxTries = expectedState.get(pos[0]) - state.get(pos[0]);
        for (int myTries = 1; myTries <= maxTries; myTries++) {
            if (!increment(expectedState, state, relevantButtons[lookingAt])) {
                return min;
            }
            final int sol = play(expectedState, relevantButtons, irrelevantButtons, state, pos, lookingAt - 1);
            if (sol < min) {
                min = Math.min(min, sol + myTries);
            }
        }
        return min;
    }

    private Button[][] splitRelevantButtons(final Button[] buttons, final int value) {
        int relevantSize = 0;
        int irrelevantSize = 0;
        final Button[] relevantButtons = new Button[buttons.length];
        final Button[] irrelevantButtons = new Button[buttons.length];
        for (final Button button : buttons) {
            if (contains(button.getAffectedIndexes(), value)) {
                relevantButtons[relevantSize++] = button;
            } else {
                irrelevantButtons[irrelevantSize++] = button;
            }
        }
        final Button[][] solution = new Button[][] { new Button[relevantSize], new Button[irrelevantSize] };
        System.arraycopy(relevantButtons, 0, solution[0], 0, relevantSize);
        System.arraycopy(irrelevantButtons, 0, solution[1], 0, irrelevantSize);
        return solution;
    }

    private boolean contains(final int[] array, final int value) {
        for (final int val : array) {
            if (val == value) {
                return true;
            }
        }
        return false;
    }

    private boolean increment(final Joltages expectedState, final Joltages state, final Button button) {
        for (final int position : button.getAffectedIndexes()) {
            if (expectedState.get(position) == state.get(position)) {
                return false;
            }
            if (expectedState.get(position) < state.get(position)) {
                throw new IllegalArgumentException();
            }
            state.increment(position);
        }
        return true;
    }

    public long getSolution() {
        return solution;
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
                joltages.increment(index);
            }
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

        public Button[] getButtons() {
            return buttons;
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

    public static class Joltages {

        private final int[] joltages;

        public Joltages(final int length) {
            joltages = new int[length];
        }

        public Joltages(final String definition) {
            this(Arrays.stream(definition.split(",")).mapToInt(Integer::parseInt).toArray());
        }

        private Joltages(final int[] joltages) {
            this.joltages = joltages;
        }

        public int get(final int index) {
            return joltages[index];
        }

        public void increment(final int index) {
            joltages[index]++;
        }

        public Joltages copy() {
            return new Joltages(Arrays.copyOf(joltages, joltages.length));
        }

        public int length() {
            return joltages.length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(joltages));
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
            final Joltages other = (Joltages) obj;
            return Arrays.equals(joltages, other.joltages);
        }

        @Override
        public String toString() {
            final Stream<String> stream = Arrays.stream(joltages).mapToObj(String::valueOf);
            return "{" + stream.collect(Collectors.joining(",")) + "}";
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

        public Buttons getButtons() {
            return buttons;
        }

        public Joltages getJoltages() {
            return joltages;
        }

        @Override
        public String toString() {
            return indicatorLights + " " + buttons + " " + joltages;
        }
    }
}
