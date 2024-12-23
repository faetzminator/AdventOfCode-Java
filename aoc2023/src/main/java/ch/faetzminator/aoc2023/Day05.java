package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day05 {

    public static void main(final String[] args) {
        final Day05 puzzle = new Day05();

        final String seeds;
        final List<List<String>> input;

        try (Scanner scanner = new Scanner(System.in)) {
            seeds = ScannerUtil.readNonBlankLine(scanner);
            ScannerUtil.readBlankLine(scanner);
            input = ScannerUtil.readNonBlankLinesBlocks(scanner);
        }

        final Timer timer = PuzzleUtil.start();
        puzzle.addSeeds(seeds);
        // TODO refactor to puzzle class
        for (final List<String> lines : input) {
            // ignore line 0 (header string)
            for (int i = 1; i < lines.size(); i++) {
                puzzle.move(lines.get(i));
            }
            puzzle.clear();
        }
        final long solution = puzzle.getSeedWithLowestLocation();
        PuzzleUtil.end(solution, timer);
    }

    private final Set<Seed> seeds = new HashSet<>();
    private final Set<Seed> processedSeeds = new HashSet<>();

    public void addSeeds(final String line) {
        final Matcher matcher = Pattern.compile("seeds: (\\d+.*\\d+)").matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        for (final String number : matcher.group(1).split(" ")) {
            seeds.add(new Seed(Long.parseLong(number)));
        }
    }

    public long getSeedWithLowestLocation() {
        Seed lowest = seeds.iterator().next();
        for (final Seed seed : seeds) {
            if (seed.getLocation() < lowest.getLocation()) {
                lowest = seed;
            }
        }
        return lowest.getLocation();
    }

    public void clear() {
        seeds.addAll(processedSeeds);
        processedSeeds.clear();
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+) (\\d+) (\\d+)");

    public void move(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final long destStart = Long.parseLong(matcher.group(1));
        final long sourceStart = Long.parseLong(matcher.group(2));
        final long sourceEnd = sourceStart + Long.parseLong(matcher.group(3)) - 1;

        final List<Seed> processed = new ArrayList<>();
        for (final Seed seed : seeds) {
            if (seed.getLocation() >= sourceStart && seed.getLocation() <= sourceEnd) {
                seed.move(destStart - sourceStart);
                processed.add(seed);
            }
        }
        processedSeeds.addAll(processed);
        seeds.removeAll(processed);
    }

    private static class Seed {

        // as learned in part B, location not necessary at all
        private long location;

        public Seed(final long number) {
            location = number;
        }

        public long getLocation() {
            return location;
        }

        public void move(final long by) {
            location += by;
        }
    }
}
