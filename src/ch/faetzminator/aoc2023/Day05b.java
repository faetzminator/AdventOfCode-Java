package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05b {

    public static void main(String[] args) {
        Day05b puzzle = new Day05b();

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
                if ("EOF".equals(line)) {
                    break;
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
        System.out.println("Solution: " + puzzle.getSeedRangeWithLowestLocation().getStart());
    }

    private Set<SeedRange> seedRanges = new HashSet<>();
    private Set<SeedRange> processedSeedRanges = new HashSet<>();

    public void addSeeds(String line) {
        Matcher matcher = Pattern.compile("seeds: (\\d+.*\\d+)").matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        String[] numbers = matcher.group(1).split(" ");
        for (int i = 0; i < numbers.length; i += 2) {
            long start = Long.parseLong(numbers[i]);
            long end = start + Long.parseLong(numbers[i + 1]) - 1;
            seedRanges.add(new SeedRange(start, end));
        }
    }

    public SeedRange getSeedRangeWithLowestLocation() {
        SeedRange lowest = seedRanges.iterator().next();
        for (SeedRange seedRange : seedRanges) {
            if (seedRange.getStart() < lowest.getStart()) {
                lowest = seedRange;
            }
        }
        return lowest;
    }

    public void clear() {
        seedRanges.addAll(processedSeedRanges);
        processedSeedRanges.clear();
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

        List<SeedRange> processed = new ArrayList<>();
        List<SeedRange> newItems = new ArrayList<>();
        for (SeedRange seedRange : seedRanges) {
            long start = seedRange.getStart();
            long end = seedRange.getEnd();

            // crappy if/else can be cleaned for sure
            if (sourceStart <= start && sourceEnd >= end) {
                seedRange.move(destStart - sourceStart);
                processed.add(seedRange);
            } else if (sourceStart >= start && sourceStart <= end) {
                if (sourceStart > start) {
                    newItems.add(seedRange);
                    seedRange = seedRange.split(sourceStart);
                }
                seedRange.move(destStart - sourceStart);
                processed.add(seedRange);
            } else if (sourceEnd <= end && sourceEnd >= start) {
                if (sourceEnd < end) {
                    newItems.add(seedRange.split(sourceEnd + 1));
                }
                seedRange.move(destStart - sourceStart);
                processed.add(seedRange);
            }
        }
        processedSeedRanges.addAll(newItems);
        seedRanges.removeAll(processed);
        // the below doesn't make sense as per the puzzle, does it? I would rather put
        // it into the seedRanges for further processing
        processedSeedRanges.addAll(processed);
    }

    public class SeedRange {

        long start;
        long end;

        public SeedRange(long start, long end) {
            if (end < start) {
                throw new IllegalArgumentException("start " + start + " > end " + end);
            }
            this.start = start;
            this.end = end;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public void move(long by) {
            start += by;
            end += by;
        }

        public SeedRange split(long upperStart) {
            SeedRange upperRange = new SeedRange(upperStart, end);
            end = upperStart - 1;
            return upperRange;
        }
    }
}
