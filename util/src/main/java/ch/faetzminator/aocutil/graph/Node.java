package ch.faetzminator.aocutil.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Node<T> {

    private final T key;
    private final Set<Node<T>> neighbours = new HashSet<>();

    public Node(final T key) {
        this.key = key;
    }

    public T getKey() {
        return key;
    }

    public void addNeighbour(final Node<T> neighbour) {
        neighbours.add(neighbour);
        neighbour.neighbours.add(this);
    }

    public Set<Node<T>> getNeighbours() {
        return Collections.unmodifiableSet(neighbours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Node<?> other = (Node<?>) obj;
        return Objects.equals(key, other.key);
    }

    @Override
    public String toString() {
        return "Node [key=" + key + ", neighbours="
                + neighbours.stream().map(n -> n.getKey()).collect(Collectors.toList()) + "]";
    }
}
