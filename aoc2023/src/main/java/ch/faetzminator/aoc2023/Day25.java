package ch.faetzminator.aoc2023;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.CollectionsUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.graph.Node;
import ch.faetzminator.aocutil.graph.NodeFactory;
import ch.faetzminator.aocutil.graph.NodeGroup;
import ch.faetzminator.aocutil.graph.NodeUtil;

public class Day25 {

    public static void main(final String[] args) {
        final Day25 puzzle = new Day25();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseNode(line);
        }
        final long solution = puzzle.calculateGroups();
        PuzzleUtil.end(solution, timer);
    }

    private final NodeFactory<String> nodeFactory = new NodeFactory<>();

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\w+):\\s+(.*?)");
    private static final Pattern WHITESPACES = Pattern.compile("\\s+");

    public void parseNode(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final String key = matcher.group(1);
        final String[] values = WHITESPACES.split(matcher.group(2));
        nodeFactory.addNode(key, values);
    }

    private static final int CONNECTIONS_TO_REMOVE = 3;

    private long calculateGroups() {
        final Collection<Node<String>> nodes = nodeFactory.build();

        // the findGroups() works under the assumption the connections between the
        // groups don't share any node with each other

        // however, we have to find two nodes which are not part of the connections
        // between the groups, otherwise the connection to the other group would be
        // added in initial loop

        // using start and end nodes by running twice findFurthest() does not work for
        // the test data (however, it works for the real data!), so let's just try out
        // all nodes as a starting point
        for (final Node<String> node : nodes) {
            final List<NodeGroup<String>> groups = findGroups(nodes, node, NodeUtil.findFurthest(node));
            if (groups == null) {
                // there were some ungrouped nodes left
                continue;
            }
            boolean matches = true;
            for (final NodeGroup<String> group : groups) {
                // we test the neighbours on all groups for the right cut
                matches = matches && group.getNeighbours().size() == CONNECTIONS_TO_REMOVE;
            }
            if (matches) {
                final NodeGroup<String> group = groups.iterator().next();
                final long firstSize = group.getNodes().size();
                return firstSize * (nodes.size() - firstSize);
            }
        }
        // in such case findFurthest() was probably not good enough
        throw new RuntimeException("this didn't work");
    }

    @SafeVarargs
    private List<NodeGroup<String>> findGroups(final Collection<Node<String>> allNodes,
            final Node<String>... startNodes) {
        return findGroups(allNodes, Arrays.asList(startNodes));
    }

    private List<NodeGroup<String>> findGroups(final Collection<Node<String>> allNodes,
            final Collection<Node<String>> startNodes) {

        final List<NodeGroup<String>> groups = startNodes.stream().map(NodeGroup::new)
                .collect(Collectors.toList());

        final Set<Node<String>> ungrouped = new HashSet<>(allNodes);
        ungrouped.removeAll(startNodes);

        int lastSize;
        do {
            lastSize = ungrouped.size();

            // create a set of all neighbours of all groups
            final Set<Node<String>> neighbours = new HashSet<>();
            for (final NodeGroup<String> group : groups) {
                neighbours.addAll(group.getNeighbours());
            }

            // create a map of all neighbours to add
            Map<Node<String>, NodeGroup<String>> nodesToAdd = new HashMap<>();
            int maxConnections = -1;

            for (final Node<String> neighbour : neighbours) {
                if (ungrouped.contains(neighbour)) { // a neighbour could be in another group
                    NodeGroup<String> targetGroup = null;
                    int connections = 0;
                    for (final NodeGroup<String> group : groups) {
                        final int size = CollectionsUtil.intersectionCount(group.getNodes(), neighbour.getNeighbours());
                        if (size > connections) {
                            targetGroup = group;
                            connections = size;
                        } else if (size == connections) {
                            // we cannot have two groups with same maximum of connections
                            targetGroup = null;
                        }
                    }
                    if (targetGroup != null && connections >= maxConnections) {
                        if (connections > maxConnections) {
                            maxConnections = connections;
                            nodesToAdd = new HashMap<>();
                        }
                        nodesToAdd.put(neighbour, targetGroup);
                    }
                }
            }

            // add all nodes to its target groups
            for (final Node<String> node : nodesToAdd.keySet()) {
                nodesToAdd.get(node).addNode(node);
                ungrouped.remove(node);
            }

        } while (lastSize > ungrouped.size()); // give up if no more nodes were moved into groups

        if (!ungrouped.isEmpty()) {
            // some ungrouped left
            return null;
        }
        return groups;
    }
}
