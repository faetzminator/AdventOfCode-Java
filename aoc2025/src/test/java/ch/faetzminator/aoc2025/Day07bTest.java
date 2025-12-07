package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day07bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = ".......S.......\n" + "...............\n" + ".......^.......\n" + "...............\n"
                + "......^.^......\n" + "...............\n" + ".....^.^.^.....\n" + "...............\n"
                + "....^.^...^....\n" + "...............\n" + "...^.^...^.^...\n" + "...............\n"
                + "..^...^.....^..\n" + "...............\n" + ".^.^.^.^.^...^.\n" + "...............";

        final Day07b puzzle = new Day07b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(40L, puzzle.getSolution());
    }
}
