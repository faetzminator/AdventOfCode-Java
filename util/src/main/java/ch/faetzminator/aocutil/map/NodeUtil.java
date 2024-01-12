package ch.faetzminator.aocutil.map;

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

    public static <T extends ElementAtPosition<T>, N extends PNode<N>> Map<Position, N> mapToNodes(final PMap<T> map,
            final NeighbourAware<T> neighbourAware, final NodeFactory<N> nodeFactory, final Position startPosition) {

        return new MapToNodesTransformer<>(map, nodeFactory, neighbourAware).transform(startPosition);
    }

    public static <E extends ElementAtPosition<T>, T extends CharPrintable, N extends PNode<N>> Map<Position, N> mapToNodes(
            final PMap<E> map, final NeighbourAware<T> neighbourAware, final NodeFactory<N> nodeFactory,
            final Position startPosition, final Position endPosition) {

        return new MapToNodesTransformer<>(map, nodeFactory, neighbourAware).transform(startPosition, endPosition);
    }

    public static interface NodeFactory<N extends PNode<N>> {

        N createNode(Position position);
    }

    public static interface NeighbourAware<T> {

        Set<Direction> getExits(T element);

        boolean canEnter(T element, Direction direction);
    }

    private static class MapToNodesTransformer<E extends ElementAtPosition<T>, T extends CharPrintable, N extends PNode<N>> {

        private final PMap<E> map;
        private final NeighbourAware<T> neighbourAware;
        private final NodeFactory<N> nodeFactory;
        private Map<Position, N> nodes;

        public MapToNodesTransformer(final PMap<E> map, final NodeFactory<N> nodeFactory,
                final NeighbourAware<T> neighbourAware) {
            this.map = map;
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

        private void createNodesFromMap(final Position startPosition, final Position endPosition) {
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
                queue.add(new QueueElement<>(map.getElementAt(lastNode.getPosition()), null));
                while (!queue.isEmpty()) {
                    final QueueElement<E> lastMove = queue.poll();
                    final Position lastPosition = lastMove.getElementAtPosition().getPosition();
                    final Queue<QueueElement<E>> tempQueue = new LinkedList<>();
                    final Set<Direction> directions = neighbourAware
                            .getExits(lastMove.getElementAtPosition().getElement());
                    for (final Direction direction : directions) {
                        if (lastMove.getDirection() == null || direction != lastMove.getDirection().getOpposite()) {
                            final E nextBlock = map.getElementAt(lastPosition.move(direction));
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
            createNodesFromMap(startPosition, endPosition);
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
