package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {

    public static void main(String[] args) {
        Day5 puzzle = new Day5();

        List<List<String>> input = new ArrayList<>();
        String seeds;

        try (Scanner scanner = new Scanner(System.in)) {
            seeds = scanner.nextLine(); // special handling

            String line;
            Pattern numericStart = Pattern.compile("^\\d");
            boolean newNeeded = true;
            List<String> subInput = new ArrayList<>();
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (!line.isEmpty()) {
                    if (numericStart.matcher(line).find()) {
                        if (newNeeded) {
                            subInput = new ArrayList<>();
                            input.add(subInput);
                            newNeeded = false;
                        }
                        subInput.add(line);
                    } else {
                        newNeeded = true;
                    }
                }
            }
            scanner.close();
        }

        System.out.println("Calculating...");
        puzzle.addSeeds(seeds);
        for (List<String> lines : input) {
            for (String line : lines) {
                puzzle.move(line);
            }
            puzzle.clear();
        }
        System.out.println("Solution: " + puzzle.getSeedWithLowestLocation().getLocation());
    }

    private Set<Seed> seeds = new HashSet<>();
    private Set<Seed> processedSeeds = new HashSet<>();

    public void addSeeds(String line) {
        Matcher matcher = Pattern.compile("seeds: (\\d+.*\\d+)").matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        for (String number : matcher.group(1).split(" ")) {
            seeds.add(new Seed(Long.parseLong(number)));
        }
    }

    public Seed getSeedWithLowestLocation() {
        Seed lowest = seeds.iterator().next();
        for (Seed seed : seeds) {
            if (seed.getLocation() < lowest.getLocation()) {
                lowest = seed;
            }
        }
        return lowest;
    }

    public void clear() {
        seeds.addAll(processedSeeds);
        processedSeeds.clear();
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+) (\\d+) (\\d+)");

    public void move(String line) {
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        long destStart = Long.parseLong(matcher.group(1));
        long sourceStart = Long.parseLong(matcher.group(2));
        long sourceEnd = sourceStart + Long.parseLong(matcher.group(3)) - 1;

        List<Seed> processed = new ArrayList<>();
        for (Seed seed : seeds) {
            if (seed.getLocation() >= sourceStart && seed.getLocation() <= sourceEnd) {
                seed.move(destStart - sourceStart);
                processed.add(seed);
            }
        }
        processedSeeds.addAll(processed);
        seeds.removeAll(processed);
    }

    public class Seed {

        final long number;
        // as learned in part B, location not necessary at all, jaja
        long location;

        public Seed(long number) {
            this.number = number;
            location = number;
        }

        public long getNumber() {
            return number;
        }

        public long getLocation() {
            return location;
        }

        public void move(long by) {
            location += by;
        }
    }
}
