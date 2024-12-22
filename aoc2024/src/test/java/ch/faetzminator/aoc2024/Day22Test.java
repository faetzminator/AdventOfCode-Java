package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day22Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "1\n" + "10\n" + "100\n" + "2024";

        final Day22 puzzle = new Day22();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(37327623L, puzzle.getSecretNumberSum());
    }
}
