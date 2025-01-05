package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day17Test extends PuzzleTest {

    @Test
    public void example() {
        final String[] inputs = toBlocks(
                "Register A: 729\n" + "Register B: 0\n" + "Register C: 0\n" + "\n" + "Program: 0,1,5,4,3,0");

        final Day17 puzzle = new Day17();
        for (final String line : toList(inputs[0])) {
            puzzle.parseRegister(line);
        }
        puzzle.parseProgram(inputs[1]);
        Assertions.assertEquals("4,6,3,5,6,3,5,2,1,0", puzzle.run());
    }
}
