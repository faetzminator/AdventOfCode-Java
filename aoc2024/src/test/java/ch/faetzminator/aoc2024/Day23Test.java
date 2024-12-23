package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day23Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "kh-tc\n" + "qp-kh\n" + "de-cg\n" + "ka-co\n" + "yn-aq\n" + "qp-ub\n" + "cg-tb\n"
                + "vc-aq\n" + "tb-ka\n" + "wh-tc\n" + "yn-cg\n" + "kh-ub\n" + "ta-co\n" + "de-co\n" + "tc-td\n"
                + "tb-wq\n" + "wh-td\n" + "ta-ka\n" + "td-qp\n" + "aq-cg\n" + "wq-ub\n" + "ub-vc\n" + "de-ta\n"
                + "wq-aq\n" + "wq-vc\n" + "wh-yn\n" + "ka-de\n" + "kh-ta\n" + "co-tc\n" + "wh-qp\n" + "tb-vc\n"
                + "td-yn";

        final Day23 puzzle = new Day23();
        for (final String line : toList(input)) {
            puzzle.parseLine(line);
        }
        Assertions.assertEquals(7L, puzzle.getComputerSetSum());
    }
}
