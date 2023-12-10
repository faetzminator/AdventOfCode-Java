package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 {

    public static void main(String[] args) {
        Day02 puzzle = new Day02();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        for (String line : input) {
            puzzle.playGame(line);
        }
        System.out.println("Solution: " + puzzle.getGameSum());
    }

    private long gameSum;

    public void playGame(String str) {
        Game game = parseGame(str);
        if (game.getRed() <= 12 && game.getGreen() <= 13 && game.getBlue() <= 14) {
            gameSum += game.getNumber();
        }
    }

    public long getGameSum() {
        return gameSum;
    }

    Pattern linePattern = Pattern.compile("Game (\\d+): (.*)");

    public Game parseGame(String line) {
        Matcher matcher = linePattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }

        Game game = new Game(Integer.parseInt(matcher.group(1)));
        String[] games = matcher.group(2).split("; ");
        for (String single : games) {
            String[] colors = single.split(", ");
            for (String color : colors) {
                String[] values = color.split(" ");
                int value = Integer.parseInt(values[0]);
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

    class Game {

        final int number;

        int red = 0;
        int green = 0;
        int blue = 0;

        public Game(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setMaxRed(int r) {
            if (red < r) {
                red = r;
            }
        }

        public void setMaxGreen(int g) {
            if (green < g) {
                green = g;
            }
        }

        public void setMaxBlue(int b) {
            if (blue < b) {
                blue = b;
            }
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }
    }
}
