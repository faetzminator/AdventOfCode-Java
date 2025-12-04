package ch.faetzminator.aocutil.grid;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.faetzminator.aocutil.Direction;

public class PositionUtil {

    public static Collection<Position> adjacent4(final Position position) {
        return Arrays.stream(Direction.values()).map(direction -> position.move(direction))
                .collect(Collectors.toList());
    }

    public static Collection<Position> adjacent8(final Position position) {
        return Arrays.stream(Direction.values()).flatMap(direction -> {
            final Position first = position.move(direction);
            return Stream.of(first, first.move(direction.getClockwise()));
        }).collect(Collectors.toList());
    }
}
