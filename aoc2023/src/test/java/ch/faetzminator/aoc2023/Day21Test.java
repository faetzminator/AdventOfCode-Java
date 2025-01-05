package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day21Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "...........\n" + ".....###.#.\n" + ".###.##..#.\n" + "..#.#...#..\n" + "....#.#....\n"
                + ".##..S####.\n" + ".##..#...#.\n" + ".......##..\n" + ".##.#.####.\n" + ".##..##.##.\n"
                + "...........";

        final Day21 puzzle = new Day21();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(16L, puzzle.countReachableGardenPlots(6));
    }
}
