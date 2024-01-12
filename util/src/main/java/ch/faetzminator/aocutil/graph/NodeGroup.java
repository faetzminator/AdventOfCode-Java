package ch.faetzminator.aocutil.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NodeGroup<T> {

    private final Set<Node<T>> nodes = new HashSet<>();
    private final Set<Node<T>> neighbours = new HashSet<>();

    public NodeGroup(final Node<T> initialNode) {
        addNode(initialNode);
    }

    public Set<Node<T>> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public Set<Node<T>> getNeighbours() {
        return Collections.unmodifiableSet(neighbours);
    }

    public void addNode(final Node<T> node) {
        neighbours.addAll(node.getNeighbours());
        nodes.add(node);
        neighbours.removeAll(nodes);
    }

    @Override
    public String toString() {
        return "NodeGroup [nodes=" + nodes + "]";
    }
}
