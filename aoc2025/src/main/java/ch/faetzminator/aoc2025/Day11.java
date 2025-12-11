package ch.faetzminator.aoc2025;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.graph.Node;
import ch.faetzminator.aocutil.graph.NodeFactory;

public class Day11 {

    public static void main(final String[] args) {
        final Day11 puzzle = new Day11();
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
        return getSolution(nodeFactory.get("you"), nodeFactory.get("out"));
    }

    private long getSolution(final Node<String> start, final Node<String> end) {
        final Queue<Node<String>> queue = new LinkedList<>();
        queue.add(start);
        long count = 0L;
        while (!queue.isEmpty()) {
            final Node<String> node = queue.poll();
            if (node.equals(end)) {
                count++;
            } else {
                queue.addAll(node.getNeighbours());
            }
        }
        return count;
    }
}
