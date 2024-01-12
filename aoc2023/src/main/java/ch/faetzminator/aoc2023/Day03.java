package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.faetzminator.aocutil.Point;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day03 {

    public static void main(final String[] args) {
        final Day03 puzzle = new Day03();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.addLine(line);
        }
        final long solution = puzzle.calculatePartsSum();
        PuzzleUtil.end(solution, timer);
    }

    private final List<Number> numbers = new ArrayList<>();
    private final List<Point> symbols = new ArrayList<>();
    private int lineNumber;

    public void addLine(final String str) {
        final int y = lineNumber++;
        int x;
        int number = 0, count = 0;
        for (x = 0; x < str.length(); x++) {
            final char c = str.charAt(x);
            if (c >= '0' && c <= '9') {
                if (count == 0) {
                    number = 0;
                }
                count++;
                number = number * 10 + (c - '0');
            } else {
                if (count > 0) {
                    numbers.add(new Number(number, x - count, y, count));
                    count = 0;
                }
                if (c != '.') {
                    symbols.add(new Point(x, y));
                }
            }
        }
        if (count > 0) {
            numbers.add(new Number(number, x - count, y, count));
        }
    }

    public long calculatePartsSum() {
        long sum = 0;
        final Set<Number> alreadyUsed = new HashSet<>();

        for (final Point symbol : symbols) {
            for (final Number number : numbers) {
                if (!alreadyUsed.contains(number)) {
                    final boolean yMatch = number.getY() <= symbol.getY() + 1 && number.getY() >= symbol.getY() - 1;
                    final boolean xMatch = number.getX() <= symbol.getX() + 1 && number.getEndX() >= symbol.getX() - 1;
                    if (yMatch && xMatch) {
                        alreadyUsed.add(number);
                        sum += number.getNumber();
                    }
                }
            }
        }
        return sum;
    }

    private static class Number {

        final int number;

        final int x;
        final int y;
        final int length;

        public Number(final int number, final int x, final int y, final int length) {
            this.number = number;
            this.x = x;
            this.y = y;
            this.length = length;
        }

        public int getNumber() {
            return number;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getEndX() {
            return x + length - 1;
        }
    }
}
