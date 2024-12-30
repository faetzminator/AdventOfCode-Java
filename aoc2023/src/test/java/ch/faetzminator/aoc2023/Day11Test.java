package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day11Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "...#......\n" + ".......#..\n" + "#.........\n" + "..........\n" + "......#...\n"
                + ".#........\n" + ".........#\n" + "..........\n" + ".......#..\n" + "#...#.....";

        final Day11 puzzle = new Day11();
        puzzle.parseMap(toList(input));
        puzzle.calculateDistance();
        Assertions.assertEquals(374L, puzzle.getDistanceSum());
    }
}
