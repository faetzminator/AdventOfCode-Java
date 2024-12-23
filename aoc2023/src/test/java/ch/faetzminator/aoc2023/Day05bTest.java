package ch.faetzminator.aoc2023;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day05bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "seeds: 79 14 55 13\n" + "\n" + "seed-to-soil map:\n" + "50 98 2\n" + "52 50 48\n" + "\n"
                + "soil-to-fertilizer map:\n" + "0 15 37\n" + "37 52 2\n" + "39 0 15\n" + "\n"
                + "fertilizer-to-water map:\n" + "49 53 8\n" + "0 11 42\n" + "42 0 7\n" + "57 7 4\n" + "\n"
                + "water-to-light map:\n" + "88 18 7\n" + "18 25 70\n" + "\n" + "light-to-temperature map:\n"
                + "45 77 23\n" + "81 45 19\n" + "68 64 13\n" + "\n" + "temperature-to-humidity map:\n" + "0 69 1\n"
                + "1 0 69\n" + "\n" + "humidity-to-location map:\n" + "60 56 37\n" + "56 93 4";

        final String[] blocks = input.split("\\n\\n");
        final Day05b puzzle = new Day05b();
        puzzle.addSeeds(blocks[0]);
        for (int i = 1; i < blocks.length; i++) {
            final List<String> lines = toList(blocks[i]);
            for (int j = 1; j < lines.size(); j++) {
                puzzle.move(lines.get(j));
            }
            puzzle.clear();
        }
        Assertions.assertEquals(46L, puzzle.getSeedStartWithLowestLocation());
    }
}
