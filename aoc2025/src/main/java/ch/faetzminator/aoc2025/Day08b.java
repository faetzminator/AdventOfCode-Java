package ch.faetzminator.aoc2025;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.Groups;
import ch.faetzminator.aocutil.Pair;
import ch.faetzminator.aocutil.Point3D;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day08b {

    public static void main(final String[] args) {
        final Day08b puzzle = new Day08b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+),(\\d+)");

    private final List<Point3D> junctionBoxes = new ArrayList<>();

    public void parseLine(final String input) {
        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final int x = Integer.parseInt(matcher.group(1));
        final int y = Integer.parseInt(matcher.group(2));
        final int z = Integer.parseInt(matcher.group(3));
        junctionBoxes.add(new Point3D(x, y, z));
    }

    public long getSolution() {
        // get distances in order, by points
        final Map<Double, Pair<Point3D>> distancesMap = getDistancesMap(junctionBoxes);
        final List<Double> distances = new ArrayList<>(distancesMap.keySet());
        Collections.sort(distances);

        // merge groups together
        final Groups<Point3D> groups = new Groups<>(junctionBoxes);
        for (final Double distance : distances) {
            final Pair<Point3D> points = distancesMap.get(distance);
            if (groups.merge(points.getLeft(), points.getRight()).size() == junctionBoxes.size()) {
                return ((long) points.getLeft().getX()) * points.getRight().getX();
            }
        }
        throw new RuntimeException();
    }

    private Map<Double, Pair<Point3D>> getDistancesMap(final List<Point3D> points) {
        final Map<Double, Pair<Point3D>> distances = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                final Double dist = points.get(i).getDistance(points.get(j));
                if (distances.containsKey(dist)) { // wouldn't work in this puzzle
                    throw new RuntimeException("for " + dist);
                }
                distances.put(dist, new Pair<>(points.get(i), points.get(j)));
            }
        }
        return distances;
    }
}
