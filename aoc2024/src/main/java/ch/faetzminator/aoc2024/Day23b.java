package ch.faetzminator.aoc2024;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.graph.Node;
import ch.faetzminator.aocutil.graph.NodeFactory;

public class Day23b {

    public static void main(final String[] args) {
        final Day23b puzzle = new Day23b();
        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseLine(line);
        }
        final String solution = puzzle.getLanPartyPassword();
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

    private Set<Node<String>> getLargestGroup(final Collection<Node<String>> nodes) {
        Set<Set<Node<String>>> groups;
        Set<Set<Node<String>>> newGroups = nodes.stream().map(Set::of).collect(Collectors.toSet());
        do {
            groups = newGroups;
            newGroups = new HashSet<>();
            for (final Node<String> node : nodes) {
                for (final Set<Node<String>> group : groups) {
                    if (!group.contains(node)
                            && !group.stream().anyMatch(other -> !node.getNeighbours().contains(other))) {

                        final Set<Node<String>> newGroup = new HashSet<>(group);
                        newGroup.add(node);
                        newGroups.add(Collections.unmodifiableSet(newGroup));
                    }
                }
            }
        } while (!newGroups.isEmpty());
        return groups.iterator().next();
    }

    public String getLanPartyPassword() {
        final Set<Node<String>> group = getLargestGroup(nodeFactory.build());
        final List<String> solution = group.stream().map(Node::getKey).sorted().collect(Collectors.toList());
        return String.join(",", solution);
    }
}
