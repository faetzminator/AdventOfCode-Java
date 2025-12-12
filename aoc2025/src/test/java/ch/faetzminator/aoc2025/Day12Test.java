package ch.faetzminator.aoc2025;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

/**
 * It's a serious test - LOL.
 *
 * @author faetzminator
 */
public class Day12Test extends PuzzleTest {

    @Test
    public void example() {
        final String[] inputs = toBlocks("0:\n" + "###\n" + "##.\n" + "##.\n" + "\n" + "1:\n" + "###\n" + "##.\n"
                + ".##\n" + "\n"
                + "2:\n" + ".##\n" + "###\n" + "##.\n" + "\n" + "3:\n" + "##.\n" + "###\n" + "##.\n" + "\n" + "4:\n"
                + "###\n" + "#..\n" + "###\n" + "\n" + "5:\n" + "###\n" + ".#.\n" + "###\n" + "\n"
                + "4x4: 0 0 0 0 2 0\n" + "12x5: 1 0 1 0 2 2\n" + "12x5: 1 0 1 0 3 2");

        final Day12 puzzle = new Day12();
        for (int i = 0; i < inputs.length - 1; i++) {
            puzzle.parsePresentLines(new ArrayList<>(toList(inputs[i])));
        }
        for (final String line : toList(inputs[inputs.length - 1])) {
            puzzle.parseRegionLine(line);
        }
        Assertions.assertEquals(2L, puzzle.getSolution());
    }
}
