package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        nodeFactory.addNode(key, Arrays.asList(values), true);
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

        final List<NodeGroup<String>> groups = startNodes.stream().map(node -> new NodeGroup<>(node))
                .collect(Collectors.toList());

        final Set<Node<String>> ungrouped = new HashSet<>(allNodes);
        ungrouped.removeAll(startNodes);

        int lastSize;
        do {
            lastSize = ungrouped.size();
            // for the neighbours of each group, check how many neighbours they have in the
            // given group (let's call them friends ;) )
            final List<Map<Node<String>, Integer>> friends = new ArrayList<>(groups.size());
            for (int i = 0; i < groups.size(); i++) {
                friends.add(new HashMap<>());
                final NodeGroup<String> group = groups.get(i);
                for (final Node<String> neighbour : group.getNeighbours()) {
                    if (ungrouped.contains(neighbour)) {
                        final int size = CollectionsUtil.intersectionCount(group.getNodes(), neighbour.getNeighbours());
                        friends.get(i).put(neighbour, size);
                    }
                }
            }
            // find nodes which are equal across two groups
            final Set<Node<String>> toClean = new HashSet<>();
            for (int i = 0; i < friends.size(); i++) {
                for (int j = i + 1; j < friends.size(); j++) {
                    for (final Entry<Node<String>, Integer> entry : friends.get(i).entrySet()) {
                        if (entry.getValue().equals(friends.get(j).get(entry.getKey()))) {
                            toClean.add(entry.getKey());
                        }
                    }
                }
            }
            int maxSize = 0;
            // ... and clean those nodes
            for (final Map<Node<String>, Integer> map : friends) {
                for (final Node<String> key : toClean) {
                    map.remove(key);
                }
                if (map.isEmpty()) {
                    continue;
                }
                // then sort the maps and find "maximum friend"
                CollectionsUtil.sortByValue(map);
                final int size = map.entrySet().iterator().next().getValue();
                if (size > maxSize) {
                    maxSize = size;
                }
            }
            for (int i = 0; i < friends.size(); i++) {
                final Iterator<Entry<Node<String>, Integer>> it = friends.get(i).entrySet().iterator();
                Entry<Node<String>, Integer> entry;
                // within each group, add friends with maxSize to the same
                while (it.hasNext() && (entry = it.next()).getValue() == maxSize) {
                    groups.get(i).addNode(entry.getKey());
                    ungrouped.remove(entry.getKey());
                }
            }
        } while (lastSize > ungrouped.size()); // give up if no more nodes were moved into groups

        if (!ungrouped.isEmpty()) {
            // some ungrouped left
            return null;
        }
        return groups;
    }
}
