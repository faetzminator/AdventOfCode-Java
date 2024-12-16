package ch.faetzminator.aocutil.grid;

import java.util.function.Function;

import ch.faetzminator.aocutil.CharPrintable;

public class GridWithStart<T extends CharPrintable> extends Grid<T> {

    private final Function<T, Boolean> findStart;
    private final Function<T, Boolean> findEnd;
    private Position startPosition;
    private Position endPosition;

    public GridWithStart(final Class<T> clazz, final int xSize, final int ySize, final Function<T, Boolean> findStart) {
        this(clazz, xSize, ySize, findStart, null);
    }

    public GridWithStart(final Class<T> clazz, final int xSize, final int ySize, final Function<T, Boolean> findStart,
            final Function<T, Boolean> findEnd) {

        super(clazz, xSize, ySize);
        this.findStart = findStart;
        this.findEnd = findEnd;
    }

    @Override
    public void setAt(final int x, final int y, final T element) {
        super.setAt(x, y, element);
        if (startPosition != null && startPosition.getX() == x && startPosition.getY() == y) {
            startPosition = null;
        }
        if (endPosition != null && endPosition.getX() == x && endPosition.getY() == y) {
            endPosition = null;
        }
        if (findStart.apply(element)) {
            if (startPosition != null) {
                throw new IllegalArgumentException("duplicate start");
            }
            startPosition = new Position(x, y);
        }
        if (findEnd != null && findEnd.apply(element)) {
            if (endPosition != null) {
                throw new IllegalArgumentException("duplicate end");
            }
            endPosition = new Position(x, y);
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

    public Position getEndPosition() {
        if (endPosition == null) {
            throw new IllegalArgumentException("end not set");
        }
        return endPosition;
    }

    public T getEndElement() {
        return getAt(getEndPosition());
    }
}
