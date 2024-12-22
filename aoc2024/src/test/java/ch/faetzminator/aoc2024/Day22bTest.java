package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day22bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "1\n" + "2\n" + "3\n" + "2024";

        final Day22b puzzle = new Day22b();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(23L, puzzle.getSecretNumberSum());
    }
}
