package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day01Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "1abc2\n" + "pqr3stu8vwx\n" + "a1b2c3d4e5f\n" + "treb7uchet";

        final Day01 puzzle = new Day01();
        for (final String line : toList(input)) {
            puzzle.addLine(line);
        }
        Assertions.assertEquals(142L, puzzle.getCalibrationSum());
    }
}
