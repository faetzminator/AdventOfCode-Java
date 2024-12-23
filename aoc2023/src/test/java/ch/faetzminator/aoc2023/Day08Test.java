package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day08Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "RL\n" + "\n" + "AAA = (BBB, CCC)\n" + "BBB = (DDD, EEE)\n" + "CCC = (ZZZ, GGG)\n"
                + "DDD = (DDD, DDD)\n" + "EEE = (EEE, EEE)\n" + "GGG = (GGG, GGG)\n" + "ZZZ = (ZZZ, ZZZ)";
        final String[] inputs = input.split("\\n\\n");

        final Day08 puzzle = new Day08();
        puzzle.parseInstructions(inputs[0]);
        for (final String line : toList(inputs[1])) {
            puzzle.addNode(line);
        }
        Assertions.assertEquals(2L, puzzle.calculateSteps());
    }

    @Test
    public void otherExample() {
        final String input = "LLR\n" + "\n" + "AAA = (BBB, BBB)\n" + "BBB = (AAA, ZZZ)\n" + "ZZZ = (ZZZ, ZZZ)";
        final String[] inputs = input.split("\\n\\n");

        final Day08 puzzle = new Day08();
        puzzle.parseInstructions(inputs[0]);
        for (final String line : toList(inputs[1])) {
            puzzle.addNode(line);
        }
        Assertions.assertEquals(6L, puzzle.calculateSteps());
    }
}
