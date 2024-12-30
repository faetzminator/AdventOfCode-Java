package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day14Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "O....#....\n" + "O.OO#....#\n" + ".....##...\n" + "OO.#O....O\n" + ".O.....O#.\n"
                + "O.#..O.#.#\n" + "..O..#O..O\n" + ".......O..\n" + "#....###..\n" + "#OO..#....";

        final Day14 puzzle = new Day14();
        puzzle.parseLines(toList(input));
        puzzle.tiltNorth();
        Assertions.assertEquals(136L, puzzle.calculateLoadSum());
    }
}
