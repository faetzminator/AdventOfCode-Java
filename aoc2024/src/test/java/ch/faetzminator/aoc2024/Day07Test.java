package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day07Test extends PuzzleTest {

    @Test
    public void example() {
        final String lines = "190: 10 19\n" + "3267: 81 40 27\n" + "83: 17 5\n" + "156: 15 6\n" + "7290: 6 8 6 15\n"
                + "161011: 16 10 13\n" + "192: 17 8 14\n" + "21037: 9 7 18 13\n" + "292: 11 6 16 20";

        final Day07 puzzle = new Day07();
        for (final String line : toList(lines)) {
            puzzle.parseEquation(line);
        }
        Assertions.assertEquals(3749L, puzzle.getEquationSum());
    }
}
