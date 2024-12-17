package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day14bTest extends PuzzleTest {

    @Test
    @Disabled
    public void example() {
        final String input = "p=0,4 v=3,-3\n" + "p=6,3 v=-1,-3\n" + "p=10,3 v=-1,2\n" + "p=2,0 v=2,-1\n"
                + "p=0,0 v=1,3\n" + "p=3,0 v=-2,-2\n" + "p=7,6 v=-1,-3\n" + "p=3,0 v=-1,-2\n" + "p=9,3 v=2,3\n"
                + "p=7,3 v=-1,2\n" + "p=2,4 v=2,-3\n" + "p=9,5 v=-3,-3";

        final Day14b puzzle = new Day14b();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        // this is unfortunately not testable
        Assertions.assertEquals(-1L, puzzle.getXmasTreeTime(false));
    }
}
