package ch.faetzminator.aocutil.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class NodeUtil {

    private NodeUtil() {
    }

    public static <T> Node<T> findFurthest(final Node<T> start) {
        Node<T> result = null;
        final Set<Node<T>> seen = new HashSet<>();
        final Queue<Node<T>> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            final Node<T> node = queue.poll();
            result = node;
            seen.add(node);
            for (final Node<T> next : node.getNeighbours()) {
                if (!seen.contains(next)) {
                    queue.add(next);
                }
            }
        }
        return result;
    }
}
