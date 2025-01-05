package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day20Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "broadcaster -> a, b, c\n" + "%a -> b\n" + "%b -> c\n" + "%c -> inv\n" + "&inv -> a";

        final Day20 puzzle = new Day20();
        for (final String line : toList(input)) {
            puzzle.parseModule(line);
        }
        puzzle.pressButtonPlentyTimes();
        Assertions.assertEquals(32000000L, puzzle.getPulseProduct());
    }

    @Test
    public void moreInterestingExample() {
        final String input = "broadcaster -> a\n" + "%a -> inv, con\n" + "&inv -> b\n" + "%b -> con\n"
                + "&con -> output";

        final Day20 puzzle = new Day20();
        for (final String line : toList(input)) {
            puzzle.parseModule(line);
        }
        puzzle.pressButtonPlentyTimes();
        Assertions.assertEquals(11687500L, puzzle.getPulseProduct());
    }
}
