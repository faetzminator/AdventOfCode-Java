package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day17bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "Register A: 2024\n" + "Register B: 0\n" + "Register C: 0\n" + "\n"
                + "Program: 0,3,5,4,3,0";
        final String inputs[] = input.split("\\n\\n");

        final Day17b puzzle = new Day17b();
        for (final String line : toList(inputs[0])) {
            puzzle.parseRegister(line);
        }
        puzzle.parseProgram(inputs[1]);
        Assertions.assertEquals(117440L, puzzle.runManyTimes());
    }
}
