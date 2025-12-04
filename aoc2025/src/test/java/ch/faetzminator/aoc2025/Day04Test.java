package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day04Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "..@@.@@@@.\n" + "@@@.@.@.@@\n" + "@@@@@.@.@@\n" + "@.@@@@..@.\n" + "@@.@@@@.@@\n"
                + ".@@@@@@@.@\n" + ".@.@.@.@@@\n" + "@.@@@.@@@@\n" + ".@@@@@@@@.\n" + "@.@.@@@.@.";

        final Day04 puzzle = new Day04();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(13L, puzzle.getSolution());
    }
}
