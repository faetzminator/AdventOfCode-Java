package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day02Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "7 6 4 2 1\n" + "1 2 7 8 9\n" + "9 7 6 2 1\n" + "1 3 2 4 5\n" + "8 6 4 4 1\n"
                + "1 3 6 7 9";

        final Day02 puzzle = new Day02();
        for (final String line : toList(input)) {
            puzzle.parseReport(line);
        }
        Assertions.assertEquals(2L, puzzle.getValidReportSum());
    }
}
