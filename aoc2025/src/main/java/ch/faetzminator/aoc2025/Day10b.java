package ch.faetzminator.aoc2025;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day10b {

    public static void main(final String[] args) {
        final Day10b puzzle = new Day10b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        /*
         * for (final String line : lines) { puzzle.parseLine(line, timer); }
         */
        puzzle.parseLines(lines, timer);
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("(.)(\\d+)");

    private long solution;
    private int processed = 0;

    public void parseLines(final List<String> input, final Timer timer) {
        input.parallelStream().forEach(line -> parseLine(line, timer));
    }

    public void parseLine(final String input) {
        parseLine(input, null);
    }

    private void parseLine(final String input, final Timer timer) {
        final String[] inputs = input.split(" ");
        final int[][] buttons = new int[inputs.length - 2][];
        for (int i = 1; i < inputs.length - 1; i++) {
            final String[] str = inputs[i].substring(1, inputs[i].length() - 1).split(",");
            buttons[i - 1] = new int[str.length];
            for (int j = 0; j < str.length; j++) {
                buttons[i - 1][j] = Integer.parseInt(str[j]);
            }
        }

        final String[] expectedStr = inputs[inputs.length - 1].substring(1, inputs[inputs.length - 1].length() - 1)
                .split(",");
        final int[] expected = new int[expectedStr.length];
        for (int i = 0; i < expectedStr.length; i++) {
            expected[i] = Integer.parseInt(expectedStr[i]);
        }

        final int s = play(expected, buttons);
        if (timer != null) {
            System.out.println("Result " + (++processed) + ": " + s + " (" + timer.getElapsedFormatted() + ")");
        }
        solution += s;
    }

    private int play(final int[] expected, final int[][] buttons) {
        final int[] pos = new int[expected.length];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = i;
        }
        return play(expected, buttons, new int[expected.length], pos);
    }

    private String toString(final int[][] buttons) {
        return Arrays.stream(buttons).map(Arrays::toString).collect(Collectors.joining());
    }

    // 155, 145, 40, 45, ...
    private int play(final int[] expected, final int[][] buttons, final int[] state, final int[] pos) {
        final int[] expectedByButtons = new int[expected.length];
        for (final int[] button : buttons) {
            for (final int index : button) {
                expectedByButtons[index]++;
            }
        }

        final int[] newPos = Arrays.stream(pos).boxed().filter(o -> expectedByButtons[o] > 0)
                .sorted(Comparator.comparingInt(o -> expectedByButtons[o])).mapToInt(v -> v).toArray();
        if (newPos.length == 0) {
            return Integer.MAX_VALUE;
        }

        final int[][][] splitButtons = splitRelevantButtons(buttons, newPos[0]);
        if (splitButtons[0].length == 0) {
            throw new IllegalArgumentException();
        }

        Arrays.sort(splitButtons[0], Comparator.comparingInt(o -> o.length));

        return play(expected, splitButtons[0], splitButtons[1], state, newPos, splitButtons[0].length - 1);
    }

    private int play(final int[] expected, final int[][] relevantButtons, final int[][] irrelevantButtons,
            final int[] ___state, final int[] pos, final int lookingAt) {
        final int[] newState = Arrays.copyOf(___state, ___state.length);
        if (lookingAt == 0) {
            // last button, we have to increment until we hit expected count
            final int myTries = expected[pos[0]] - newState[pos[0]];
            for (int i = 0; i < myTries; i++) {
                if (!increment(expected, newState, relevantButtons[lookingAt])) {
                    return Integer.MAX_VALUE;
                }
            }
            if (Arrays.equals(expected, newState)) {
                return myTries;
            }
            if (pos.length == 1) {
                return Integer.MAX_VALUE;
            }
            final int[] newPos = new int[pos.length - 1];
            System.arraycopy(pos, 1, newPos, 0, newPos.length);
            final int sol = play(expected, irrelevantButtons, newState, newPos);
            if (sol == Integer.MAX_VALUE) {
                return sol;
            }
            return sol + myTries;
        }
        if (lookingAt < 0) {
            throw new IllegalArgumentException();
        }
        int min = play(expected, relevantButtons, irrelevantButtons, newState, pos, lookingAt - 1);
        final int maxTries = expected[pos[0]] - newState[pos[0]];
        for (int myTries = 1; myTries <= maxTries; myTries++) {
            if (!increment(expected, newState, relevantButtons[lookingAt])) {
                return min;
            }
            final int sol = play(expected, relevantButtons, irrelevantButtons, newState, pos, lookingAt - 1);
            if (sol < min) {
                min = Math.min(min, sol + myTries);
            }
        }
        return min;
    }

    private int[][][] splitRelevantButtons(final int[][] buttons, final int value) {
        int relevantSize = 0;
        int irrelevantSize = 0;
        final int[][] relevantButtons = new int[buttons.length][];
        final int[][] irrelevantButtons = new int[buttons.length][];
        for (final int[] button : buttons) {
            if (contains(button, value)) {
                relevantButtons[relevantSize++] = button;
            } else {
                irrelevantButtons[irrelevantSize++] = button;
            }
        }
        final int[][][] solution = new int[][][] { new int[relevantSize][], new int[irrelevantSize][] };
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

    private boolean increment(final int[] expected, final int[] state, final int[] positions) {
        for (final int position : positions) {
            if (expected[position] == state[position]) {
                return false;
            }
            if (expected[position] < state[position]) {
                throw new IllegalArgumentException();
            }
            state[position]++;
        }
        return true;
    }

    public long getSolution() {
        return solution;
    }
}
