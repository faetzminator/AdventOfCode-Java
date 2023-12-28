package ch.faetzminator.aoc2023;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.ElementAtPosition;
import ch.faetzminator.aocutil.PMap;
import ch.faetzminator.aocutil.Position;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day23b {

    public static void main(final String[] args) {
        final Day23b puzzle = new Day23b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        puzzle.parseLines(lines);
        final long solution = puzzle.findLongestPath();
        PuzzleUtil.end(solution, timer);
    }

    private PMap<BlockAtPosition> map;
    private Map<Position, Node> nodes;

    public void parseLines(final List<String> input) {
        map = new PMap<>(BlockAtPosition.class, input.get(0).length(), input.size());

        for (int y = 0; y < input.size(); y++) {
            final String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                final BlockAtPosition block = new BlockAtPosition(new Position(x, y), Block.byChar(line.charAt(x)));
                map.setElementAt(block.getPosition(), block);
            }
        }
    }

    public long findLongestPath() {
        final Position startPos = new Position(1, 0);
        final Position endPos = new Position(map.getXSize() - 2, map.getYSize() - 1);

        createNodesFromMap(startPos, endPos);
        return findLongestPath(startPos, endPos);
    }

    private long findLongestPath(final Position startPos, final Position endPos) {
        final Queue<NodeQueueItem> queue = new LinkedList<>();
        queue.add(new NodeQueueItem(nodes.get(startPos), new boolean[Node.getNextId()], 0L));

        long longestDistance = 0L;

        while (!queue.isEmpty()) {
            final NodeQueueItem item = queue.poll();
            // the check for end node looks strange
            if (endPos.equals(item.getNode().getPosition()) && longestDistance < item.getDistance()) {
                longestDistance = item.getDistance();
            }
            final Node node = item.getNode();
            for (final Entry<Node, Long> entry : node.getNextNodes().entrySet()) {
                final Node nextNode = entry.getKey();
                if (!item.isVisited(nextNode)) {
                    queue.add(new NodeQueueItem(nextNode, item.getVisited(), item.getDistance() + entry.getValue()));
                }
            }

        }
        return longestDistance;
    }

    private boolean createNode(final Position position) {
        return createNode(position, null, -1L);
    }

    private boolean createNode(final Position position, final Node lastNode, final long distance) {
        Node node = nodes.get(position);
        final boolean create = node == null;
        if (create) {
            node = new Node(position);
            nodes.put(position, node);
        }
        if (lastNode != null) {
            node.addNextNode(lastNode, distance);
            lastNode.addNextNode(node, distance);
        }
        return create;
    }

    private void createNodesFromMap(final Position startPos, final Position endPos) {
        nodes = new LinkedHashMap<>();
        createNode(endPos);
        createNode(startPos);

        final Queue<Node> nodeQueue = new LinkedList<>();
        nodeQueue.add(nodes.get(startPos));

        // TODO OMG refactor! move to util?
        while (!nodeQueue.isEmpty()) {
            final Node lastNode = nodeQueue.poll();
            final Queue<BlockAtPositionWithDirection> queue = new LinkedList<>();
            queue.add(new BlockAtPositionWithDirection(map.getElementAt(lastNode.getPosition()), null));
            while (!queue.isEmpty()) {
                final BlockAtPositionWithDirection lastMove = queue.poll();
                final Position lastPosition = lastMove.getBlockAtPosition().getPosition();
                final Queue<BlockAtPositionWithDirection> tempQueue = new LinkedList<>();
                for (final Direction direction : lastMove.getBlockAtPosition().getElement().getExits()) {
                    if (lastMove.getDirection() == null || direction != lastMove.getDirection().getOpposite()) {
                        final BlockAtPosition nextBlock = map.getElementAt(lastPosition.move(direction));
                        if (nextBlock != null && nextBlock.getElement().canEnter(direction.getOpposite())) {
                            tempQueue.add(new BlockAtPositionWithDirection(nextBlock, direction));
                        }
                    }
                }
                if (tempQueue.size() > 1 && !lastNode.getPosition().equals(lastPosition)) { // we hit a node!
                    if (createNode(lastPosition, lastNode, lastMove.getDistance())) {
                        nodeQueue.add(nodes.get(lastPosition));
                    }
                } else {
                    final long distance = lastMove.getDistance() + 1L;
                    for (final BlockAtPositionWithDirection queueItem : tempQueue) {
                        final Node node = nodes.get(queueItem.getBlockAtPosition().getPosition());
                        if (node != null) { // we hit the end node
                            createNode(queueItem.getBlockAtPosition().getPosition(), lastNode, distance);
                        }
                        queueItem.setDistance(distance);
                    }
                    queue.addAll(tempQueue);
                }
            }
        }
    }

    private static class BlockAtPositionWithDirection {
        private final BlockAtPosition blockAtPosition;
        private final Direction direction;
        private long distance = 0L;

        public BlockAtPositionWithDirection(final BlockAtPosition blockAtPosition, final Direction direction) {
            this.blockAtPosition = blockAtPosition;
            this.direction = direction;
        }

        public BlockAtPosition getBlockAtPosition() {
            return blockAtPosition;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDistance(final long distance) {
            this.distance = distance;
        }

        public long getDistance() {
            return distance;
        }
    }

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        public BlockAtPosition(final Position position, final Block block) {
            super(block, position);
        }
    }

    private static enum Block implements CharEnum {

        PATH('.'), ROCK('#', false), SLOPE_UP('^'), SLOW_RIGHT('>'), SLOPE_DOWN('v'), SLOPE_LEFT('<');

        private final char character;
        private final Set<Direction> entries;
        private final Set<Direction> exits;

        private Block(final char character) {
            this(character, true);
        }

        private Block(final char character, final boolean canEnter) {
            this.character = character;
            exits = canEnter ? Set.of(Direction.values()) : Set.of();
            entries = exits;
        }

        @Override
        public char getCharacter() {
            return character;
        }

        public static Block byChar(final char c) {
            return CharEnum.byChar(Block.class, c);
        }

        public boolean canEnter(final Direction fromDirection) {
            return entries.contains(fromDirection);
        }

        public Set<Direction> getExits() {
            return exits;
        }
    }

    private static class Node {

        private static int nextId = 0;

        private final Position position;
        private final int id;
        private final Map<Node, Long> nextNodes = new HashMap<>();

        public Node(final Position position) {
            this.position = position;
            id = nextId++;
        }

        public Position getPosition() {
            return position;
        }

        public int getId() {
            return id;
        }

        public void addNextNode(final Node node, final long distance) {
            final Long oldDistance = nextNodes.get(node);
            if (oldDistance == null) {
                nextNodes.put(node, distance);
            } else if (oldDistance != distance) {
                throw new IllegalArgumentException("something wrong");
            }
        }

        public Map<Node, Long> getNextNodes() {
            return Collections.unmodifiableMap(nextNodes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            final Node other = (Node) obj;
            return id == other.id;
        }

        public static int getNextId() {
            return nextId;
        }
    }

    private static class NodeQueueItem {

        private final Node node;
        private final boolean[] visited;
        private final long distance;

        public NodeQueueItem(final Node node, final boolean[] earlierVisited, final long distance) {
            this.node = node;
            visited = new boolean[earlierVisited.length];
            System.arraycopy(earlierVisited, 0, visited, 0, earlierVisited.length);
            visited[node.getId()] = true;
            this.distance = distance;
        }

        public boolean isVisited(final Node node) {
            return visited[node.getId()];
        }

        public Node getNode() {
            return node;
        }

        public boolean[] getVisited() {
            return visited;
        }

        public long getDistance() {
            return distance;
        }
    }
}
