package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {

    public static void main(String[] args) {
        Day6 puzzle = new Day6();

        String time, distance;
        try (Scanner scanner = new Scanner(System.in)) {
            time = scanner.nextLine();
            distance = scanner.nextLine();
            scanner.close();
        }

        System.out.println("Calculating...");
        puzzle.parseInput(time, distance);
        long product = puzzle.calculate();
        System.out.println("Solution: " + product);
    }

    private List<Race> races;

    public void parseInput(String time, String distance) {
        Pattern linePattern = Pattern.compile(".*?: +(.*)");
        Matcher timeMatcher = linePattern.matcher(time);
        Matcher distanceMatcher = linePattern.matcher(distance);
        if (!timeMatcher.matches() || !distanceMatcher.matches()) {
            throw new IllegalArgumentException();
        }
        String[] times = timeMatcher.group(1).split(" +");
        String[] distances = distanceMatcher.group(1).split(" +");

        races = new ArrayList<>(times.length);
        for (int i = 0; i < times.length; i++) {
            races.add(new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i])));
        }
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
