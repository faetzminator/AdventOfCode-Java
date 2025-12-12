package ch.faetzminator.aoc2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.grid.Grid;
import ch.faetzminator.aocutil.grid.GridFactory;

public class Day12 {

    public static void main(final String[] args) {
        final Day12 puzzle = new Day12();
        final List<List<String>> blocks = ScannerUtil.readNonBlankLinesBlocks();
        final List<String> lastBlock = blocks.remove(blocks.size() - 1);
        final Timer timer = PuzzleUtil.start();
        for (final List<String> lines : blocks) {
            puzzle.parsePresentLines(lines);
        }
        for (final String line : lastBlock) {
            puzzle.parseRegionLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern PRESENT_HEADER_PATTERN = Pattern.compile("(\\d+):");
    private static final Pattern REGION_PATTERN = Pattern.compile("(\\d+)x(\\d+): (\\d+(?: \\d+)*)");
    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");
    private static final double MAGIC_FU_K_SH_T_FACTOR = 1.1836734d;

    private final List<Present> presents = new ArrayList<>();
    private final PresentCountsParser countsParser = new PresentCountsParser();
    private final ProblemSolver solver = new ProblemSolver(MAGIC_FU_K_SH_T_FACTOR);
    private final SolutionIncrementer incrementer = new SolutionIncrementer();

    public void parsePresentLines(final List<String> input) {
        final String header = input.remove(0);
        final Matcher matcher = PRESENT_HEADER_PATTERN.matcher(header);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("header line: " + header);
        }
        final int index = Integer.parseInt(matcher.group(1));
        if (index != presents.size()) {
            throw new IllegalArgumentException("wrong index " + index + " for size " + presents.size());
        }
        presents.add(Present.of(input));
    }

    public void parseRegionLine(final String input) {
        final Matcher matcher = REGION_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        final Region region = Region.of(matcher.group(1), matcher.group(2));
        final List<Integer> presentCounts = countsParser.parse(matcher.group(3));
        incrementer.increment(solver.fits(region, presents, presentCounts));
    }

    public long getSolution() {
        return incrementer.get();
    }

    public static class Present {

        private static final GridFactory<Grid<Point>, Point> FACTORY = new GridFactory<>(Point.class,
                (character, position) -> Point.byChar(character));

        private final Grid<Point> grid;
        private final long size;

        public Present(final Grid<Point> grid) {
            this.grid = grid;
            size = grid.stream().filter(point -> point == Point.USED).count();
        }

        public long getSize() {
            return size;
        }

        public static Present of(final List<String> lines) {
            return new Present(FACTORY.create(lines));
        }

        @Override
        public String toString() {
            return grid.toString();
        }
    }

    public static enum Point implements CharEnum {
        USED('#'),
        FREE('.');

        private final char character;

        private Point(final char character) {
            this.character = character;
        }

        @Override
        public char charValue() {
            return character;
        }

        public static Point byChar(final char c) {
            return CharEnum.byChar(Point.class, c);
        }
    }

    public static class Region {

        private final int xSize;
        private final int ySize;

        public Region(final int xSize, final int ySize) {
            this.xSize = xSize;
            this.ySize = ySize;
        }

        public int getXSize() {
            return xSize;
        }

        public int getYSize() {
            return ySize;
        }

        public long getArea() {
            return ((long) xSize) * ySize;
        }

        public static Region of(final String xSize, final String ySize) {
            return new Region(Integer.parseInt(xSize), Integer.parseInt(ySize));
        }
    }

    public static class PresentCountsParser {

        public List<Integer> parse(final String input) {
            return Arrays.stream(SPACE_PATTERN.split(input)).map(Integer::valueOf).collect(Collectors.toList());
        }
    }

    public static class ProblemSolver {

        private final double secret;

        public ProblemSolver(final double secret) {
            this.secret = secret;
        }

        public boolean fits(final Region region, final List<Present> presents, final List<Integer> presentCounts) {
            long spaceNeeded = 0L;
            for (int i =0; i< presents.size(); i++) {
                spaceNeeded += (presents.get(i).getSize()) * presentCounts.get(i);
            }
            return spaceNeeded * secret <= region.getArea();
        }
    }

    public static class SolutionIncrementer {

        private long value;

        public void increment(final boolean performIncrement) {
            if (performIncrement) {
                value++;
            }
        }

        public long get() {
            return value;
        }
    }
}
