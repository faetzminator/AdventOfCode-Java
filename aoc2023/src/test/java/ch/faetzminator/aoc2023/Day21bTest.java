package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day21bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "...........\n" + ".....###.#.\n" + ".###.##..#.\n" + "..#.#...#..\n" + "....#.#....\n"
                + ".##..S####.\n" + ".##..#...#.\n" + ".......##..\n" + ".##.#.####.\n" + ".##..##.##.\n"
                + "...........";

        final Day21b puzzle = new Day21b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(7318L, puzzle.countReachableGardenPlots(104));
    }
}
