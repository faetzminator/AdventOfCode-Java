package ch.faetzminator.aoc2024;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day17b {

    public static void main(final String[] args) {
        final Day17b puzzle = new Day17b();
        final List<String> registerLines;
        final String programLine;
        try (Scanner scanner = new Scanner(System.in)) {
            registerLines = ScannerUtil.readNonBlankLines(scanner);
            programLine = ScannerUtil.readNonBlankLine(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        for (final String line : registerLines) {
            puzzle.parseRegister(line);
        }
        puzzle.parseProgram(programLine);
        final long solution = puzzle.runManyTimes();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern REGISTER_LINE_PATTERN = Pattern.compile("Register ([A-Z]): (\\d+)");
    private static final Pattern PROGRAM_LINE_PATTERN = Pattern.compile("Program: ((?:[0-7],)+[0-7])");

    private final Map<Register, Long> registers = new LinkedHashMap<>();
    private int[] instructions;
    private int pointer;

    public void parseRegister(final String line) {
        final Matcher matcher = REGISTER_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final Register register = Register.byChar(matcher.group(1).charAt(0));
        final Long value = Long.parseLong(matcher.group(2));
        registers.put(register, value);
    }

    public void parseProgram(final String line) {
        final Matcher matcher = PROGRAM_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final String[] values = matcher.group(1).split(",");
        instructions = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            instructions[i] = Integer.parseInt(values[i]);
        }
    }

    public long runManyTimes() {
        final Map<Register, Long> originalReg = new LinkedHashMap<>(registers);

        long lastValue = 1L;

        for (int len = 2; len <= instructions.length; len++) {
            final int[] expected = new int[len];
            System.arraycopy(instructions, instructions.length - len, expected, 0, len);
            // this works... somehow...
            for (long a = 0L; a < Math.max(50L, lastValue * 8); a++) {
                registers.putAll(originalReg);
                registers.put(Register.A, lastValue * 8 + a);
                pointer = 0;
                if (run(expected)) {
                    lastValue = lastValue * 8 + a;
                    if (len == instructions.length) {
                        return lastValue;
                    }
                    break;
                }
            }
        }
        return -1L;
    }

    private boolean run(final int[] expected) {
        int outputIndex = 0;
        while (pointer < instructions.length) {
            final Instruction instruction = Instruction.byId(instructions[pointer]);
            final int value = instructions[pointer + 1];
            final long result = instruction.run(new Input(registers, value));
            if (instruction == Instruction.OUT) {
                if (outputIndex >= expected.length) {
                    return false;
                }
                if (expected[outputIndex++] != result) {
                    return false;
                }
            }
            if (instruction == Instruction.JNZ && result >= 0) {
                pointer = (int) result;
            } else {
                pointer += 2;
            }
        }
        return outputIndex == expected.length;
    }

    private static enum Register implements CharEnum {

        A,
        B,
        C;

        private final char character = name().charAt(0);

        @Override
        public char charValue() {
            return character;
        }

        public static Register byChar(final char c) {
            return CharEnum.byChar(Register.class, c);
        }
    }

    private static class Input {

        private final Map<Register, Long> registers;
        private final int value;

        public Input(final Map<Register, Long> registers, final int value) {
            super();
            this.registers = registers;
            this.value = value;
        }

        public long get(final Register register) {
            return registers.get(register);
        }

        public long set(final Register register, final long value) {
            registers.put(register, value);
            return value;
        }

        public long get() {
            return value;
        }

        public long getCombo() {
            return ComboOperand.byId(value).get(registers);
        }
    }

    private static enum Instruction {

        ADV(0, input -> input.set(Register.A, (long) (input.get(Register.A) / Math.pow(2, input.getCombo())))),
        BXL(1, input -> input.set(Register.B, input.get(Register.B) ^ input.get())),
        BST(2, input -> input.set(Register.B, input.getCombo() % 8)),
        JNZ(3, input -> (input.get(Register.A) == 0L ? -1L : input.get())),
        BXC(4, input -> input.set(Register.B, input.get(Register.B) ^ input.get(Register.C))),
        OUT(5, input -> input.getCombo() % 8),
        BDV(6, input -> input.set(Register.B, (long) (input.get(Register.A) / Math.pow(2, input.getCombo())))),
        CDV(7, input -> input.set(Register.C, (long) (input.get(Register.A) / Math.pow(2, input.getCombo()))));

        private final int id;
        private final Function<Input, Long> fn;

        private Instruction(final int id, final Function<Input, Long> fn) {
            this.id = id;
            this.fn = fn;
        }

        public long run(final Input value) {
            return fn.apply(value);
        }

        public static Instruction byId(final int id) {
            for (final Instruction item : values()) {
                if (item.id == id) {
                    return item;
                }
            }
            throw new IllegalArgumentException("id: " + id);
        }
    }

    private static enum ComboOperand {

        VAL0(0, registers -> 0L),
        VAL1(1, registers -> 1L),
        VAL2(2, registers -> 2L),
        VAL3(3, registers -> 3L),
        REGA(4, registers -> registers.get(Register.A)),
        REGB(5, registers -> registers.get(Register.B)),
        REGC(6, registers -> registers.get(Register.C)),
        RES0(7, registers -> {
            throw new IllegalArgumentException();
        });

        private final int id;
        private final Function<Map<Register, Long>, Long> fn;

        private ComboOperand(final int id, final Function<Map<Register, Long>, Long> fn) {
            this.id = id;
            this.fn = fn;
        }

        public long get(final Map<Register, Long> registers) {
            return fn.apply(registers);
        }

        public static ComboOperand byId(final int id) {
            for (final ComboOperand item : values()) {
                if (item.id == id) {
                    return item;
                }
            }
            throw new IllegalArgumentException("id: " + id);
        }
    }
}
