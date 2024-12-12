package ch.faetzminator.aocutil.map;

import java.util.Objects;

import ch.faetzminator.aocutil.CharPrintable;

public class ElementAtPosition<T extends CharPrintable> implements CharPrintable {

    private final T element;
    private final Position position;

    public ElementAtPosition(final T element, final Position position) {
        this.element = element;
        this.position = position;
    }

    public T getElement() {
        return element;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public char charValue() {
        return element.charValue();
    }

    @Override
    public char toPrintableChar() {
        return element.toPrintableChar();
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    /**
     * Note <code>T element</code> needs to implement hashCode and equals properly!
     */
    @Override
    public int hashCode() {
        return Objects.hash(element, position);
    }

    /**
     * Note <code>T element</code> needs to implement hashCode and equals properly!
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final ElementAtPosition<?> other = (ElementAtPosition<?>) obj;
        return Objects.equals(element, other.element) && Objects.equals(position, other.position);
    }

    @Override
    public String toString() {
        return "ElementAtPosition [element=" + element + ", position=" + position + "]";
    }
}
