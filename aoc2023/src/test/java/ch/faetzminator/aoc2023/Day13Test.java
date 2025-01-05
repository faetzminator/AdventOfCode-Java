package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day13Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "#.##..##.\n" + "..#.##.#.\n" + "##......#\n" + "##......#\n" + "..#.##.#.\n"
                + "..##..##.\n" + "#.#.##.#.\n" + "\n" + "#...##..#\n" + "#....#..#\n" + "..##..###\n" + "#####.##.\n"
                + "#####.##.\n" + "..##..###\n" + "#....#..#";

        final Day13 puzzle = new Day13();
        for (final String lines : toBlocks(input)) {
            puzzle.parsePattern(toList(lines));
        }
        Assertions.assertEquals(405L, puzzle.getSummary());
    }
}
