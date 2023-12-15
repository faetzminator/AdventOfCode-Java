package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day09 {

    public static void main(String[] args) {
        Day09 puzzle = new Day09();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (String line : input) {
            puzzle.addSequence(line);
        }
        System.out.println("Solution: " + puzzle.getExtrapolationSum());
    }

    private long extrapolationSum = 0;

    private boolean atEnd(List<Value> values) {
        for (Value value : values) {
            if (value.getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    public void addSequence(String str) {
        List<Stack<Value>> numbers = new ArrayList<>();
        Stack<Value> firstLine = new Stack<>();
        numbers.add(firstLine);
        for (String number : str.split(" ")) {
            firstLine.add(new Value(Long.parseLong(number)));
        }

        Stack<Value> lastLine;
        while (!atEnd(lastLine = numbers.get(numbers.size() - 1))) {
            Stack<Value> newLine = new Stack<>();
            numbers.add(newLine);
            for (int i = 1; i < lastLine.size(); i++) {
                newLine.add(child(lastLine.get(i - 1), lastLine.get(i)));
            }
        }

        lastLine.add(new Value(0));
        for (int i = numbers.size() - 2; i >= 0; i--) {
            Stack<Value> currentLine = numbers.get(i);
            Stack<Value> lowerLine = numbers.get(i + 1);
            currentLine.add(parent(currentLine.lastElement(), lowerLine.lastElement()));
        }

        extrapolationSum += numbers.get(0).lastElement().getValue();
    }

    public long getExtrapolationSum() {
        return extrapolationSum;
    }

    class Value {

        long value;

        public Value(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

    public Value child(Value leftParent, Value rightParent) {
        return new Value(rightParent.getValue() - leftParent.getValue());
    }

    public Value parent(Value leftParent, Value child) {
        return new Value(leftParent.getValue() + child.getValue());
    }
}
