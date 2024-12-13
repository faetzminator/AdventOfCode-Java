package ch.faetzminator.aocutil.map;

import ch.faetzminator.aocutil.CharEnum;

public abstract class CharEnumAtPosition<T extends CharEnum> extends ElementAtPosition<T> {

    public CharEnumAtPosition(final T element, final Position position) {
        super(element, position);
    }

    public char charValue() {
        return getElement().charValue();
    }
}
