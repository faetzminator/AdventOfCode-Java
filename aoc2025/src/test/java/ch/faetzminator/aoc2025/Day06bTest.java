package ch.faetzminator.aoc2025;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day06bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "123 328  51 64 \n" + " 45 64  387 23 \n" + "  6 98  215 314\n" + "*   +   *   +  ";

        final Day06b puzzle = new Day06b();
        final List<String> lines = new ArrayList<>(toList(input));
        puzzle.parseLastLine(lines.remove(lines.size() - 1));
        puzzle.parseLines(lines);
        Assertions.assertEquals(3263827L, puzzle.getSolution());
    }
}
