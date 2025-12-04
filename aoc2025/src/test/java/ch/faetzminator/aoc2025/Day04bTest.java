package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day04bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "..@@.@@@@.\n" + "@@@.@.@.@@\n" + "@@@@@.@.@@\n" + "@.@@@@..@.\n" + "@@.@@@@.@@\n"
                + ".@@@@@@@.@\n" + ".@.@.@.@@@\n" + "@.@@@.@@@@\n" + ".@@@@@@@@.\n" + "@.@.@@@.@.";

        final Day04b puzzle = new Day04b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(43L, puzzle.getSolution());
    }
}
