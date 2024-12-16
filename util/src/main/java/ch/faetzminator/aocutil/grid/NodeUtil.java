package ch.faetzminator.aocutil.grid;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharPrintable;
import ch.faetzminator.aocutil.Direction;

public final class NodeUtil {

    private NodeUtil() {
    }

    public static <T extends ElementAtPosition<T>, N extends PNode<N>> Map<Position, N> gridToNodes(final Grid<T> grid,
            final NeighbourAware<T> neighbourAware, final NodeFactory<N> nodeFactory, final Position startPosition) {

        return new GridToNodesTransformer<>(grid, nodeFactory, neighbourAware).transform(startPosition);
    }

    public static <E extends ElementAtPosition<T>, T extends CharPrintable, N extends PNode<N>> Map<Position, N> gridToNodes(
            final Grid<E> grid, final NeighbourAware<T> neighbourAware, final NodeFactory<N> nodeFactory,
            final Position startPosition, final Position endPosition) {

        return new GridToNodesTransformer<>(grid, nodeFactory, neighbourAware).transform(startPosition, endPosition);
    }

    public static interface NodeFactory<N extends PNode<N>> {

        N createNode(Position position);
    }

    public static interface NeighbourAware<T> {

        Set<Direction> getExits(T element);

        boolean canEnter(T element, Direction direction);
    }

    private static class GridToNodesTransformer<E extends ElementAtPosition<T>, T extends CharPrintable, N extends PNode<N>> {

        private final Grid<E> grid;
        private final NeighbourAware<T> neighbourAware;
        private final NodeFactory<N> nodeFactory;
        private Map<Position, N> nodes;

        public GridToNodesTransformer(final Grid<E> grid, final NodeFactory<N> nodeFactory,
                final NeighbourAware<T> neighbourAware) {
            this.grid = grid;
            this.nodeFactory = nodeFactory;
            this.neighbourAware = neighbourAware;
        }

        private boolean createNode(final Position position) {
            return createNode(position, null, -1L);
        }

        private boolean createNode(final Position position, final N lastNode, final long distance) {
            N node = nodes.get(position);
            final boolean create = node == null;
            if (create) {
                node = nodeFactory.createNode(position);
                nodes.put(position, node);
            }
            if (lastNode != null) {
                node.addNextNode(lastNode, distance);
                lastNode.addNextNode(node, distance);
            }
            return create;
        }

        private void createNodesFromGrid(final Position startPosition, final Position endPosition) {
            nodes = new LinkedHashMap<>();
            if (endPosition != null) {
                createNode(endPosition);
            }
            createNode(startPosition);

            final Queue<N> nodeQueue = new LinkedList<>();
            nodeQueue.add(nodes.get(startPosition));

            // TODO OMG refactor!
            while (!nodeQueue.isEmpty()) {
                final N lastNode = nodeQueue.poll();
                final Queue<QueueElement<E>> queue = new LinkedList<>();
                queue.add(new QueueElement<>(grid.getAt(lastNode.getPosition()), null));
                while (!queue.isEmpty()) {
                    final QueueElement<E> lastMove = queue.poll();
                    final Position lastPosition = lastMove.getElementAtPosition().getPosition();
                    final Queue<QueueElement<E>> tempQueue = new LinkedList<>();
                    final Set<Direction> directions = neighbourAware
                            .getExits(lastMove.getElementAtPosition().getElement());
                    for (final Direction direction : directions) {
                        if (lastMove.getDirection() == null || direction != lastMove.getDirection().getOpposite()) {
                            final E nextBlock = grid.getAt(lastPosition.move(direction));
                            if (nextBlock != null
                                    && neighbourAware.canEnter(nextBlock.getElement(), direction.getOpposite())) {
                                tempQueue.add(new QueueElement<>(nextBlock, direction));
                            }
                        }
                    }
                    if (tempQueue.size() > 1 && !lastNode.getPosition().equals(lastPosition)) { // we hit a node!
                        if (createNode(lastPosition, lastNode, lastMove.getDistance())) {
                            nodeQueue.add(nodes.get(lastPosition));
                        }
                    } else {
                        final long distance = lastMove.getDistance() + 1L;
                        for (final QueueElement<E> queueItem : tempQueue) {
                            final N node = nodes.get(queueItem.getElementAtPosition().getPosition());
                            if (node != null) { // we hit the end node
                                createNode(queueItem.getElementAtPosition().getPosition(), lastNode, distance);
                            }
                            queueItem.setDistance(distance);
                        }
                        queue.addAll(tempQueue);
                    }
                }
            }
        }

        public Map<Position, N> transform(final Position startPosition) {
            return transform(startPosition, null);
        }

        public Map<Position, N> transform(final Position startPosition, final Position endPosition) {
            createNodesFromGrid(startPosition, endPosition);
            return getNodes();
        }

        public Map<Position, N> getNodes() {
            return Collections.unmodifiableMap(nodes);
        }
    }

    private static class QueueElement<T extends ElementAtPosition<? extends CharPrintable>>
            extends ElementAtPositionWithDirection<T> {

        public QueueElement(final T elementAtPosition, final Direction direction) {
            super(elementAtPosition, direction);
        }

        private long distance;

        public long getDistance() {
            return distance;
        }

        public void setDistance(final long distance) {
            this.distance = distance;
        }
    }
}
