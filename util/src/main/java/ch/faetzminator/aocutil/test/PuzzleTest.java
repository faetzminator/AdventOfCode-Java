package ch.faetzminator.aocutil.test;

import java.util.Arrays;
import java.util.List;

public abstract class PuzzleTest {

    public List<String> toList(final String input) {
        return Arrays.asList(input.split("\\n"));
    }
}
