package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day03Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "987654321111111\n" + "811111111111119\n" + "234234234234278\n" + "818181911112111";

        final Day03 puzzle = new Day03();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(357L, puzzle.getSolution());
    }
}
