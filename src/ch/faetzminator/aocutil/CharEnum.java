package ch.faetzminator.aocutil;

public interface CharEnum extends CharPrintable {

    char getCharacter();

    @Override
    default char toPrintableChar() {
        return getCharacter();
    }

    static <T extends Enum<T> & CharEnum> T byChar(final Class<T> clazz, final char c) {
        for (final T item : clazz.getEnumConstants()) {
            if (item.getCharacter() == c) {
                return item;
            }
        }
        throw new IllegalArgumentException("element of " + clazz.getSimpleName() + " not found for " + c);
    }
}
