package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day11b {

    public static void main(String[] args) {
        Day11b puzzle = new Day11b();

        List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        puzzle.parseMap(input);
        puzzle.calculateDistance();
        System.out.println("Solution: " + puzzle.getDistanceSum());
    }

    private static final char GALAXY = '#';
    private static final int EXPANSION = 1000000 - 1;

    private boolean[][] map;
    private boolean[] expandX;
    private boolean[] expandY;
    private long distanceSum;

    public void parseMap(List<String> lines) {
        map = new boolean[lines.size()][];
        expandX = new boolean[lines.get(0).length()];
        expandY = new boolean[lines.size()];
        for (int x = 0; x < expandX.length; x++) {
            expandX[x] = true;
        }
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            map[y] = new boolean[line.length()];
            expandY[y] = true;
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == GALAXY) {
                    map[y][x] = true;
                    expandX[x] = expandY[y] = false;
                }
            }
        }
    }

    public void calculateDistance() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x]) {
                    addDistances(x, y);
                }
            }
        }
    }

    private void addDistances(int fromX, int fromY) {
        int[][] distances = getSouthernDistances(fromX, fromY);
        for (int y = fromY; y < map.length; y++) {
            for (int x = y == fromY ? fromX + 1 : 0; x < map[y].length; x++) {
                if (map[y][x]) {
                    distanceSum += distances[y][x];
                }
            }
        }
    }

    private int[][] getSouthernDistances(int fromX, int fromY) {
        // I really hoped A* is needed in part B!!1! ;)
        int[][] dist = new int[map.length][map[0].length];
        for (int y = fromY; y < dist.length; y++) {
            for (int x = 0; x < dist[y].length; x++) {
                dist[y][x] = -1;
            }
        }
        dist[fromY][fromX] = 0;
        Queue<Position> toProcess = new LinkedList<>();
        toProcess.add(new Position(fromX, fromY));
        while (!toProcess.isEmpty()) {
            Position pos = toProcess.poll();
            int value = dist[pos.getY()][pos.getX()] + 1;
            if (pos.getX() > 0 && dist[pos.getY()][pos.getX() - 1] == -1) {
                dist[pos.getY()][pos.getX() - 1] = value + (expandX[pos.getX() - 1] ? EXPANSION : 0);
                toProcess.add(new Position(pos.getX() - 1, pos.getY()));
            }
            if ((pos.getX() + 1 < dist[pos.getY()].length) && dist[pos.getY()][pos.getX() + 1] == -1) {
                dist[pos.getY()][pos.getX() + 1] = value + (expandX[pos.getX()] ? EXPANSION : 0);
                toProcess.add(new Position(pos.getX() + 1, pos.getY()));
            }
            if ((pos.getY() + 1 < dist.length) && dist[pos.getY() + 1][pos.getX()] == -1) {
                dist[pos.getY() + 1][pos.getX()] = value + (expandY[pos.getY()] ? EXPANSION : 0);
                toProcess.add(new Position(pos.getX(), pos.getY() + 1));
            }
        }
        return dist;
    }

    public long getDistanceSum() {
        return distanceSum;
    }

    public class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
