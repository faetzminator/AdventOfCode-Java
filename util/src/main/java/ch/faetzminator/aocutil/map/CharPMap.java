package ch.faetzminator.aocutil.map;

/**
 * See {@link PMap}.
 */
public class CharPMap {

    private final char[][] map;
    private final char outOfRange;

    public CharPMap(final int xSize, final int ySize, final char outOfRange) {
        map = new char[ySize][xSize];
        this.outOfRange = outOfRange;
    }

    public CharPMap(final int xSize, final int ySize) {
        this(xSize, ySize, (char) 0);
    }

    public int getXSize() {
        return map[0].length;
    }

    public int getYSize() {
        return map.length;
    }

    public void setElementAt(final Position position, final char element) {
        setElementAt(position.getX(), position.getY(), element);
    }

    public void setElementAt(final int x, final int y, final char element) {
        map[y][x] = element;
    }

    public char getElementAt(final Position position) {
        return getElementAt(position.getX(), position.getY());
    }

    public char getElementAt(final int x, final int y) {
        return isInBounds(x, y) ? map[y][x] : outOfRange;
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
        for (final char[] row : map) {
            for (final char element : row) {
                builder.append(element);
            }
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
