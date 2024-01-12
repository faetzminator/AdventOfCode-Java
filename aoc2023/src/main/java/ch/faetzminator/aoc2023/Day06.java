package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day06 {

    public static void main(final String[] args) {
        final Day06 puzzle = new Day06();

        final String time, distance;
        try (Scanner scanner = new Scanner(System.in)) {
            time = ScannerUtil.readNonBlankLine(scanner);
            distance = ScannerUtil.readNonBlankLine(scanner);
        }
        final Timer timer = PuzzleUtil.start();
        puzzle.parseInput(time, distance);
        final long solution = puzzle.calculate();
        PuzzleUtil.end(solution, timer);
    }

    private List<Race> races;

    public void parseInput(final String time, final String distance) {
        final Pattern linePattern = Pattern.compile(".*?: +(.*)");
        final Matcher timeMatcher = linePattern.matcher(time);
        final Matcher distanceMatcher = linePattern.matcher(distance);
        if (!timeMatcher.matches() || !distanceMatcher.matches()) {
            throw new IllegalArgumentException();
        }
        final String[] times = timeMatcher.group(1).split(" +");
        final String[] distances = distanceMatcher.group(1).split(" +");

        races = new ArrayList<>(times.length);
        for (int i = 0; i < times.length; i++) {
            races.add(new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i])));
        }
    }

    public long calculate() {
        long product = 1;
        for (final Race race : races) {
            product *= calculateWays(race);
        }
        return product;
    }

    private long calculateWays(final Race race) {
        long ways = 0;
        final long startTimeToPress = (long) Math.sqrt(race.getRecordDistance());
        for (long timeToPress = startTimeToPress; timeToPress < race.getTime(); timeToPress++) {
            if (timeToPress * (race.getTime() - timeToPress) > race.getRecordDistance()) {
                ways++;
            } else {
                break;
            }
        }
        for (long timeToPress = startTimeToPress - 1; timeToPress > 0; timeToPress--) {
            if (timeToPress * (race.getTime() - timeToPress) > race.getRecordDistance()) {
                ways++;
            } else {
                break;
            }
        }
        return ways;
    }

    private static class Race {

        private final long time;
        private final long recordDistance;

        public Race(final long time, final long recordDistance) {
            this.time = time;
            this.recordDistance = recordDistance;
        }

        public long getTime() {
            return time;
        }

        public long getRecordDistance() {
            return recordDistance;
        }
    }
}
