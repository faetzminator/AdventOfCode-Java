package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day10bTest extends PuzzleTest {

    @Test
    public void example() {
        final String map = "0123\n" + "1234\n" + "8765\n" + "9876";

        final Day10b puzzle = new Day10b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(16L, puzzle.getTrailheadSum());
    }

    @Test
    public void largerExample() {
        final String map = "89010123\n" + "78121874\n" + "87430965\n" + "96549874\n" + "45678903\n" + "32019012\n"
                + "01329801\n" + "10456732";

        final Day10b puzzle = new Day10b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(81L, puzzle.getTrailheadSum());
    }
}