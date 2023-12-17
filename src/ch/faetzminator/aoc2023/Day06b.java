package ch.faetzminator.aoc2023;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day06b {

    public static void main(final String[] args) {
        final Day06b puzzle = new Day06b();

        String time, distance;
        try (Scanner scanner = new Scanner(System.in)) {
            time = scanner.nextLine();
            distance = scanner.nextLine();
        }

        System.out.println("Calculating...");
        puzzle.parseInput(time, distance);
        final long product = puzzle.calculate();
        System.out.println("Solution: " + product);
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
