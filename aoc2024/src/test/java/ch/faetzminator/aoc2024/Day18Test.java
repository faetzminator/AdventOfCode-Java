package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day18Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "5,4\n" + "4,2\n" + "4,5\n" + "3,0\n" + "2,1\n" + "6,3\n" + "2,4\n" + "1,5\n" + "0,6\n"
                + "3,3\n" + "2,6\n" + "5,1\n" + "1,2\n" + "5,5\n" + "2,5\n" + "6,5\n" + "1,4\n" + "0,4\n" + "6,4\n"
                + "1,1\n" + "6,1\n" + "1,0\n" + "0,5\n" + "1,6\n" + "2,0";

        final Day18 puzzle = new Day18();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(22L, puzzle.getMinimumNumberOfSteps());
    }
}
