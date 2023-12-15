package ch.faetzminator.aoc2023;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day06b {

    public static void main(String[] args) {
        Day06b puzzle = new Day06b();

        String time, distance;
        try (Scanner scanner = new Scanner(System.in)) {
            time = scanner.nextLine();
            distance = scanner.nextLine();
        }

        System.out.println("Calculating...");
        puzzle.parseInput(time, distance);
        long product = puzzle.calculate();
        System.out.println("Solution: " + product);
    }

    private List<Race> races;

    public void parseInput(String time, String distance) {
        long parsedTime = Long.parseLong(time.replaceAll("\\D", ""));
        long parsedDistance = Long.parseLong(distance.replaceAll("\\D", ""));
        races = Collections.singletonList(new Race(parsedTime, parsedDistance));
    }

    public long calculate() {
        long product = 1;
        for (Race race : races) {
            product *= calculateWays(race);
        }
        return product;
    }

    public static long calculateWays(Race race) {
        long ways = 0;
        long startTimeToPress = (long) Math.sqrt(race.getRecordDistance());
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

    public class Race {

        final long time;
        final long recordDistance;

        public Race(long time, long recordDistance) {
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
