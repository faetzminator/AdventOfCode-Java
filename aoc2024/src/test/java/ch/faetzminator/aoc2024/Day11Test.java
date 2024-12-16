package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day11Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "0 1 10 99 999";

        final Day11 puzzle = new Day11();
        puzzle.parseLine(input);
        Assertions.assertEquals(7L, puzzle.blinkMultipleTimes(1));
    }

    @Test
    public void otherExample() {
        final String input = "125 17";

        final Day11 puzzle = new Day11();
        puzzle.parseLine(input);
        Assertions.assertEquals(55312L, puzzle.blinkMultipleTimes());
    }
}
