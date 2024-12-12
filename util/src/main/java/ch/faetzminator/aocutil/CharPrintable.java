package ch.faetzminator.aocutil;

public interface CharPrintable {

    char toPrintableChar();

    default char charValue() {
        return toPrintableChar();
    }
}
