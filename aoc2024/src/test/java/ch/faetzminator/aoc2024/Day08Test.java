package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day08Test extends PuzzleTest {

    @Test
    public void example() {
        final String map = "............\n" + "........0...\n" + ".....0......\n" + ".......0....\n" + "....0.......\n"
                + "......A.....\n" + "............\n" + "............\n" + "........A...\n" + ".........A..\n"
                + "............\n" + "............";

        final Day08 puzzle = new Day08();
        puzzle.parseLines(toList(map));
        Assertions.assertEquals(14L, puzzle.getAntinodeSum());
    }
}
