package ch.faetzminator.aocutil;

import java.lang.reflect.Array;

/**
 * PMap = <i>Puzzle</i> Map to not conflict with java.util.Map.
 */
public class PMap<T extends CharPrintable> {

    private final T[][] map;

    @SuppressWarnings("unchecked")
    public PMap(final Class<T> clazz, final int xSize, final int ySize) {
        map = (T[][]) Array.newInstance(clazz, ySize, xSize);
    }

    public int getXSize() {
        return map[0].length;
    }

    public int getYSize() {
        return map.length;
    }

    public void setElementAt(final Position position, final T element) {
        setElementAt(position.getX(), position.getY(), element);
    }

    public void setElementAt(final int x, final int y, final T element) {
        map[y][x] = element;
    }

    public T getElementAt(final Position position) {
        return getElementAt(position.getX(), position.getY());
    }

    public T getElementAt(final int x, final int y) {
        return isInBounds(x, y) ? map[y][x] : null;
    }

    public boolean isInBounds(final Position position) {
        return isInBounds(position.getX(), position.getY());
    }

    public boolean isInBounds(final int x, final int y) {
        return x >= 0 && y >= 0 && x < map[0].length && y < map.length;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final T[] row : map) {
            for (final T element : row) {
                builder.append(element.toPrintableChar());
            }
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
