package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day25Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "jqt: rhn xhk nvd\n" + "rsh: frs pzl lsr\n" + "xhk: hfx\n" + "cmg: qnr nvd lhk bvb\n"
                + "rhn: xhk bvb hfx\n" + "bvb: xhk hfx\n" + "pzl: lsr hfx nvd\n" + "qnr: nvd\n"
                + "ntq: jqt hfx bvb xhk\n" + "nvd: lhk\n" + "lsr: lhk\n" + "rzs: qnr cmg lsr rsh\n"
                + "frs: qnr lhk lsr";

        final Day25 puzzle = new Day25();
        for (final String line : toList(input)) {
            puzzle.parseNode(line);
        }
        Assertions.assertEquals(54L, puzzle.calculateGroups());
    }
}
