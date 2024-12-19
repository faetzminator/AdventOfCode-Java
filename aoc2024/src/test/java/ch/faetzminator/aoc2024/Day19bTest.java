package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day19bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "r, wr, b, g, bwu, rb, gb, br\n" + "\n" + "brwrr\n" + "bggr\n" + "gbbr\n" + "rrbgbr\n"
                + "ubwu\n" + "bwurrg\n" + "brgr\n" + "bbrgwb";

        final String[] inputs = input.split("\\n\\n");

        final Day19b puzzle = new Day19b();
        puzzle.parsePatterns(inputs[0]);
        for (final String line : toList(inputs[1])) {
            puzzle.parseTowel(line);
        }
        Assertions.assertEquals(16L, puzzle.getPossibleTowelSum());
    }
}
