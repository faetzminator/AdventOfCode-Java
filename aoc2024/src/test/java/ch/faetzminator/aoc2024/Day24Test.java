package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.faetzminator.aoctestutil.PuzzleTest;

public class Day24Test extends PuzzleTest {

    @Test
    public void example() {
        final String input = "x00: 1\n" + "x01: 1\n" + "x02: 1\n" + "y00: 0\n" + "y01: 1\n" + "y02: 0\n" + "\n"
                + "x00 AND y00 -> z00\n" + "x01 XOR y01 -> z01\n" + "x02 OR y02 -> z02";
        final String[] inputs = input.split("\\n\\n");

        final Day24 puzzle = new Day24();
        for (final String line : toList(inputs[0])) {
            puzzle.parseWire(line);
        }
        for (final String line : toList(inputs[1])) {
            puzzle.parseGate(line);
        }
        Assertions.assertEquals(4L, puzzle.getSolution());
    }

    @Test
    public void largerExample() {
        final String input = "x00: 1\n" + "x01: 0\n" + "x02: 1\n" + "x03: 1\n" + "x04: 0\n" + "y00: 1\n" + "y01: 1\n"
                + "y02: 1\n" + "y03: 1\n" + "y04: 1\n" + "\n" + "ntg XOR fgs -> mjb\n" + "y02 OR x01 -> tnw\n"
                + "kwq OR kpj -> z05\n" + "x00 OR x03 -> fst\n" + "tgd XOR rvg -> z01\n" + "vdt OR tnw -> bfw\n"
                + "bfw AND frj -> z10\n" + "ffh OR nrd -> bqk\n" + "y00 AND y03 -> djm\n" + "y03 OR y00 -> psh\n"
                + "bqk OR frj -> z08\n" + "tnw OR fst -> frj\n" + "gnj AND tgd -> z11\n" + "bfw XOR mjb -> z00\n"
                + "x03 OR x00 -> vdt\n" + "gnj AND wpb -> z02\n" + "x04 AND y00 -> kjc\n" + "djm OR pbm -> qhw\n"
                + "nrd AND vdt -> hwm\n" + "kjc AND fst -> rvg\n" + "y04 OR y02 -> fgs\n" + "y01 AND x02 -> pbm\n"
                + "ntg OR kjc -> kwq\n" + "psh XOR fgs -> tgd\n" + "qhw XOR tgd -> z09\n" + "pbm OR djm -> kpj\n"
                + "x03 XOR y03 -> ffh\n" + "x00 XOR y04 -> ntg\n" + "bfw OR bqk -> z06\n" + "nrd XOR fgs -> wpb\n"
                + "frj XOR qhw -> z04\n" + "bqk OR frj -> z07\n" + "y03 OR x01 -> nrd\n" + "hwm AND bqk -> z03\n"
                + "tgd XOR rvg -> z12\n" + "tnw OR pbm -> gnj";
        final String[] inputs = input.split("\\n\\n");

        final Day24 puzzle = new Day24();
        for (final String line : toList(inputs[0])) {
            puzzle.parseWire(line);
        }
        for (final String line : toList(inputs[1])) {
            puzzle.parseGate(line);
        }
        Assertions.assertEquals(2024L, puzzle.getSolution());
    }
}
