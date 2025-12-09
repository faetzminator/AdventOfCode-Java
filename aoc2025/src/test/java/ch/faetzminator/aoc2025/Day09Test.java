package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day09Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "7,1\n" + "11,1\n" + "11,7\n" + "9,7\n" + "9,5\n" + "2,5\n" + "2,3\n" + "7,3";

        final Day09 puzzle = new Day09();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(50L, puzzle.getSolution());
    }
}
