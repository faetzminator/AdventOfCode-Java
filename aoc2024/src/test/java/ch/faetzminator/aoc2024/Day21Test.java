package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day21Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "029A\n" + "980A\n" + "179A\n" + "456A\n" + "379A";

        final Day21 puzzle = new Day21();
        puzzle.initialize();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(126384L, puzzle.getComplexitySum());
    }
}
