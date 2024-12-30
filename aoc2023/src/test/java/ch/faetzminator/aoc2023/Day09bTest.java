package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day09bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "0 3 6 9 12 15\n" + "1 3 6 10 15 21\n" + "10 13 16 21 30 45";

        final Day09b puzzle = new Day09b();
        for (final String line : toList(input)) {
            puzzle.addSequence(line);
        }
        Assertions.assertEquals(2L, puzzle.getExtrapolationSum());
    }
}
