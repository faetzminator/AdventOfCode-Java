package ch.faetzminator.aocutil.grid;

import java.util.List;
import java.util.function.Function;

import ch.faetzminator.aocutil.CharPrintable;

public class GridFactory<G extends Grid<T>, T extends CharPrintable> {

    private final Class<T> clazz;
    private final GridFactory_<G, T> gridFactory;
    private final ElementFactory<T> factory;

    @SuppressWarnings("unchecked")
    public GridFactory(final Class<T> clazz, final ElementFactory<T> factory) {
        this(clazz, (xSize, ySize) -> (G) new Grid<T>(clazz, xSize, ySize), factory);
    }

    public GridFactory(final Class<T> clazz, final GridFactory_<G, T> gridFactory, final ElementFactory<T> factory) {
        this.clazz = clazz;
        this.gridFactory = gridFactory;
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

    public G create(final List<String> input) {
        final G grid = gridFactory.create(input.get(0).length(), input.size());
        fill(grid, input);
        return grid;
    }

    public G create(final int xSize, final int ySize) {
        final G grid = gridFactory.create(xSize, ySize);
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                final Position position = new Position(x, y);
                grid.setAt(position, factory.create((char) 0, position));
            }
        }
        return grid;
    }

    public GridWithStart<T> create(final List<String> input, final Function<T, Boolean> findStart) {
        return create(input, findStart, null);
    }

    public GridWithStart<T> create(final List<String> input, final Function<T, Boolean> findStart,
            final Function<T, Boolean> findEnd) {
        final GridWithStart<T> grid = new GridWithStart<>(clazz, input.get(0).length(), input.size(), findStart,
                findEnd);
        fill(grid, input);
        return grid;
    }

    public static interface ElementFactory<T extends CharPrintable> {
        public T create(char character, Position position);
    }

    public static interface GridFactory_<G extends Grid<T>, T extends CharPrintable> {
        public G create(final int xSize, final int ySize);
    }
}
