package ch.faetzminator.aocutil;

public enum Direction implements CharEnum {

    NORTH('N'), EAST('E'), SOUTH('S'), WEST('W');

    private final char label;

    Direction(final char c) {
        this.label = c;
    }

    public Direction getClockwise() {
        switch (this) {
        case NORTH:
            return EAST;
        case EAST:
            return SOUTH;
        case SOUTH:
            return WEST;
        case WEST:
            return NORTH;
        }
        throw new IllegalArgumentException();
    }

    public Direction getOpposite() {
        return getClockwise().getClockwise();
    }

    public Direction getCounterclockwise() {
        return getClockwise().getOpposite();
    }

    @Override
    public char charValue() {
        return label;
    }

    public static Direction byChar(final char c) {
        return CharEnum.byChar(Direction.class, c);
    }
}
