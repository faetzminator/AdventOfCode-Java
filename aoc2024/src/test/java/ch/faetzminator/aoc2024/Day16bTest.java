package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day16bTest extends PuzzleTest {

    @Test
    public void example() {
        final String map = "###############\n" + "#.......#....E#\n" + "#.#.###.#.###.#\n" + "#.....#.#...#.#\n"
                + "#.###.#####.#.#\n" + "#.#.#.......#.#\n" + "#.#.#####.###.#\n" + "#...........#.#\n"
                + "###.#.#####.#.#\n" + "#...#.....#.#.#\n" + "#.#.#.###.#.#.#\n" + "#.....#...#.#.#\n"
                + "#.###.#.#.#.#.#\n" + "#S..#.....#...#\n" + "###############";

        final Day16b puzzle = new Day16b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(45L, puzzle.calculateLowestScore());
    }

    @Test
    public void otherExample() {
        final String map = "#################\n" + "#...#...#...#..E#\n" + "#.#.#.#.#.#.#.#.#\n" + "#.#.#.#...#...#.#\n"
                + "#.#.#.#.###.#.#.#\n" + "#...#.#.#.....#.#\n" + "#.#.#.#.#.#####.#\n" + "#.#...#.#.#.....#\n"
                + "#.#.#####.#.###.#\n" + "#.#.#.......#...#\n" + "#.#.###.#####.###\n" + "#.#.#...#.....#.#\n"
                + "#.#.#.#####.###.#\n" + "#.#.#.........#.#\n" + "#.#.#.#########.#\n" + "#S#.............#\n"
                + "#################";

        final Day16b puzzle = new Day16b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(64L, puzzle.calculateLowestScore());
    }
}
