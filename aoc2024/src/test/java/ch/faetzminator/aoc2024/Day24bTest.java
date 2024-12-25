package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoc2024.Day24b.Solver;
import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day24bTest extends PuzzleTest {

    @Test
    public void example() {
        final String input = "x00: 0\n" + "x01: 1\n" + "x02: 0\n" + "x03: 1\n" + "x04: 0\n" + "x05: 1\n" + "y00: 0\n"
                + "y01: 0\n" + "y02: 1\n" + "y03: 1\n" + "y04: 0\n" + "y05: 1\n" + "\n" + "x00 AND y00 -> z05\n"
                + "x01 AND y01 -> z02\n" + "x02 AND y02 -> z01\n" + "x03 AND y03 -> z03\n" + "x04 AND y04 -> z04\n"
                + "x05 AND y05 -> z00";
        final String[] inputs = input.split("\\n\\n");

        final Day24b puzzle = new Day24b();

        puzzle.setSwapsNeeded(2);
        puzzle.setSolver(new Solver() {
            @Override
            protected String calculateExpected(final String x, final String y) {
                final char[] result = new char[x.length()];
                for (int i = 0; i < x.length(); i++) {
                    result[i] = x.charAt(i) == '1' && y.charAt(i) == '1' ? '1' : '0';
                }
                return String.valueOf(result);
            }
        });

        for (final String line : toList(inputs[0])) {
            puzzle.parseWire(line);
        }
        for (final String line : toList(inputs[1])) {
            puzzle.parseGate(line);
        }
        Assertions.assertEquals("z00,z01,z02,z05", puzzle.getBruteForceSolution());
    }
}
