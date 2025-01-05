package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day19Test extends PuzzleTest {

    @Test
    public void example() {
        final String[] inputs = toBlocks("r, wr, b, g, bwu, rb, gb, br\n" + "\n" + "brwrr\n" + "bggr\n" + "gbbr\n"
                + "rrbgbr\n" + "ubwu\n" + "bwurrg\n" + "brgr\n" + "bbrgwb");

        final Day19 puzzle = new Day19();
        puzzle.parsePatterns(inputs[0]);
        for (final String line : toList(inputs[1])) {
            puzzle.parseTowel(line);
        }
        Assertions.assertEquals(6L, puzzle.getPossibleTowelSum());
    }
}
