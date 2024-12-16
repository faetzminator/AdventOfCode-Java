package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day13Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "Button A: X+94, Y+34\n" + "Button B: X+22, Y+67\n" + "Prize: X=8400, Y=5400\n" + "\n"
                + "Button A: X+26, Y+66\n" + "Button B: X+67, Y+21\n" + "Prize: X=12748, Y=12176\n" + "\n"
                + "Button A: X+17, Y+86\n" + "Button B: X+84, Y+37\n" + "Prize: X=7870, Y=6450\n" + "\n"
                + "Button A: X+69, Y+23\n" + "Button B: X+27, Y+71\n" + "Prize: X=18641, Y=10279";

        final Day13 puzzle = new Day13();
        for (final String lines : input.split("\\n\\n")) {
            puzzle.parseLines(toList(lines));
        }
        Assertions.assertEquals(480L, puzzle.getTokenSum());
    }
}