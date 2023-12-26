package ch.faetzminator.aoc2023;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day06b {

    public static void main(final String[] args) {
        final Day06b puzzle = new Day06b();

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
        final long parsedTime = Long.parseLong(time.replaceAll("\\D", ""));
        final long parsedDistance = Long.parseLong(distance.replaceAll("\\D", ""));
        races = Collections.singletonList(new Race(parsedTime, parsedDistance));
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
