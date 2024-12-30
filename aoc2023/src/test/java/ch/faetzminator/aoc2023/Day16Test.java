package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day16Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = ".|...\\....\n" + "|.-.\\.....\n" + ".....|-...\n" + "........|.\n" + "..........\n"
                + ".........\\\n" + "..../.\\\\..\n" + ".-.-/..|..\n" + ".|....-|.\\\n" + "..//.|....";

        final Day16 puzzle = new Day16();
        puzzle.parseLines(toList(input));
        puzzle.beam();
        Assertions.assertEquals(46L, puzzle.getEnergizedSum());
    }
}
