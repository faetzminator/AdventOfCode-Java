package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day25Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "#####\n" + ".####\n" + ".####\n" + ".####\n" + ".#.#.\n" + ".#...\n" + ".....\n" + "\n"
                + "#####\n" + "##.##\n" + ".#.##\n" + "...##\n" + "...#.\n" + "...#.\n" + ".....\n" + "\n" + ".....\n"
                + "#....\n" + "#....\n" + "#...#\n" + "#.#.#\n" + "#.###\n" + "#####\n" + "\n" + ".....\n" + ".....\n"
                + "#.#..\n" + "###..\n" + "###.#\n" + "###.#\n" + "#####\n" + "\n" + ".....\n" + ".....\n" + ".....\n"
                + "#....\n" + "#.#..\n" + "#.#.#\n" + "#####";
        final String[] inputs = input.split("\\n\\n");

        final Day25 puzzle = new Day25();
        for (final String block : inputs) {
            puzzle.parseBlock(toList(block));
        }
        Assertions.assertEquals(3L, puzzle.getFitTogetherSum());
    }
}
