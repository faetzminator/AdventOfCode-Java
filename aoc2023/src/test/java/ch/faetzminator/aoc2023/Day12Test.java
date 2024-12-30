package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day12Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "???.### 1,1,3\n" + ".??..??...?##. 1,1,3\n" + "?#?#?#?#?#?#?#? 1,3,1,6\n"
                + "????.#...#... 4,1,1\n" + "????.######..#####. 1,6,5\n" + "?###???????? 3,2,1";

        final Day12 puzzle = new Day12();
        for (final String line : toList(input)) {
            puzzle.parseConditionRecord(line);
        }
        Assertions.assertEquals(21L, puzzle.getArrangementSum());
    }
}
