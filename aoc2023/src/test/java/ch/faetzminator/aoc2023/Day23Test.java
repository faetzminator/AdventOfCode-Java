package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day23Test extends PuzzleTest {

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

        final Day23 puzzle = new Day23();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(94L, puzzle.findLongestPath());
    }
}
