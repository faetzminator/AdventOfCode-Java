package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day04Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "MMMSXXMASM\n" + "MSAMXMSMSA\n" + "AMXSXMAAMM\n" + "MSAMASMSMX\n" + "XMASAMXAMM\n"
                + "XXAMMXXAMA\n" + "SMSMSASXSS\n" + "SAXAMASAAA\n" + "MAMMMXMMMM\n" + "MXMXAXMASX";

        final Day04 puzzle = new Day04();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(18L, puzzle.getXmasSum());
    }
}
