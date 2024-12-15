package ch.faetzminator.aoc2024;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day15Test extends PuzzleTest {

    @Test
    public void example() {
        final String map = "##########\n" + "#..O..O.O#\n" + "#......O.#\n" + "#.OO..O.O#\n" + "#..O@..O.#\n"
                + "#O#..O...#\n" + "#O..O..O.#\n" + "#.OO.O.OO#\n" + "#....O...#\n" + "##########";
        final String moves = "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^\n"
                + "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v\n"
                + "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<\n"
                + "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^\n"
                + "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><\n"
                + "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^\n"
                + ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^\n"
                + "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>\n"
                + "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>\n"
                + "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^";

        final Day15 puzzle = new Day15();
        puzzle.parseMap(toList(map));
        puzzle.parseMoves(toList(moves));
        puzzle.move();
        Assertions.assertEquals(10092L, puzzle.getCoordinatesSum());
    }

    @Test
    public void smallerExample() {
        final String map = "########\n#..O.O.#\n##@.O..#\n#...O..#\n#.#.O..#\n#...O..#\n#......#\n########";
        final String moves = "<^^>>>vv<v>>v<<";

        final Day15 puzzle = new Day15();
        puzzle.parseMap(toList(map));
        puzzle.parseMoves(toList(moves));
        puzzle.move();
        Assertions.assertEquals(2028L, puzzle.getCoordinatesSum());
    }
}
