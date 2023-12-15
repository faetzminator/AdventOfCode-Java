package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day01b {

    public static void main(String[] args) {
        Day01b puzzle = new Day01b();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (String line : input) {
            puzzle.addLine(line);
        }
        System.out.println("Solution: " + puzzle.getCalibrationSum());
    }

    private long calibrationSum;

    public void addLine(String str) {
        calibrationSum += parseCalibration(str);
    }

    public static final String[] NUMBERS = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
            "nine" };

    public int parseNumber(String str, int pos) {
        char c = str.charAt(pos);
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        String sub = str.substring(pos);
        // ignore zero, start 1
        for (int i = 1; i < NUMBERS.length; i++) {
            if (sub.startsWith(NUMBERS[i])) {
                return i;
            }
        }
        return -1;
    }

    public int parseCalibration(String str) {
        int first = 0, last = 0;
        boolean firstSet = false;
        for (int i = 0; i < str.length(); i++) {
            int number = parseNumber(str, i);
            if (number >= 0) {
                if (!firstSet) {
                    first = number;
                    firstSet = true;
                }
                last = number;
            }
        }
        return 10 * first + last;
    }

    public long getCalibrationSum() {
        return calibrationSum;
    }
}
