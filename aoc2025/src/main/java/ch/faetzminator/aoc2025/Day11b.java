package ch.faetzminator.aoc2025;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.graph.Node;
import ch.faetzminator.aocutil.graph.NodeFactory;

public class Day11b {

    public static void main(final String[] args) {
        final Day11b puzzle = new Day11b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getSolution();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\S+): (\\S+(?: \\S+)*)");
    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

    private final NodeFactory<String> nodeFactory = new NodeFactory<>(true);

    public void parseLine(final String input) {
        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        nodeFactory.addNode(matcher.group(1), SPACE_PATTERN.split(matcher.group(2)));
    }

    public long getSolution() {
        final Node<String> svr = nodeFactory.get("svr");
        final Node<String> fft = nodeFactory.get("fft");
        final Node<String> dac = nodeFactory.get("dac");
        final Node<String> out = nodeFactory.get("out");
        return getSolution(svr, fft) * getSolution(fft, dac) * getSolution(dac, out)
                + getSolution(svr, dac) * getSolution(dac, fft) * getSolution(fft, out);
    }

    private long getSolution(final Node<String> start, final Node<String> end) {
        return getSolution(start, end, new HashMap<>());
    }

    private long getSolution(final Node<String> current, final Node<String> end, final Map<Node<String>, Long> cache) {
        if (current.equals(end)) {
            return 1L;
        }
        if (cache.containsKey(current)) {
            return cache.get(current);
        }
        long sum = 0L;
        for (final Node<String> next : current.getNeighbours()) {
            sum += getSolution(next, end, cache);
        }
        cache.put(current, sum);
        return sum;
    }
}
