package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day11Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "aaa: you hhh\n" + "you: bbb ccc\n" + "bbb: ddd eee\n" + "ccc: ddd eee fff\n"
                + "ddd: ggg\n" + "eee: out\n" + "fff: out\n" + "ggg: out\n" + "hhh: ccc fff iii\n" + "iii: out";

        final Day11 puzzle = new Day11();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(5L, puzzle.getSolution());
    }
}
