package ch.faetzminator.aocutil;

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
    public char toPrintableChar() {
        return element.toPrintableChar();
    }

    @Override
    public String toString() {
        return "ElementAtPosition [element=" + element + ", position=" + position + "]";
    }
}
