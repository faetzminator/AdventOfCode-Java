package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day01bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "two1nine\n" + "eightwothree\n" + "abcone2threexyz\n" + "xtwone3four\n"
                + "4nineeightseven2\n" + "zoneight234\n" + "7pqrstsixteen";

        final Day01b puzzle = new Day01b();
        for (final String line : toList(input)) {
            puzzle.addLine(line);
        }
        Assertions.assertEquals(281L, puzzle.getCalibrationSum());
    }
}
