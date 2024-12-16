package ch.faetzminator.aocutil.grid;

import java.util.List;
import java.util.function.Function;

import ch.faetzminator.aocutil.CharPrintable;

public class GridFactory<T extends CharPrintable> {

    private final Class<T> clazz;
    private final ElementFactory<T> factory;

    public GridFactory(final Class<T> clazz, final ElementFactory<T> factory) {
        this.clazz = clazz;
        this.factory = factory;
    }

    protected void fill(final Grid<T> grid, final List<String> input) {
        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                final Position position = new Position(x, y);
                grid.setAt(position, factory.create(line.charAt(x), position));
            }
        }
    }

    public Grid<T> create(final List<String> input) {
        final Grid<T> grid = new Grid<>(clazz, input.get(0).length(), input.size());
        fill(grid, input);
        return grid;
    }

    public GridWithStart<T> create(final List<String> input, final Function<T, Boolean> findStart) {
        final GridWithStart<T> grid = new GridWithStart<>(clazz, input.get(0).length(), input.size(), findStart);
        fill(grid, input);
        return grid;
    }

    public static interface ElementFactory<T extends CharPrintable> {
        public T create(char character, Position position);
    }
}
