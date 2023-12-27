package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day25 {

    public static void main(final String[] args) {
        final Day25 puzzle = new Day25();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseConnections(line);
        }
        puzzle.calculateGroups();
        final long solution = puzzle.getGroupsProduct();
        PuzzleUtil.end(solution, timer);
    }

    private final Map<String, Set<String>> connections = new HashMap<>();
    private final Map<String, Set<String>> blacklist = new LinkedHashMap<>();

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\w+):\\s+(.*?)");
    private static final Pattern WHITESPACES = Pattern.compile("\\s+");

    public void parseConnections(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        final String key = matcher.group(1);
        final String[] values = WHITESPACES.split(matcher.group(2));
        for (final String value : values) {
            addEntries(connections, key, value);
        }
    }

    private static final int CONNECTIONS_TO_REMOVE = 3;

    private void calculateGroups() {
        // TODO faster algo? different algo?
        int count = 0;
        while (count++ < CONNECTIONS_TO_REMOVE) {
            final Map<Connection, Integer> counts = new HashMap<>();
            final List<String> nodes = new ArrayList<>(connections.keySet());

            for (int i = 0; i < nodes.size(); i++) {
                if (i > 0 && i % 100 == 0) {
                    System.out.println(String.format("Sorry, some more time please! %d/%d: %d/%d", count,
                            CONNECTIONS_TO_REMOVE, i, nodes.size()));
                }
                for (int j = i + 1; j < nodes.size(); j++) {
                    findPath(counts, nodes.get(i), nodes.get(j));
                }
            }

            final Map<Connection, Integer> sorted = sortByValue(counts);
            final Iterator<Connection> iterator = sorted.keySet().iterator();
            final Connection connection = iterator.next();
            addEntries(blacklist, connection.getFrom(), connection.getTo());
        }
    }

    private void findPath(final Map<Connection, Integer> counts, final String fromKey, final String toKey) {
        final Queue<NodeWithDistance> queue = new LinkedList<>();
        final NodeWithDistance startPoint = new NodeWithDistance(fromKey);
        startPoint.setDistance(null, 0);
        final Map<String, NodeWithDistance> nodes = new HashMap<>();
        queue.add(startPoint);

        while (!queue.isEmpty()) {
            final NodeWithDistance node = queue.poll();
            final String key = node.getKey();
            if (!nodes.containsKey(key)) {
                nodes.put(key, node);
            }
            final int newDistance = node.getDistance() + 1;
            for (final String target : connections.get(key)) {
                if (blacklist.containsKey(key) && blacklist.get(key).contains(target)) {
                    continue;
                }
                if (!nodes.containsKey(target)) {
                    nodes.put(target, new NodeWithDistance(target));
                }
                final NodeWithDistance targetNode = nodes.get(target);
                if (targetNode.setDistance(key, newDistance)) {
                    queue.add(targetNode);
                }
            }
        }
        if (!nodes.containsKey(toKey)) {
            throw new IllegalArgumentException("node " + toKey + " unreachable");
        }

        final Queue<String> queue2 = new LinkedList<>();
        queue2.add(toKey);
        while (!queue2.isEmpty()) {
            final String key = queue2.poll();
            for (final String from : nodes.get(key).getFrom()) {
                final Connection connection = new Connection(key, from);
                if (!counts.containsKey(connection)) {
                    counts.put(connection, 0);
                }
                counts.put(connection, counts.get(connection) + 1);
                queue2.add(from);
            }
        }
    }

    public long getGroupsProduct() {
        final int groupSize = getGroupSize();
        return groupSize * (connections.size() - groupSize);
    }

    private int getGroupSize() {
        final Queue<String> queue = new LinkedList<>();
        final Set<String> group = new HashSet<>();
        queue.add(connections.entrySet().iterator().next().getKey());

        while (!queue.isEmpty()) {
            final String key = queue.poll();
            group.add(key);
            final Set<String> next = connections.get(key);
            next.removeAll(group);
            if (blacklist.containsKey(key)) {
                next.removeAll(blacklist.get(key));
            }
            queue.addAll(next);
        }
        return group.size();
    }

    private static void addEntries(final Map<String, Set<String>> map, final String a, final String b) {
        addEntry(map, a, b);
        addEntry(map, b, a);
    }

    private static void addEntry(final Map<String, Set<String>> map, final String key, final String value) {
        if (!map.containsKey(key)) {
            map.put(key, new HashSet<>());
        }
        map.get(key).add(value);
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
        final List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);

        final Map<K, V> result = new LinkedHashMap<>();
        for (final Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private static class Connection {

        private final String from;
        private final String to;

        public Connection(final String a, final String b) {
            if (a.compareTo(b) < 0) {
                from = a;
                to = b;
            } else {
                from = b;
                to = a;
            }
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final Connection other = (Connection) obj;
            return Objects.equals(from, other.from) && Objects.equals(to, other.to);
        }
    }

    private static class NodeWithDistance {

        private final String key;
        private int distance = Integer.MAX_VALUE;
        private Set<String> from;

        public NodeWithDistance(final String key) {
            this.key = key;
        }

        public boolean setDistance(final String from, final int distance) {
            if (distance > this.distance) {
                return false;
            }
            if (distance < this.distance) {
                this.from = new HashSet<>();
                this.distance = distance;
                if (from != null) {
                    this.from.add(from);
                }
                return true;
            }
            this.from.add(from);
            return false;
        }

        public String getKey() {
            return key;
        }

        public int getDistance() {
            return distance;
        }

        public Set<String> getFrom() {
            return from;
        }
    }
}
