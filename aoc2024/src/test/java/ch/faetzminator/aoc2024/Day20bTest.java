package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day20bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "###############\n" + "#...#...#.....#\n" + "#.#.#.#.#.###.#\n" + "#S#...#.#.#...#\n"
                + "#######.#.#.###\n" + "#######.#.#...#\n" + "#######.#.###.#\n" + "###..E#...#...#\n"
                + "###.#######.###\n" + "#...###...#...#\n" + "#.#####.#.###.#\n" + "#.#...#.#.#...#\n"
                + "#.#.#.#.#.#.###\n" + "#...#...#...###\n" + "###############";

        final Day20b puzzle = new Day20b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(285L, puzzle.getNumberOfCheats(50));
        Assertions.assertEquals(129L, puzzle.getNumberOfCheats(60));
    }
}
