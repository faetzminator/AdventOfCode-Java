package ch.faetzminator.aocutil;

public class Char implements CharPrintable, Comparable<Char> {

    private final char value;

    public Char(final char value) {
        this.value = value;
    }

    public char charValue() {
        return value;
    }

    @Override
    public int compareTo(final Char other) {
        return value - other.charValue();
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Char other = (Char) obj;
        return value == other.value;
    }

    @Override
    public char toPrintableChar() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
