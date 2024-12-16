package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day11bTest extends PuzzleTest {

    @Test
    public void otherExample() {
        final String input = "125 17";

        final Day11b puzzle = new Day11b();
        puzzle.parseLine(input);
        Assertions.assertEquals(65601038650482L, puzzle.blinkMultipleTimes());
    }
}
