package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day09bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "2333133121414131402";

        final Day09b puzzle = new Day09b();
        puzzle.parseLine(input);
        puzzle.defragment();
        Assertions.assertEquals(2858L, puzzle.getChecksum());
    }
}
