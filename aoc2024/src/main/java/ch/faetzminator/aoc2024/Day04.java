package ch.faetzminator.aoc2024;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.CharPMap;

public class Day04 {

    public static void main(final String[] args) {
        final Day04 puzzle = new Day04();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.getXmasSum();
        PuzzleUtil.end(solution, timer);
    }

    private CharPMap map;

    public void parseLines(final List<String> input) {
        map = new CharPMap(input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.setElementAt(x, y, line.charAt(x));
            }
        }
    }

    public long getXmasSum() {
        long sum = 0L;
        for (int xMove = -1; xMove <= 1; xMove++) {
            for (int yMove = -1; yMove <= 1; yMove++) {
                if (xMove != 0 || yMove != 0) {
                    sum += getXmasSum(xMove, yMove);
                }
            }
        }
        return sum;
    }

    private long getXmasSum(final int xMove, final int yMove) {
        long sum = 0L;
        for (int x = 0; x < map.getXSize(); x++) {
            for (int y = 0; y < map.getYSize(); y++) {
                if (matches(x, y, xMove, yMove)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private static final char[] SEARCH = "XMAS".toCharArray();

    private boolean matches(final int x, final int y, final int xMove, final int yMove) {
        for (int i = 0; i < SEARCH.length; i++) {
            if (map.getElementAt(x + i * xMove, y + i * yMove) != SEARCH[i]) {
                return false;
            }
        }
        return true;
    }
}
