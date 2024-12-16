package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day06Test extends PuzzleTest {

    @Test
    public void example() {
        final String map = "....#.....\n" + ".........#\n" + "..........\n" + "..#.......\n" + ".......#..\n"
                + "..........\n" + ".#..^.....\n" + "........#.\n" + "#.........\n" + "......#...";

        final Day06b puzzle = new Day06b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(6L, puzzle.walkAllPossibilities());
    }
}
