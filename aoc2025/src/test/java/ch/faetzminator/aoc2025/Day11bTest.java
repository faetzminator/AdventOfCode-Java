package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day11bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "svr: aaa bbb\n" + "aaa: fft\n" + "fft: ccc\n" + "bbb: tty\n" + "tty: ccc\n"
                + "ccc: ddd eee\n" + "ddd: hub\n" + "hub: fff\n" + "eee: dac\n" + "dac: fff\n" + "fff: ggg hhh\n"
                + "ggg: out\n" + "hhh: out";

        final Day11b puzzle = new Day11b();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(2L, puzzle.getSolution());
    }
}
