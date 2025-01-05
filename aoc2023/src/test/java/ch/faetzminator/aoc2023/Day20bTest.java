package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day20bTest extends PuzzleTest {

    @Test
    public void moreInterestingExample() {
        // TODO better test
        final String input = "broadcaster -> a\n" + "%a -> inv, con\n" + "&inv -> b\n" + "%b -> con\n" + "&con -> rx";

        final Day20b puzzle = new Day20b();
        for (final String line : toList(input)) {
            puzzle.parseModule(line);
        }
        Assertions.assertEquals(1L, puzzle.pressButtonPlentyTimes());
    }
}
