package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day09Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "2333133121414131402";

        final Day09 puzzle = new Day09();
        puzzle.parseLine(input);
        puzzle.defragment();
        Assertions.assertEquals(1928L, puzzle.getChecksum());
    }
}
