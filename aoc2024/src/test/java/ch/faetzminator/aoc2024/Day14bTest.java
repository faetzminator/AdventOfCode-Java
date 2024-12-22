package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day14bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "p=72,34 v=9,-12\n" + "p=74,24 v=8,-79\n" + "p=43,28 v=-37,92\n" + "p=69,34 v=25,-12\n"
                + "p=30,99 v=99,-40\n" + "p=93,34 v=39,-12\n" + "p=97,100 v=88,-23\n" + "p=38,58 v=99,87\n"
                + "p=55,41 v=-72,-99\n" + "p=35,25 v=-53,41\n" + "p=1,84 v=55,14\n" + "p=8,46 v=36,89\n"
                + "p=44,44 v=-53,55";

        final Day14b puzzle = new Day14b();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(6L, puzzle.getXmasTreeTime(false));
    }
}
