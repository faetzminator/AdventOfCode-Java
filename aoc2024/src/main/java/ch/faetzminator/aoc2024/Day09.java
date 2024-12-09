package ch.faetzminator.aoc2024;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day09 {

    public static void main(final String[] args) {
        final Day09 puzzle = new Day09();
        final String line = ScannerUtil.readNonBlankLine();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLine(line);
        puzzle.defragment();
        final long solution = puzzle.getChecksum();
        PuzzleUtil.end(solution, timer);
    }

    private final static char EMPTY = '.';

    private char[] disk;

    public void parseLine(final String line) {
        final int[] blockLengths = new int[line.length()];
        int size = 0;
        for (int i = 0; i < line.length(); i++) {
            blockLengths[i] = line.charAt(i) - '0';
            size += blockLengths[i];
        }
        disk = new char[size];
        int nextIndex = 0;
        int nextId = 0;
        boolean empty = false;
        for (int blockLength : blockLengths) {
            final char c = empty ? EMPTY : (char) ((nextId++) + '0');
            while (blockLength-- > 0) {
                disk[nextIndex++] = c;
            }
            empty = !empty;
        }
    }

    public void defragment() {
        int lastIndex = disk.length;
        for (int i = 0; i < lastIndex; i++) {
            if (disk[i] == EMPTY) {
                while (disk[--lastIndex] == EMPTY) {
                }
                disk[i] = disk[lastIndex];
                disk[lastIndex] = EMPTY;
            }
        }
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
