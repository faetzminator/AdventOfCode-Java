package ch.faetzminator.aocutil.grid;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import ch.faetzminator.aocutil.CharPrintable;

public class Grid<T extends CharPrintable> implements Iterable<T> {

    private final T[][] grid;
    private final T outOfRange;

    @SuppressWarnings("unchecked")
    public Grid(final Class<T> clazz, final int xSize, final int ySize, final T outOfRange) {
        grid = (T[][]) Array.newInstance(clazz, ySize, xSize);
        this.outOfRange = outOfRange;
    }

    public Grid(final Class<T> clazz, final int xSize, final int ySize) {
        this(clazz, xSize, ySize, null);
    }

    public int getXSize() {
        return grid[0].length;
    }

    public int getYSize() {
        return grid.length;
    }

    public void setAt(final Position position, final T element) {
        setAt(position.getX(), position.getY(), element);
    }

    public void setAt(final int x, final int y, final T element) {
        grid[y][x] = element;
    }

    public T getAt(final Position position) {
        return getAt(position.getX(), position.getY());
    }

    public T getAt(final int x, final int y) {
        return isInBounds(x, y) ? grid[y][x] : outOfRange;
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
        for (final T[] row : grid) {
            for (final T element : row) {
                builder.append(element.toPrintableChar());
            }
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public Stream<T> stream() {
        return Arrays.stream(grid).flatMap(Arrays::stream);
    }

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }
}
