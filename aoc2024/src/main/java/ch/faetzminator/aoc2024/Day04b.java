package ch.faetzminator.aoc2024;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.CharPMap;

public class Day04b {

    public static void main(final String[] args) {
        final Day04b puzzle = new Day04b();
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
        for (int x = 0; x < map.getXSize(); x++) {
            for (int y = 0; y < map.getYSize(); y++) {
                if (matches(x, y)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private boolean matches(final int x, final int y) {
        return map.getElementAt(x, y) == 'A' && check(x, y, 1, 1) && check(x, y, 1, -1);
    }

    private boolean check(final int x, final int y, final int xMove, final int yMove) {
        final char one = map.getElementAt(x + xMove, y + yMove);
        final char other = map.getElementAt(x - xMove, y - yMove);
        return (one == 'M' && other == 'S') || (other == 'M' && one == 'S');
    }
}
