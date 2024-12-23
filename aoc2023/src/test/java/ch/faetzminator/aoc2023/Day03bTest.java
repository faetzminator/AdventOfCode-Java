package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day03bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "467..114..\n" + "...*......\n" + "..35..633.\n" + "......#...\n" + "617*......\n"
                + ".....+.58.\n" + "..592.....\n" + "......755.\n" + "...$.*....\n" + ".664.598..";

        final Day03b puzzle = new Day03b();
        for (final String line : toList(input)) {
            puzzle.addLine(line);
        }
        Assertions.assertEquals(467835L, puzzle.calculatePartsSum());
    }
}
