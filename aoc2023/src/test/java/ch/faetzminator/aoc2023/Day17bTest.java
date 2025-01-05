package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day17bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "2413432311323\n" + "3215453535623\n" + "3255245654254\n" + "3446585845452\n"
                + "4546657867536\n" + "1438598798454\n" + "4457876987766\n" + "3637877979653\n" + "4654967986887\n"
                + "4564679986453\n" + "1224686865563\n" + "2546548887735\n" + "4322674655533";

        final Day17b puzzle = new Day17b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(94L, puzzle.calculateLeastHeatLoss());
    }

    @Test
    public void anotherExample() {
        final String input = "111111111111\n" + "999999999991\n" + "999999999991\n" + "999999999991\n" + "999999999991";

        final Day17b puzzle = new Day17b();
        puzzle.parseLines(toList(input));
        Assertions.assertEquals(71L, puzzle.calculateLeastHeatLoss());
    }
}
