package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day15Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

        final Day15 puzzle = new Day15();
        puzzle.parseInput(input);
        Assertions.assertEquals(1320L, puzzle.getHashSum());
    }
}
