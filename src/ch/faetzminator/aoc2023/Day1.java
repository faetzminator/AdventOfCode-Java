package ch.faetzminator.aoc2023;

import java.util.Scanner;

public class Day1 {

	public static void main(String[] args) {
		Day1 puzzle = new Day1();

		try (Scanner scanner = new Scanner(System.in)) {
			String line;
			while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
				puzzle.addLine(line);
			}
		}
		System.out.println("Solution: " + puzzle.getCalibrationSum());
	}

	private long calibrationSum;

	public void addLine(String str) {
		calibrationSum += parseCalibration(str);
	}

	public int parseCalibration(String str) {
		char first = 0, last = 0;
		boolean firstSet = false;
		for (char c : str.toCharArray()) {
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
