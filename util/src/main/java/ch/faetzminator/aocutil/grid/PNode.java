package ch.faetzminator.aocutil.grid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PNode<N extends PNode<N>> {

    private final Position position;
    private final Map<N, Long> nextNodes = new HashMap<>();

    public PNode(final Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void addNextNode(final N node, final long distance) {
        final Long oldDistance = nextNodes.get(node);
        if (oldDistance == null) {
            nextNodes.put(node, distance);
        } else if (oldDistance != distance) {
            throw new IllegalArgumentException("something wrong");
        }
    }

    public Map<N, Long> getNextNodes() {
        return Collections.unmodifiableMap(nextNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final PNode<?> other = (PNode<?>) obj;
        return Objects.equals(position, other.position);
    }

    @Override
    public String toString() {
        return "PNode [position=" + position + "]";
    }
}
