package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.Range;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day05b {

    public static void main(final String[] args) {
        final Day05b puzzle = new Day05b();

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
        final long solution = puzzle.getSeedStartWithLowestLocation();
        PuzzleUtil.end(solution, timer);
    }

    private final Set<Range> seedRanges = new HashSet<>();
    private final Set<Range> processedSeedRanges = new HashSet<>();

    public void addSeeds(final String line) {
        final Matcher matcher = Pattern.compile("seeds: (\\d+.*\\d+)").matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final String[] numbers = matcher.group(1).split(" ");
        for (int i = 0; i < numbers.length; i += 2) {
            final long start = Long.parseLong(numbers[i]);
            final long end = start + Long.parseLong(numbers[i + 1]) - 1;
            seedRanges.add(new Range(start, end));
        }
    }

    public long getSeedStartWithLowestLocation() {
        Range lowest = seedRanges.iterator().next();
        for (final Range seedRange : seedRanges) {
            if (seedRange.getStart() < lowest.getStart()) {
                lowest = seedRange;
            }
        }
        return lowest.getStart();
    }

    public void clear() {
        seedRanges.addAll(processedSeedRanges);
        processedSeedRanges.clear();
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

        final List<Range> processed = new ArrayList<>();
        final List<Range> newItems = new ArrayList<>();
        for (Range seedRange : seedRanges) {
            final long start = seedRange.getStart();
            final long end = seedRange.getEnd();

            // crappy if/else can be cleaned for sure
            if (sourceStart <= start && sourceEnd >= end) {
                seedRange.move(destStart - sourceStart);
                processed.add(seedRange);
            } else if (sourceStart >= start && sourceStart <= end) {
                if (sourceStart > start) {
                    newItems.add(seedRange);
                    seedRange = seedRange.splitUpper(sourceStart);
                }
                seedRange.move(destStart - sourceStart);
                processed.add(seedRange);
            } else if (sourceEnd <= end && sourceEnd >= start) {
                if (sourceEnd < end) {
                    newItems.add(seedRange.splitUpper(sourceEnd + 1));
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
}
