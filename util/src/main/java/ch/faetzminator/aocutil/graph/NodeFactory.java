package ch.faetzminator.aocutil.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NodeFactory<T> {

    private final Map<T, Node<T>> nodes = new HashMap<>();

    public void addNode(final T key, final Collection<T> neighbourKeys, final boolean undirected) {
        final Node<T> node = getOrCreateNode(key);
        for (final T neighbourKey : neighbourKeys) {
            final Node<T> neighbourNode = getOrCreateNode(neighbourKey);
            node.addNeighbour(neighbourNode);
            if (undirected) {
                neighbourNode.addNeighbour(node);
            }
        }
    }

    private Node<T> getOrCreateNode(final T key) {
        if (!nodes.containsKey(key)) {
            nodes.put(key, new Node<>(key));
        }
        return nodes.get(key);
    }

    public Collection<Node<T>> build() {
        return nodes.values();
    }
}
