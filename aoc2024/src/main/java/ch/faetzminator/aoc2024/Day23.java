package ch.faetzminator.aoc2024;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.graph.Node;
import ch.faetzminator.aocutil.graph.NodeFactory;

public class Day23 {

    public static void main(final String[] args) {
        final Day23 puzzle = new Day23();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final long solution = puzzle.getComputerSetSum();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("([a-z]+)\\-([a-z]+)");

    private final NodeFactory<String> nodeFactory = new NodeFactory<>();

    public void parseLine(final String input) {
        final Matcher matcher = LINE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + input);
        }
        nodeFactory.addNode(matcher.group(1), matcher.group(2));
    }

    private long getComputerSetSum(final Collection<Node<String>> nodes) {
        final Set<Set<Node<String>>> groups = new HashSet<>();
        for (final Node<String> node : nodes) {
            if (node.getKey().charAt(0) != 't') {
                continue;
            }
            for (final Node<String> one : node.getNeighbours()) {
                for (final Node<String> other : node.getNeighbours()) {
                    if (one.getNeighbours().contains(other)) {
                        groups.add(new HashSet<>(Arrays.asList(node, one, other)));
                    }
                }
            }
        }
        return groups.size();
    }

    public long getComputerSetSum() {
        return getComputerSetSum(nodeFactory.build());
    }
}
