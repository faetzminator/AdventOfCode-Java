package ch.faetzminator.aoc2023;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.Position;

public class Day11b {

    public static void main(final String[] args) {
        final Day11b puzzle = new Day11b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseMap(lines);
        puzzle.calculateDistance();
        final long solution = puzzle.getDistanceSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final char GALAXY = '#';
    private static final int EXPANSION = 1000000 - 1;

    private boolean[][] map;
    private boolean[] expandX;
    private boolean[] expandY;
    private long distanceSum;

    public void parseMap(final List<String> lines) {
        map = new boolean[lines.size()][];
        expandX = new boolean[lines.get(0).length()];
        expandY = new boolean[lines.size()];
        for (int x = 0; x < expandX.length; x++) {
            expandX[x] = true;
        }
        for (int y = 0; y < lines.size(); y++) {
            final String line = lines.get(y);
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
        // I really hoped A* is needed in part B!!1! ;)
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
}
