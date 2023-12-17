package ch.faetzminator.aoc2023;

import java.util.Scanner;

public class Day01 {

    public static void main(final String[] args) {
        final Day01 puzzle = new Day01();

        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                puzzle.addLine(line);
            }
        }
        System.out.println("Solution: " + puzzle.getCalibrationSum());
    }

    private long calibrationSum;

    public void addLine(final String str) {
        calibrationSum += parseCalibration(str);
    }

    private int parseCalibration(final String str) {
        char first = 0, last = 0;
        boolean firstSet = false;
        for (final char c : str.toCharArray()) {
            if (c >= '0' && c <= '9') {
                if (!firstSet) {
                    first = c;
                    firstSet = true;
                }
                last = c;
            }
        }
        return 10 * (first - '0') + last - '0';
    }

    public long getCalibrationSum() {
        return calibrationSum;
    }
}
