package ch.faetzminator.aocutil.map;

import java.util.function.Function;

import ch.faetzminator.aocutil.CharPrintable;

public class PMapWithStart<T extends CharPrintable> extends PMap<T> {

    private final Function<T, Boolean> findStart;
    private Position startPosition;

    public PMapWithStart(final Class<T> clazz, final int xSize, final int ySize, final Function<T, Boolean> findStart) {
        super(clazz, xSize, ySize);
        this.findStart = findStart;
    }

    @Override
    public void setElementAt(final int x, final int y, final T element) {
        super.setElementAt(x, y, element);
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
        return getElementAt(getStartPosition());
    }
}
