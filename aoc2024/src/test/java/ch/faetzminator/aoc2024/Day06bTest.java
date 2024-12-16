package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day06bTest extends PuzzleTest {

    @Test
    public void example() {
        final String map = "....#.....\n" + ".........#\n" + "..........\n" + "..#.......\n" + ".......#..\n"
                + "..........\n" + ".#..^.....\n" + "........#.\n" + "#.........\n" + "......#...";

        final Day06 puzzle = new Day06();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(41L, puzzle.walk());
    }
}
