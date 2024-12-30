package ch.faetzminator.aoc2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day12bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "???.### 1,1,3\n" + ".??..??...?##. 1,1,3\n" + "?#?#?#?#?#?#?#? 1,3,1,6\n"
                + "????.#...#... 4,1,1\n" + "????.######..#####. 1,6,5\n" + "?###???????? 3,2,1";

        final Day12b puzzle = new Day12b();
        for (final String line : toList(input)) {
            puzzle.parseConditionRecord(line);
        }
        Assertions.assertEquals(525152L, puzzle.getArrangementSum());
    }
}
