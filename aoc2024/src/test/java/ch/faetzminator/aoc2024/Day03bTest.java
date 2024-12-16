package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aocutil.test.PuzzleTest;

public class Day03bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

        final Day03b puzzle = new Day03b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(48L, puzzle.getProductSum());
    }
}
