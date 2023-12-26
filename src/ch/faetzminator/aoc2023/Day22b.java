package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22b {

    public static void main(final String[] args) {
        final Day22b puzzle = new Day22b();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (final String line : input) {
            puzzle.parseBrick(line);
        }
        final long solution = puzzle.calculateDisintegrableBlocks();
        System.out.println("Solution: " + solution);
    }

    private final List<Area> bricks = new ArrayList<>();

    public Day22b() {
        bricks.add(new Ground());
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)");

    public void parseBrick(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final int[] from = new int[3];
        final int[] to = new int[3];
        for (int i = 0; i < from.length; i++) {
            from[i] = Integer.parseInt(matcher.group(i + 1));
            to[i] = Integer.parseInt(matcher.group(i + from.length + 1));
        }
        bricks.add(new Brick(from, to));
    }

    public long calculateDisintegrableBlocks() {
        long sum = 0;

        Collections.sort(bricks, (o1, o2) -> {
            final int fromDiff = o2.getFrom()[2] - o1.getFrom()[2];
            return fromDiff != 0 ? fromDiff : o2.getTo()[2] - o1.getTo()[2];
        });
        Collections.reverse(bricks);

        final Set<Area> movingBricks = new LinkedHashSet<>();
        final Set<Area> fixedBricks = new LinkedHashSet<>();
        for (final Area brick : bricks) {
            if (brick.isFixed()) {
                fixedBricks.add(brick);
            } else {
                movingBricks.add(brick);
            }
        }

        while (!movingBricks.isEmpty()) {
            for (final Area moving : movingBricks) {
                boolean hit = false;
                for (final Area fixed : fixedBricks) {
                    if (moving.onTopOf(fixed)) {
                        hit = true;
                    }
                }
                if (hit) {
                    fixedBricks.add(moving);
                } else {
                    moving.fall();
                }
            }
            movingBricks.removeAll(fixedBricks);
        }

        for (final Area brick : bricks) {
            if (brick instanceof Ground) {
                continue; // eek!
            }
            final Set<Area> removed = new HashSet<>();
            removed.add(brick);
            final Set<Area> others = new HashSet<>(bricks);
            others.remove(brick);
            int recentSize;
            do {
                recentSize = removed.size();
                for (final Area other : others) {
                    if (other.isFallingIfMissing(removed)) {
                        removed.add(other);
                    }
                }
                others.removeAll(removed);
            } while (recentSize < removed.size());
            sum += removed.size() - 1;
        }

        return sum;
    }

    private static interface Area {

        String getName();

        int[] getFrom();

        int[] getTo();

        void fall();

        boolean onTopOf(Area other);

        boolean contains(int[] point);

        boolean isFixed();

        boolean isFallingIfMissing(Set<Area> missing);

        void addBelow(Area area);
    }

    private static class Brick implements Area {

        private final String name;
        private final int[] from;
        private final int[] to;

        private final Set<Area> below = new HashSet<>();
        private final Set<Area> onTopOf = new HashSet<>();

        public Brick(final int[] from, final int[] to) {
            name = null;
            this.from = from;
            this.to = to;
            for (int d = 0; d <= 2; d++) {
                if (from[d] > to[d]) {
                    throw new IllegalArgumentException();
                }
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int[] getFrom() {
            return from;
        }

        @Override
        public int[] getTo() {
            return to;
        }

        @Override
        public void fall() {
            from[2] -= 1;
            to[2] -= 1;
        }

        @Override
        public boolean onTopOf(final Area other) {
            if (!other.isFixed()) {
                return false;
            }
            for (int x = from[0]; x <= to[0]; x++) {
                for (int y = from[1]; y <= to[1]; y++) {
                    if (other.contains(new int[] { x, y, from[2] - 1 })) {
                        onTopOf.add(other);
                        other.addBelow(this);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean contains(final int[] point) {
            for (int d = 0; d <= 2; d++) {
                if (from[d] > point[d] || to[d] < point[d]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean isFixed() {
            return !onTopOf.isEmpty();
        }

        @Override
        public boolean isFallingIfMissing(final Set<Area> missing) {
            for (final Area area : onTopOf) {
                if (!missing.contains(area)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void addBelow(final Area area) {
            below.add(area);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(name).append(' ');
            builder.append(isFixed() ? 'F' : 'M').append('@').append(from[0]).append(',');
            builder.append(from[1]).append(',').append(from[2]).append('~');
            builder.append(to[0]).append(',').append(to[1]).append(',').append(to[2]);
            builder.append(" on top of " + onTopOf.stream().map(f -> f.getName()).collect(Collectors.toList()));
            return builder.toString();
        }
    }

    private static class Ground implements Area {

        private final int[] from = new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, 0 };
        private final int[] to = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, 0 };

        @Override
        public String getName() {
            return "Ground";
        }

        @Override
        public int[] getFrom() {
            return from;
        }

        @Override
        public int[] getTo() {
            return to;
        }

        @Override
        public void fall() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean onTopOf(final Area other) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(final int[] point) {
            return point[2] == 0;
        }

        @Override
        public boolean isFixed() {
            return true;
        }

        @Override
        public boolean isFallingIfMissing(final Set<Area> missing) {
            return false;
        }

        @Override
        public void addBelow(final Area area) {
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
