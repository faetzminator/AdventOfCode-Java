package ch.faetzminator.aoc2023;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day15 {

    public static void main(final String[] args) {
        final Day15 puzzle = new Day15();

        final String input = ScannerUtil.readNonBlankLine();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseInput(input);
        final long solution = puzzle.getHashSum();
        PuzzleUtil.end(solution, timer);
    }

    private long hashSum;

    public void parseInput(final String str) {
        for (final String value : str.split(",")) {
            hashSum += calculateHash(value);
        }
    }

    private int calculateHash(final String str) {
        int hash = 0;
        for (final char c : str.toCharArray()) {
            hash += c;
            hash *= 17;
            hash = hash % 256;
        }
        return hash;
    }

    public long getHashSum() {
        return hashSum;
    }
}
