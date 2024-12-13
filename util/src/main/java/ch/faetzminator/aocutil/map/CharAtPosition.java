package ch.faetzminator.aocutil.map;

import ch.faetzminator.aocutil.Char;

public abstract class CharAtPosition<T extends Char> extends ElementAtPosition<T> {

    public CharAtPosition(final T element, final Position position) {
        super(element, position);
    }

    public char charValue() {
        return getElement().charValue();
    }
}
