package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day09 {

    public static void main(final String[] args) {
        final Day09 puzzle = new Day09();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.addSequence(line);
        }
        final long solution = puzzle.getExtrapolationSum();
        PuzzleUtil.end(solution, timer);
    }

    private long extrapolationSum;

    private boolean atEnd(final List<Long> values) {
        for (final long value : values) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    public void addSequence(final String str) {
        final List<Stack<Long>> numbers = new ArrayList<>();
        final Stack<Long> firstLine = new Stack<>();
        numbers.add(firstLine);
        for (final String number : str.split(" ")) {
            firstLine.add(Long.parseLong(number));
        }

        Stack<Long> lastLine;
        while (!atEnd(lastLine = numbers.get(numbers.size() - 1))) {
            final Stack<Long> newLine = new Stack<>();
            numbers.add(newLine);
            for (int i = 1; i < lastLine.size(); i++) {
                newLine.add(child(lastLine.get(i - 1), lastLine.get(i)));
            }
        }

        lastLine.add(0L);
        for (int i = numbers.size() - 2; i >= 0; i--) {
            final Stack<Long> currentLine = numbers.get(i);
            final Stack<Long> lowerLine = numbers.get(i + 1);
            currentLine.add(parent(currentLine.lastElement(), lowerLine.lastElement()));
        }

        extrapolationSum += numbers.get(0).lastElement();
    }

    public long getExtrapolationSum() {
        return extrapolationSum;
    }

    private static long child(final long leftParent, final long rightParent) {
        return rightParent - leftParent;
    }

    private static long parent(final long leftParent, final long child) {
        return leftParent + child;
    }
}
