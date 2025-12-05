package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day05Test extends PuzzleTest {

    @Test
    public void example() {
        final String[] inputs = toBlocks(
                "3-5\n" + "10-14\n" + "16-20\n" + "12-18\n" + "\n" + "1\n" + "5\n" + "8\n" + "11\n" + "17\n" + "32");

        final Day05 puzzle = new Day05();
        for (final String line : toList(inputs[0])) {
            puzzle.parseFreshIngredientRangeLine(line);
        }
        for (final String line : toList(inputs[1])) {
            puzzle.parseAvailableIngredientLine(line);
        }
        Assertions.assertEquals(3L, puzzle.getSolution());
    }
}
