package ch.faetzminator.aocutil;

public class Position extends Point {

    public Position(final int x, final int y) {
        super(x, y);
    }

    public Position move(final Direction direction) {
        switch (direction) {
        case NORTH:
            return new Position(getX(), getY() - 1);
        case EAST:
            return new Position(getX() + 1, getY());
        case SOUTH:
            return new Position(getX(), getY() + 1);
        case WEST:
            return new Position(getX() - 1, getY());
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return "Position [x=" + getX() + ", y=" + getY() + "]";
    }
}
