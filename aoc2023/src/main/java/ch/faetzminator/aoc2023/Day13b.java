package ch.faetzminator.aoc2023;

import java.util.List;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day13b {

    public static void main(final String[] args) {
        final Day13b puzzle = new Day13b();

        final List<List<String>> input = ScannerUtil.readNonBlankLinesBlocks();
        final Timer timer = PuzzleUtil.start();
        for (final List<String> lines : input) {
            puzzle.parsePattern(lines);
        }
        final long solution = puzzle.getSummary();
        PuzzleUtil.end(solution, timer);
    }

    private long summary;

    public void parsePattern(final List<String> lines) {
        final boolean[][] pattern = new boolean[lines.size()][];
        for (int y = 0; y < lines.size(); y++) {
            final String line = lines.get(y);
            pattern[y] = new boolean[line.length()];
            for (int x = 0; x < line.length(); x++) {
                pattern[y][x] = line.charAt(x) == '#';
            }
        }

        final int vMatch = getVerticalMatch(pattern);
        final int hMatch = getHorizontalMatch(pattern);
        if (vMatch >= 0) {
            summary += vMatch + 1;
        }
        if (hMatch >= 0) {
            summary += 100 * (hMatch + 1);
        }
    }

    private final int smudges = 1;

    private int getHorizontalMatch(final boolean[][] pattern) {
        for (int mirrorAfter = 0; mirrorAfter < pattern.length - 1; mirrorAfter++) {
            final int checks = Math.min(mirrorAfter + 1, pattern.length - mirrorAfter - 1);
            int issues = 0;
            for (int i = 0; i < checks && issues <= smudges; i++) {
                for (int x = 0; x < pattern[0].length && issues <= smudges; x++) {
                    if (pattern[mirrorAfter - i][x] != pattern[mirrorAfter + i + 1][x]) {
                        issues++;
                    }
                }
            }
            if (issues == smudges) {
                return mirrorAfter;
            }
        }
        return -1;
    }

    private int getVerticalMatch(final boolean[][] pattern) {
        for (int mirrorAfter = 0; mirrorAfter < pattern[0].length - 1; mirrorAfter++) {
            final int checks = Math.min(mirrorAfter + 1, pattern[0].length - mirrorAfter - 1);
            int issues = 0;
            for (int i = 0; i < checks && issues <= smudges; i++) {
                for (int y = 0; y < pattern.length && issues <= smudges; y++) {
                    if (pattern[y][mirrorAfter - i] != pattern[y][mirrorAfter + i + 1]) {
                        issues++;
                    }
                }
            }
            if (issues == smudges) {
                return mirrorAfter;
            }
        }
        return -1;
    }

    public long getSummary() {
        return summary;
    }
}
