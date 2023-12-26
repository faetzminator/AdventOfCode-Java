package ch.faetzminator.aoc2023;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day02b {

    public static void main(final String[] args) {
        final Day02b puzzle = new Day02b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.playGame(line);
        }
        final long solution = puzzle.getGamePower();
        PuzzleUtil.end(solution, timer);
    }

    private long gamePower;

    public void playGame(final String str) {
        final Game game = parseGame(str);
        gamePower += game.getPower();
    }

    public long getGamePower() {
        return gamePower;
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("Game (\\d+): (.*)");

    private Game parseGame(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }

        final Game game = new Game();
        final String[] games = matcher.group(2).split("; ");
        for (final String single : games) {
            final String[] colors = single.split(", ");
            for (final String color : colors) {
                final String[] values = color.split(" ");
                final int value = Integer.parseInt(values[0]);
                switch (values[1]) {
                case "red":
                    game.setMaxRed(value);
                    break;
                case "green":
                    game.setMaxGreen(value);
                    break;
                case "blue":
                    game.setMaxBlue(value);
                    break;
                default:
                    throw new IllegalArgumentException("color: " + values[1]);
                }
            }
        }
        return game;
    }

    private static class Game {

        int red = 0;
        int green = 0;
        int blue = 0;

        public void setMaxRed(final int r) {
            if (red < r) {
                red = r;
            }
        }

        public void setMaxGreen(final int g) {
            if (green < g) {
                green = g;
            }
        }

        public void setMaxBlue(final int b) {
            if (blue < b) {
                blue = b;
            }
        }

        public int getPower() {
            return red * green * blue;
        }
    }
}
