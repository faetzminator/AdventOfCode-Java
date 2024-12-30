package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day11bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "...#......\n" + ".......#..\n" + "#.........\n" + "..........\n" + "......#...\n"
                + ".#........\n" + ".........#\n" + "..........\n" + ".......#..\n" + "#...#.....";

        final Day11b puzzle = new Day11b();
        puzzle.parseMap(toList(input));
        puzzle.calculateDistance();
        Assertions.assertEquals(82000210L, puzzle.getDistanceSum());
    }
}
