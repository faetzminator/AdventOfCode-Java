package ch.faetzminator.aocutil;

public interface CharEnum extends CharPrintable {

    char charValue();

    @Override
    default char toPrintableChar() {
        return charValue();
    }

    static <T extends Enum<T> & CharEnum> T byChar(final Class<T> clazz, final char value) {
        for (final T item : clazz.getEnumConstants()) {
            if (item.charValue() == value) {
                return item;
            }
        }
        throw new IllegalArgumentException("element of " + clazz.getSimpleName() + " not found for " + value);
    }
}
