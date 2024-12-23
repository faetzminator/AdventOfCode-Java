package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day07Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "32T3K 765\n" + "T55J5 684\n" + "KK677 28\n" + "KTJJT 220\n" + "QQQJA 483";

        final Day07 puzzle = new Day07();
        for (final String line : toList(input)) {
            puzzle.parseHand(line);
        }
        Assertions.assertEquals(6440L, puzzle.calculateSum());
    }
}
