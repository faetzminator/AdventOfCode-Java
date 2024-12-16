package ch.faetzminator.aoc2023;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.Position;

public class Day11 {

    public static void main(final String[] args) {
        final Day11 puzzle = new Day11();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseMap(lines);
        puzzle.calculateDistance();
        final long solution = puzzle.getDistanceSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final char GALAXY = '#';

    private boolean[][] map;
    private long distanceSum;

    public void parseMap(final List<String> lines) {
        final boolean[] hasGalaxy = new boolean[lines.get(0).length()];
        int expandXBy = 0, expandYBy = 0;
        for (final String line : lines) {
            boolean galaxyInLine = false;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == GALAXY) {
                    hasGalaxy[i] = true;
                    galaxyInLine = true;
                }
            }
            if (!galaxyInLine) {
                expandYBy++;
            }
        }
        for (final boolean b : hasGalaxy) {
            if (!b) {
                expandXBy++;
            }
        }
        map = new boolean[lines.size() + expandYBy][];
        for (int l = 0, lo = 0; l < lines.size(); l++, lo++) {
            final String line = lines.get(l);
            map[lo] = new boolean[line.length() + expandXBy];
            boolean galaxyInLine = false;
            for (int i = 0, o = 0; i < line.length(); i++, o++) {
                if (line.charAt(i) == GALAXY) {
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

    private void addDistances(final int fromX, final int fromY) {
        final int[][] distances = getSouthernDistances(fromX, fromY);
        for (int y = fromY; y < map.length; y++) {
            for (int x = y == fromY ? fromX + 1 : 0; x < map[y].length; x++) {
                if (map[y][x]) {
                    distanceSum += distances[y][x];
                }
            }
        }
    }

    private int[][] getSouthernDistances(final int fromX, final int fromY) {
        final int[][] dist = new int[map.length][map[0].length];
        for (int y = fromY; y < dist.length; y++) {
            for (int x = 0; x < dist[y].length; x++) {
                dist[y][x] = -1;
            }
        }
        dist[fromY][fromX] = 0;
        final Queue<Position> toProcess = new LinkedList<>();
        toProcess.add(new Position(fromX, fromY));
        while (!toProcess.isEmpty()) {
            final Position pos = toProcess.poll();
            final int value = dist[pos.getY()][pos.getX()] + 1;
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
}
