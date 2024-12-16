package ch.faetzminator.aocutil.grid;

import java.util.function.Function;

import ch.faetzminator.aocutil.CharPrintable;

public class GridWithStart<T extends CharPrintable> extends Grid<T> {

    private final Function<T, Boolean> findStart;
    private Position startPosition;

    public GridWithStart(final Class<T> clazz, final int xSize, final int ySize, final Function<T, Boolean> findStart) {
        super(clazz, xSize, ySize);
        this.findStart = findStart;
    }

    @Override
    public void setAt(final int x, final int y, final T element) {
        super.setAt(x, y, element);
        if (startPosition != null && startPosition.getX() == x && startPosition.getY() == y) {
            startPosition = null;
        }
        if (findStart.apply(element)) {
            if (startPosition != null) {
                throw new IllegalArgumentException("duplicate start");
            }
            startPosition = new Position(x, y);
        }
    }

    public Position getStartPosition() {
        if (startPosition == null) {
            throw new IllegalArgumentException("start not set");
        }
        return startPosition;
    }

    public T getStartElement() {
        return getAt(getStartPosition());
    }
}
