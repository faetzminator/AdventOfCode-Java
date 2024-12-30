package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day16bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = ".|...\\....\n" + "|.-.\\.....\n" + ".....|-...\n" + "........|.\n" + "..........\n"
                + ".........\\\n" + "..../.\\\\..\n" + ".-.-/..|..\n" + ".|....-|.\\\n" + "..//.|....";

        final Day16b puzzle = new Day16b();
        puzzle.parseLines(toList(input));
        puzzle.beam();
        Assertions.assertEquals(51L, puzzle.getHighestEnergizedSum());
    }
}
