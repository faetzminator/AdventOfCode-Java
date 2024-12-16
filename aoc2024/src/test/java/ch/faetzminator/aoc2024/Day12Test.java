package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day12Test extends PuzzleTest {

    @Test
    public void example() {
        final String map = "AAAA\n" + "BBCD\n" + "BBCC\n" + "EEEC";

        final Day12 puzzle = new Day12();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(140L, puzzle.getPriceSum());
    }

    @Test
    public void secondExample() {
        final String map = "OOOOO\n" + "OXOXO\n" + "OOOOO\n" + "OXOXO\n" + "OOOOO";

        final Day12 puzzle = new Day12();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(772L, puzzle.getPriceSum());
    }

    @Test
    public void largerExample() {
        final String map = "RRRRIICCFF\n" + "RRRRIICCCF\n" + "VVRRRCCFFF\n" + "VVRCCCJFFF\n" + "VVVVCJJCFE\n"
                + "VVIVCCJJEE\n" + "VVIIICJJEE\n" + "MIIIIIJJEE\n" + "MIIISIJEEE\n" + "MMMISSJEEE";

        final Day12 puzzle = new Day12();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(1930L, puzzle.getPriceSum());
    }
}
