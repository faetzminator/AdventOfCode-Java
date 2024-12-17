package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day08bTest extends PuzzleTest {

    @Test
    public void example() {
        final String map = "............\n" + "........0...\n" + ".....0......\n" + ".......0....\n" + "....0.......\n"
                + "......A.....\n" + "............\n" + "............\n" + "........A...\n" + ".........A..\n"
                + "............\n" + "............";

        final Day08b puzzle = new Day08b();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(34L, puzzle.getAntinodeSum());
    }
}
