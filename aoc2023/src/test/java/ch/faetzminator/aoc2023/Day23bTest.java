package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day23bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "#.#####################\n" + "#.......#########...###\n" + "#######.#########.#.###\n"
                + "###.....#.>.>.###.#.###\n" + "###v#####.#v#.###.#.###\n" + "###.>...#.#.#.....#...#\n"
                + "###v###.#.#.#########.#\n" + "###...#.#.#.......#...#\n" + "#####.#.#.#######.#.###\n"
                + "#.....#.#.#.......#...#\n" + "#.#####.#.#.#########v#\n" + "#.#...#...#...###...>.#\n"
                + "#.#.#v#######v###.###v#\n" + "#...#.>.#...>.>.#.###.#\n" + "#####v#.#.###v#.#.###.#\n"
                + "#.....#...#...#.#.#...#\n" + "#.#########.###.#.#.###\n" + "#...###...#...#...#.###\n"
                + "###.###.#.###v#####v###\n" + "#...#...#.#.>.>.#.>.###\n" + "#.###.###.#.###.#.#v###\n"
                + "#.....###...###...#...#\n" + "#####################.#";

        final Day23b puzzle = new Day23b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(154L, puzzle.findLongestPath());
    }
}
