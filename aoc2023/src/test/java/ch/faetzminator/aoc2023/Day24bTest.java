package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day24bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "19, 13, 30 @ -2,  1, -2\n" + "18, 19, 22 @ -1, -1, -2\n" + "20, 25, 34 @ -2, -2, -4\n"
                + "12, 31, 28 @ -1, -2, -1\n" + "20, 19, 15 @  1, -5, -3";

        final Day24b puzzle = new Day24b();
        for (final String line : toList(input)) {
            puzzle.parseHailstone(line);
        }
        Assertions.assertEquals(47L, puzzle.throwStone());
    }
}
