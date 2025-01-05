package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day22bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "1,0,1~1,2,1\n" + "0,0,2~2,0,2\n" + "0,2,3~2,2,3\n" + "0,0,4~0,2,4\n" + "2,0,5~2,2,5\n"
                + "0,1,6~2,1,6\n" + "1,1,8~1,1,9";

        final Day22b puzzle = new Day22b();
        for (final String line : toList(input)) {
            puzzle.parseBrick(line);
        }
        Assertions.assertEquals(7L, puzzle.calculateDisintegrableBlocks());
    }
}
