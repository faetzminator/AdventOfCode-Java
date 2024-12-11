package ch.faetzminator.aoc2024;

import java.util.ArrayList;
import java.util.List;

import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day11 {

    public static void main(final String[] args) {
        final Day11 puzzle = new Day11();
        final String line = ScannerUtil.readNonBlankLine();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLine(line);
        final long solution = puzzle.blinkMultipleTimes();
        PuzzleUtil.end(solution, timer);
    }

    private static final int ITERATIONS = 25;

    private List<Long> stones;

    public void parseLine(final String input) {
        stones = new ArrayList<>();
        for (final String number : input.split(" ")) {
            stones.add(Long.valueOf(number));
        }
    }

    private long blinkMultipleTimes() {
        for (int i = 0; i < ITERATIONS; i++) {
            blink();
        }
        return stones.size();
    }

    private void blink() {
        final List<Long> newStones = new ArrayList<>();
        for (final long stone : stones) {
            final int digits = MathUtil.countDigits(stone);
            if (stone == 0L) {
                newStones.add(1L);
            } else if (digits % 2 == 0) {
                final long divisor = MathUtil.pow10(digits / 2);
                newStones.add(stone / divisor);
                newStones.add(stone % divisor);
            } else {
                newStones.add(stone * 2024);
            }
        }
        stones = newStones;
    }
}
