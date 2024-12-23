package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day06bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "Time:      7  15   30\n" + "Distance:  9  40  200";

        final Day06b puzzle = new Day06b();
        final String[] lines = input.split("\\n");
        puzzle.parseInput(lines[0], lines[1]);
        Assertions.assertEquals(71503L, puzzle.calculate());
    }
}
