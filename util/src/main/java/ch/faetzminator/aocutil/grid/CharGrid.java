package ch.faetzminator.aocutil.grid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * See {@link Grid}.
 */
public class CharGrid implements Iterable<Integer> {

    private final int[][] grid;
    private final char outOfRange;

    public CharGrid(final int xSize, final int ySize, final char outOfRange) {
        grid = new int[ySize][xSize];
        this.outOfRange = outOfRange;
    }

    public CharGrid(final int xSize, final int ySize) {
        this(xSize, ySize, (char) 0);
    }

    public int getXSize() {
        return grid[0].length;
    }

    public int getYSize() {
        return grid.length;
    }

    public void setElementAt(final Position position, final char element) {
        setElementAt(position.getX(), position.getY(), element);
    }

    public void setElementAt(final int x, final int y, final char element) {
        grid[y][x] = element;
    }

    public char getElementAt(final Position position) {
        return getElementAt(position.getX(), position.getY());
    }

    public char getElementAt(final int x, final int y) {
        return isInBounds(x, y) ? (char) grid[y][x] : outOfRange;
    }

    public boolean isInBounds(final Position position) {
        return isInBounds(position.getX(), position.getY());
    }

    public boolean isInBounds(final int x, final int y) {
        return x >= 0 && y >= 0 && x < grid[0].length && y < grid.length;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final int[] row : grid) {
            for (final int element : row) {
                builder.append((char) element);
            }
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public IntStream stream() {
        return Arrays.stream(grid).flatMapToInt(Arrays::stream);
    }

    @Override
    public Iterator<Integer> iterator() {
        return stream().iterator();
    }
}
