package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day01Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "L68\n" + "L30\n" + "R48\n" + "L5\n" + "R60\n" + "L55\n" + "L1\n" + "L99\n" + "R14\n"
                + "L82";

        final Day01 puzzle = new Day01();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(3L, puzzle.getSolution());
    }
}
