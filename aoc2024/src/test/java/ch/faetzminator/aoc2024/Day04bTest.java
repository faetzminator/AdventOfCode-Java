package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day04bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "MMMSXXMASM\n" + "MSAMXMSMSA\n" + "AMXSXMAAMM\n" + "MSAMASMSMX\n" + "XMASAMXAMM\n"
                + "XXAMMXXAMA\n" + "SMSMSASXSS\n" + "SAXAMASAAA\n" + "MAMMMXMMMM\n" + "MXMXAXMASX";

        final Day04b puzzle = new Day04b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(9L, puzzle.getXmasSum());
    }
}
