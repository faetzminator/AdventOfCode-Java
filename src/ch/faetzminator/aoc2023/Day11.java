package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day11 {

    public static void main(String[] args) {
        Day11 puzzle = new Day11();

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

    private boolean map[][];
    private long distanceSum;

    public void parseMap(List<String> lines) {
        boolean[] hasGalaxy = new boolean[lines.get(0).length()];
        int expandXBy = 0, expandYBy = 0;
        for (String line : lines) {
            boolean galaxyInLine = false;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#') {
                    hasGalaxy[i] = true;
                    galaxyInLine = true;
                }
            }
            if (!galaxyInLine) {
                expandYBy++;
            }
        }
        for (boolean b : hasGalaxy) {
            if (!b) {
                expandXBy++;
            }
        }
        map = new boolean[lines.size() + expandYBy][];
        for (int l = 0, lo = 0; l < lines.size(); l++, lo++) {
            String line = lines.get(l);
            map[lo] = new boolean[line.length() + expandXBy];
            boolean galaxyInLine = false;
            for (int i = 0, o = 0; i < line.length(); i++, o++) {
                if (line.charAt(i) == '#') {
                    map[lo][o] = true;
                    galaxyInLine = true;
                }
                if (!hasGalaxy[i]) {
                    o++;
                }
            }
            if (!galaxyInLine) {
                map[++lo] = new boolean[line.length() + expandXBy];
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
                dist[pos.getY()][pos.getX() - 1] = value;
                toProcess.add(new Position(pos.getX() - 1, pos.getY()));
            }
            if ((pos.getX() + 1 < dist[pos.getY()].length) && dist[pos.getY()][pos.getX() + 1] == -1) {
                dist[pos.getY()][pos.getX() + 1] = value;
                toProcess.add(new Position(pos.getX() + 1, pos.getY()));
            }
            if ((pos.getY() + 1 < dist.length) && dist[pos.getY() + 1][pos.getX()] == -1) {
                dist[pos.getY() + 1][pos.getX()] = value;
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
