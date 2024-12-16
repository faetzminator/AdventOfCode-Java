package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day12bTest extends PuzzleTest {

    @Test
    public void example() {
        final String map = "AAAA\n" + "BBCD\n" + "BBCC\n" + "EEEC";

        final Day12b puzzle = new Day12b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(80L, puzzle.getPriceSum());
    }

    @Test
    public void secondExample() {
        final String map = "OOOOO\n" + "OXOXO\n" + "OOOOO\n" + "OXOXO\n" + "OOOOO";

        final Day12b puzzle = new Day12b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(436L, puzzle.getPriceSum());
    }

    @Test
    public void eShapedExample() {
        final String map = "EEEEE\n" + "EXXXX\n" + "EEEEE\n" + "EXXXX\n" + "EEEEE";

        final Day12b puzzle = new Day12b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(236L, puzzle.getPriceSum());
    }

    @Test
    public void beCarefulExample() {
        final String map = "AAAAAA\n" + "AAABBA\n" + "AAABBA\n" + "ABBAAA\n" + "ABBAAA\n" + "AAAAAA";

        final Day12b puzzle = new Day12b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(368L, puzzle.getPriceSum());
    }
}
