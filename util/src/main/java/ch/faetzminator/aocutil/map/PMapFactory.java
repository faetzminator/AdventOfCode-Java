package ch.faetzminator.aocutil.map;

import java.util.List;
import java.util.function.Function;

import ch.faetzminator.aocutil.CharPrintable;

public class PMapFactory<T extends CharPrintable> {

    private final Class<T> clazz;
    private final ElementFactory<T> factory;

    public PMapFactory(final Class<T> clazz, final ElementFactory<T> factory) {
        this.clazz = clazz;
        this.factory = factory;
    }

    protected void fill(final PMap<T> map, final List<String> input) {
        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                final Position position = new Position(x, y);
                map.setElementAt(position, factory.create(line.charAt(x), position));
            }
        }
    }

    public PMap<T> create(final List<String> input) {
        final PMap<T> map = new PMap<>(clazz, input.get(0).length(), input.size());
        fill(map, input);
        return map;
    }

    public PMapWithStart<T> create(final List<String> input, final Function<T, Boolean> findStart) {
        final PMapWithStart<T> map = new PMapWithStart<>(clazz, input.get(0).length(), input.size(), findStart);
        fill(map, input);
        return map;
    }

    public static interface ElementFactory<T extends CharPrintable> {
        public T create(char character, Position position);
    }
}
