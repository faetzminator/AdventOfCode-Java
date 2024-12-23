package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day07bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "32T3K 765\n" + "T55J5 684\n" + "KK677 28\n" + "KTJJT 220\n" + "QQQJA 483";

        final Day07b puzzle = new Day07b();
        for (final String line : toList(input)) {
            puzzle.parseHand(line);
        }
        Assertions.assertEquals(5905L, puzzle.calculateSum());
    }
}
