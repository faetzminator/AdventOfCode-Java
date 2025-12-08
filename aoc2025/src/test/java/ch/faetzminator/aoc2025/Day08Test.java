package ch.faetzminator.aoc2025;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day08Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "162,817,812\n" + "57,618,57\n" + "906,360,560\n" + "592,479,940\n" + "352,342,300\n"
                + "466,668,158\n" + "542,29,236\n" + "431,825,988\n" + "739,650,466\n" + "52,470,668\n"
                + "216,146,977\n" + "819,987,18\n" + "117,168,530\n" + "805,96,715\n" + "346,949,466\n" + "970,615,88\n"
                + "941,993,340\n" + "862,61,35\n" + "984,92,344\n" + "425,690,689";

        final Day08 puzzle = new Day08();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(40L, puzzle.getSolution(10));
    }
}
