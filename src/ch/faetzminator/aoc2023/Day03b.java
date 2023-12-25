package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ch.faetzminator.aocutil.Point;

public class Day03b {

    public static void main(final String[] args) {
        final Day03b puzzle = new Day03b();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (final String line : input) {
            puzzle.addLine(line);
        }
        System.out.println("Solution: " + puzzle.calculatePartsSum());
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
                if (c == '*') {
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

        for (final Point symbol : symbols) {
            final List<Number> matchingNumbers = new ArrayList<>();
            for (final Number number : numbers) {
                final boolean yMatch = number.getY() <= symbol.getY() + 1 && number.getY() >= symbol.getY() - 1;
                final boolean xMatch = number.getX() <= symbol.getX() + 1 && number.getEndX() >= symbol.getX() - 1;
                if (yMatch && xMatch) {
                    matchingNumbers.add(number);
                }
            }
            if (matchingNumbers.size() >= 2) {
                long product = 1;
                for (final Number number : matchingNumbers) {
                    product *= number.getNumber();
                }
                sum += product;
            }
        }
        return sum;
    }

    private static class Number {

        private final int number;
        private final int x;
        private final int y;
        private final int length;

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
