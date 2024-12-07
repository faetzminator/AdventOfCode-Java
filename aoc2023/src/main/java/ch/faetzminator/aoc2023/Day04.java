package ch.faetzminator.aoc2023;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day04 {

    public static void main(final String[] args) {
        final Day04 puzzle = new Day04();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.addScratchcard(line);
        }
        final long solution = puzzle.getWinningSum();
        PuzzleUtil.end(solution, timer);
    }

    private long winningSum;

    private static final Pattern LINE_PATTERN = Pattern.compile("Card +(\\d+): +(.*?) \\| +(.*?)");

    public void addScratchcard(final String str) {
        final Matcher matcher = LINE_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + str);
        }

        final Set<String> winningNumbers = new LinkedHashSet<>(Arrays.asList(matcher.group(2).split(" +")));
        final Set<String> numbers = new HashSet<>(Arrays.asList(matcher.group(3).split(" +")));

        winningNumbers.retainAll(numbers);
        final int hits = winningNumbers.size();
        if (hits > 0) {
            winningSum += MathUtil.pow2(hits - 1);
        }
    }

    public long getWinningSum() {
        return winningSum;
    }
}
