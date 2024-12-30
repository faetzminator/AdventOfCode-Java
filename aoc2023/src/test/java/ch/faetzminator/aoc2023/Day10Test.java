package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day10Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "-L|F7\n" + "7S-7|\n" + "L|7||\n" + "-L-J|\n" + "L|-JF";

        final Day10 puzzle = new Day10();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(4L, puzzle.calculateLoopSteps());
    }

    @Test
    public void otherExample() {
        final String input = "7-F7-\n" + ".FJ|7\n" + "SJLL7\n" + "|F--J\n" + "LJ.LJ";

        final Day10 puzzle = new Day10();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(8L, puzzle.calculateLoopSteps());
    }
}
