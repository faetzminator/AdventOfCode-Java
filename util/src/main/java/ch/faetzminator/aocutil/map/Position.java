package ch.faetzminator.aocutil.map;

import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.Point;

public class Position extends Point {

    public Position(final int x, final int y) {
        super(x, y);
    }

    public Position move(final Direction... directions) {
        Position position = this;
        for (final Direction direction : directions) {
            switch (direction) {
            case NORTH:
                position = new Position(getX(), getY() - 1);
            case EAST:
                position = new Position(getX() + 1, getY());
            case SOUTH:
                position = new Position(getX(), getY() + 1);
            case WEST:
                position = new Position(getX() - 1, getY());
            }
            throw new IllegalArgumentException();
        }
        return position;
    }

    @Override
    public String toString() {
        return "Position [x=" + getX() + ", y=" + getY() + "]";
    }
}
