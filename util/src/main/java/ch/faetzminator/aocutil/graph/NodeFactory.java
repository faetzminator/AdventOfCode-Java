package ch.faetzminator.aocutil.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NodeFactory<T> {

    private final Map<T, Node<T>> nodes = new HashMap<>();
    private final boolean directed;

    public NodeFactory() {
        this(false);
    }

    public NodeFactory(final boolean directed) {
        this.directed = directed;
    }

    @SuppressWarnings("unchecked")
    public void addNode(final T key, final T... neighbourKeys) {
        final Node<T> node = getOrCreateNode(key);
        for (final T neighbourKey : neighbourKeys) {
            final Node<T> neighbourNode = getOrCreateNode(neighbourKey);
            node.addNeighbour(neighbourNode);
            if (!directed) {
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
