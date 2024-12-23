package ch.faetzminator.aoc2023;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day04bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53\n"
                + "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19\n"
                + "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1\n"
                + "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83\n"
                + "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36\n"
                + "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11";

        final Day04b puzzle = new Day04b();
        final List<String> lines = toList(input);
        puzzle.init(lines.size());
        for (final String line : lines) {
            puzzle.addScratchcard(line);
        }
        Assertions.assertEquals(30L, puzzle.getScratchcardSum());
    }
}
