package ch.faetzminator.aoc2024;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.Timer;

public class Day01 {

    public static void main(final String[] args) {
        final Day01 puzzle = new Day01();
        final Timer timer = PuzzleUtil.start();
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    public long getSolution() {
        return 42;
    }
}
