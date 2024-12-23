package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day08bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "LR\n" + "\n" + "11A = (11B, XXX)\n" + "11B = (XXX, 11Z)\n" + "11Z = (11B, XXX)\n"
                + "22A = (22B, XXX)\n" + "22B = (22C, 22C)\n" + "22C = (22Z, 22Z)\n" + "22Z = (22B, 22B)\n"
                + "XXX = (XXX, XXX)";
        final String[] inputs = input.split("\\n\\n");

        final Day08b puzzle = new Day08b();
        puzzle.parseInstructions(inputs[0]);
        for (final String line : toList(inputs[1])) {
            puzzle.addNode(line);
        }
        Assertions.assertEquals(6L, puzzle.calculateSteps());
    }
}
