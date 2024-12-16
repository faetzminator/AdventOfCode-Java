package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day01bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "3   4\n" + "4   3\n" + "2   5\n" + "1   3\n" + "3   9\n" + "3   3";

        final Day01b puzzle = new Day01b();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(31L, puzzle.getSolution());
    }
}
