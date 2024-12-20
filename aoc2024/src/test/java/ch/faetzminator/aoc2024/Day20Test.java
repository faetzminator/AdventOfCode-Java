package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day20Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "###############\n" + "#...#...#.....#\n" + "#.#.#.#.#.###.#\n" + "#S#...#.#.#...#\n"
                + "#######.#.#.###\n" + "#######.#.#...#\n" + "#######.#.###.#\n" + "###..E#...#...#\n"
                + "###.#######.###\n" + "#...###...#...#\n" + "#.#####.#.###.#\n" + "#.#...#.#.#...#\n"
                + "#.#.#.#.#.#.###\n" + "#...#...#...###\n" + "###############";

        final Day20 puzzle = new Day20();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(5L, puzzle.getNumberOfCheats(20));
        Assertions.assertEquals(4L, puzzle.getNumberOfCheats(22));
    }
}
