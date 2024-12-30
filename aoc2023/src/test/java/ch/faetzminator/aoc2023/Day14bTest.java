package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day14bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "O....#....\n" + "O.OO#....#\n" + ".....##...\n" + "OO.#O....O\n" + ".O.....O#.\n"
                + "O.#..O.#.#\n" + "..O..#O..O\n" + ".......O..\n" + "#....###..\n" + "#OO..#....";

        final Day14b puzzle = new Day14b();
        puzzle.parseLines(toList(input));
        puzzle.tiltManyTimes();
        Assertions.assertEquals(64L, puzzle.calculateLoadSum());
    }
}
