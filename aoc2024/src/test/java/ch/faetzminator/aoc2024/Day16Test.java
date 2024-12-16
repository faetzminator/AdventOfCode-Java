package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day16Test extends PuzzleTest {

    @Test
    public void example() {
        final String map = "###############\n" + "#.......#....E#\n" + "#.#.###.#.###.#\n" + "#.....#.#...#.#\n"
                + "#.###.#####.#.#\n" + "#.#.#.......#.#\n" + "#.#.#####.###.#\n" + "#...........#.#\n"
                + "###.#.#####.#.#\n" + "#...#.....#.#.#\n" + "#.#.#.###.#.#.#\n" + "#.....#...#.#.#\n"
                + "#.###.#.#.#.#.#\n" + "#S..#.....#...#\n" + "###############";

        final Day16 puzzle = new Day16();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(7036L, puzzle.calculateLowestScore());
    }

    @Test
    public void otherExample() {
        final String map = "#################\n" + "#...#...#...#..E#\n" + "#.#.#.#.#.#.#.#.#\n" + "#.#.#.#...#...#.#\n"
                + "#.#.#.#.###.#.#.#\n" + "#...#.#.#.....#.#\n" + "#.#.#.#.#.#####.#\n" + "#.#...#.#.#.....#\n"
                + "#.#.#####.#.###.#\n" + "#.#.#.......#...#\n" + "#.#.###.#####.###\n" + "#.#.#...#.....#.#\n"
                + "#.#.#.#####.###.#\n" + "#.#.#.........#.#\n" + "#.#.#.#########.#\n" + "#S#.............#\n"
                + "#################";

        final Day16 puzzle = new Day16();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(11048L, puzzle.calculateLowestScore());
    }
}
