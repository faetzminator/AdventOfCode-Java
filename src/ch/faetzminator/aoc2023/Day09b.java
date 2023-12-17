package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day09b {

    public static void main(final String[] args) {
        final Day09b puzzle = new Day09b();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (final String line : input) {
            puzzle.addSequence(line);
        }
        System.out.println("Solution: " + puzzle.getExtrapolationSum());
    }

    private long extrapolationSum;

    private boolean atEnd(final List<Value> values) {
        for (final Value value : values) {
            if (value.getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    public void addSequence(final String str) {
        final List<Stack<Value>> numbers = new ArrayList<>();
        final Stack<Value> firstLine = new Stack<>();
        numbers.add(firstLine);
        for (final String number : str.split(" ")) {
            firstLine.add(new Value(Long.parseLong(number)));
        }

        Stack<Value> lastLine;
        while (!atEnd(lastLine = numbers.get(numbers.size() - 1))) {
            final Stack<Value> newLine = new Stack<>();
            numbers.add(newLine);
            for (int i = 1; i < lastLine.size(); i++) {
                newLine.add(child(lastLine.get(i - 1), lastLine.get(i)));
            }
        }

        // no clue why I'm creating this data structure at all
        lastLine.add(new Value(0));
        for (int i = numbers.size() - 2; i >= 0; i--) {
            final Stack<Value> currentLine = numbers.get(i);
            final Stack<Value> lowerLine = numbers.get(i + 1);
            currentLine.insertElementAt(parent(currentLine.firstElement(), lowerLine.firstElement()), 0);
        }

        extrapolationSum += numbers.get(0).firstElement().getValue();
    }

    public long getExtrapolationSum() {
        return extrapolationSum;
    }

    private static class Value {

        private final long value;

        public Value(final long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

    private static Value child(final Value leftParent, final Value rightParent) {
        return new Value(rightParent.getValue() - leftParent.getValue());
    }

    private static Value parent(final Value rightParent, final Value child) {
        return new Value(rightParent.getValue() - child.getValue());
    }
}
