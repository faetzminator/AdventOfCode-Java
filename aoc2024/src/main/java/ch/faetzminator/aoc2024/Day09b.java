package ch.faetzminator.aoc2024;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day09b {

    public static void main(final String[] args) {
        final Day09b puzzle = new Day09b();
        final String line = ScannerUtil.readNonBlankLine();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLine(line);
        puzzle.defragment();
        final long solution = puzzle.getChecksum();
        PuzzleUtil.end(solution, timer);
    }

    private final static char EMPTY = '.';

    // TODO rewrite this in OOP ;)
    private char[] disk;
    private int[] blockLengths;
    private int[] blockStarts;

    public void parseLine(final String line) {
        blockLengths = new int[line.length()];
        blockStarts = new int[blockLengths.length];
        int size = 0;
        for (int i = 0; i < line.length(); i++) {
            blockLengths[i] = line.charAt(i) - '0';
            size += blockLengths[i];
        }
        disk = new char[size];
        int nextIndex = 0;
        int nextId = 0;
        boolean empty = false;
        for (int i = 0; i < blockLengths.length; i++) {
            int blockLength = blockLengths[i];
            blockStarts[i] = nextIndex;
            final char c = empty ? EMPTY : (char) ((nextId++) + '0');
            while (blockLength-- > 0) {
                disk[nextIndex++] = c;
            }
            empty = !empty;
        }
    }

    public void defragment() {
        if (blockLengths.length % 2 == 0) {
            throw new IllegalArgumentException();
        }
        for (int i = blockLengths.length - 1; i >= 0; i -= 2) {
            final int length = blockLengths[i];
            final int start = blockStarts[i];
            final int spacePos = findSpace(i, length);
            if (spacePos >= 0) {
                for (int j = 0; j < length; j++) {
                    disk[spacePos + j] = disk[start + j];
                    disk[start + j] = EMPTY;
                }
            }
        }
    }

    private int findSpace(final int blockFrom, final int blockFromLength) {
        for (int i = 1; i < blockFrom; i += 2) {
            final int length = blockLengths[i];
            final int start = blockStarts[i];
            if (length < blockFromLength) {
                // probably unnecessary performance improvement
                continue;
            }
            int counted = 0;
            for (int j = 0; j < length; j++) {
                if (disk[start + j] == EMPTY) {
                    counted++;
                } else {
                    counted = 0;
                }
                if (counted == blockFromLength) {
                    return start + j - counted + 1;
                }
            }
        }
        return -1;
    }

    public long getChecksum() {
        long sum = 0L;
        for (int i = 0; i < disk.length; i++) {
            if (disk[i] != '.') {
                sum += i * (disk[i] - '0');
            }
        }
        return sum;
    }
}
