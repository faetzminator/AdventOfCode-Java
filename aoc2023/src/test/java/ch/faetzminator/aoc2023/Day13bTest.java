package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day13bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "#.##..##.\n" + "..#.##.#.\n" + "##......#\n" + "##......#\n" + "..#.##.#.\n"
                + "..##..##.\n" + "#.#.##.#.\n" + "\n" + "#...##..#\n" + "#....#..#\n" + "..##..###\n" + "#####.##.\n"
                + "#####.##.\n" + "..##..###\n" + "#....#..#";

        final Day13b puzzle = new Day13b();
        for (final String lines : input.split("\\n\\n")) {
            puzzle.parsePattern(toList(lines));
        }
        Assertions.assertEquals(400L, puzzle.getSummary());
    }
}