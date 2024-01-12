package ch.faetzminator.aocutil.map;

import java.util.Objects;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.Direction;

public class ElementAtPositionWithDirection<T extends ElementAtPosition<? extends CharPrintable>>
        implements CharPrintable {

    private final T elementAtPosition;
    private final Direction direction;

    public ElementAtPositionWithDirection(final T elementAtPosition, final Direction direction) {
        this.elementAtPosition = elementAtPosition;
        this.direction = direction;
    }

    public T getElementAtPosition() {
        return elementAtPosition;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public char toPrintableChar() {
        return elementAtPosition.toPrintableChar();
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, elementAtPosition);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final ElementAtPositionWithDirection<?> other = (ElementAtPositionWithDirection<?>) obj;
        return direction == other.direction && Objects.equals(elementAtPosition, other.elementAtPosition);
    }

    @Override
    public String toString() {
        return "ElementAtPositionWithDirection [elementAtPosition=" + elementAtPosition + ", direction=" + direction
                + "]";
    }
}
