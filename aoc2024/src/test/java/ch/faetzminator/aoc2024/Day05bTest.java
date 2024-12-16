package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day05bTest extends PuzzleTest {

    @Test
    public void example() {
        final String rules = "47|53\n" + "97|13\n" + "97|61\n" + "97|47\n" + "75|29\n" + "61|13\n" + "75|53\n"
                + "29|13\n" + "97|29\n" + "53|29\n" + "61|53\n" + "97|53\n" + "61|29\n" + "47|13\n" + "75|47\n"
                + "97|75\n" + "47|61\n" + "75|61\n" + "47|29\n" + "75|13\n" + "53|13";
        final String updates = "75,47,61,53,29\n" + "97,61,53,29,13\n" + "75,29,13\n" + "75,97,47,61,53\n"
                + "61,13,29\n" + "97,13,75,29,47\n" + "";

        final Day05b puzzle = new Day05b();
        for (final String line : toList(rules)) {
            puzzle.parseRule(line);
        }
        puzzle.init();
        for (final String line : toList(updates)) {
            puzzle.parseUpdate(line);
        }
        Assertions.assertEquals(123L, puzzle.getPageNumberSum());
    }
}
