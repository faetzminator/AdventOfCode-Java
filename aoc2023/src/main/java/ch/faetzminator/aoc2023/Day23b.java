package ch.faetzminator.aoc2023;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.Direction;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;
import ch.faetzminator.aocutil.map.ElementAtPosition;
import ch.faetzminator.aocutil.map.NodeUtil;
import ch.faetzminator.aocutil.map.NodeUtil.NeighbourAware;
import ch.faetzminator.aocutil.map.PMap;
import ch.faetzminator.aocutil.map.PMapFactory;
import ch.faetzminator.aocutil.map.PNode;
import ch.faetzminator.aocutil.map.Position;

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

    public void parseLines(final List<String> input) {
        map = new PMapFactory<>(BlockAtPosition.class,
                (character, position) -> new BlockAtPosition(Block.byChar(character), position)).create(input);
    }

    public long findLongestPath() {
        final Position startPos = new Position(1, 0);
        final Position endPos = new Position(map.getXSize() - 2, map.getYSize() - 1);

        final Map<Position, Node> nodes = NodeUtil.mapToNodes(map, new NeighbourAware<Block>() {
            @Override
            public Set<Direction> getExits(final Block block) {
                return block.getExits();
            }

            @Override
            public boolean canEnter(final Block block, final Direction direction) {
                return block.canEnter(direction);
            }
        }, Node::new, startPos, endPos);
        return findLongestPath(nodes, startPos, endPos);
    }

    private long findLongestPath(final Map<Position, Node> nodes, final Position startPos, final Position endPos) {
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

    private static class BlockAtPosition extends ElementAtPosition<Block> {

        public BlockAtPosition(final Block block, final Position position) {
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

    private static class Node extends PNode<Node> {

        private static int nextId = 0;

        private final int id;

        public Node(final Position position) {
            super(position);
            id = nextId++;
        }

        public int getId() {
            return id;
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
