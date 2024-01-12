package ch.faetzminator.aoc2023;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ch.faetzminator.aocutil.MathUtil;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day20b {

    public static void main(final String[] args) {
        final Day20b puzzle = new Day20b();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseModule(line);
        }
        final long solution = puzzle.pressButtonPlentyTimes();
        PuzzleUtil.end(solution, timer);
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("(\\S+) -> (.*)");
    private static final Pattern COMMA_PATTERN = Pattern.compile(",\\s*");

    private final Map<String, Module> modules = new LinkedHashMap<>();

    public void parseModule(final String line) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("line: " + line);
        }
        String key = matcher.group(1);
        final List<String> receivers = List.of(COMMA_PATTERN.split(matcher.group(2)));

        if (key.equals(Broadcast.NAME)) {
            modules.put(key, new Broadcast(receivers));
        } else if (key.charAt(0) == FlipFlop.PREFIX) {
            key = key.substring(1);
            modules.put(key, new FlipFlop(key, receivers));
        } else if (key.charAt(0) == Conjunction.PREFIX) {
            key = key.substring(1);
            modules.put(key, new Conjunction(key, receivers));
        }
    }

    public void init() {
        final Map<String, Module> missing = new LinkedHashMap<>();
        for (final Module module : modules.values()) {
            final List<String> receivers = module.getReceivers();
            for (final String receiver : receivers) {
                if (modules.get(receiver) == null) {
                    missing.put(receiver, new Output(receiver));
                } else {
                    modules.get(receiver).init(module.getName());
                }
            }
        }
        modules.putAll(missing);
    }

    public void pressButton(final int count) {
        final Queue<Event> queue = new LinkedList<>();
        queue.add(new Event(count, "button", Broadcast.NAME, false));

        while (!queue.isEmpty()) {
            final Event event = queue.poll();
            final Module module = modules.get(event.getTarget());
            queue.addAll(module.processPulse(event));
        }
    }

    private static final String RECEIVER_NAME = "rx";

    public long pressButtonPlentyTimes() {
        final Output receiver = new Output(RECEIVER_NAME);
        modules.put(RECEIVER_NAME, receiver);

        init();
        final Module parent = findRxParent();
        int count = 0;

        while (parent.getButtonCount() == null) {
            pressButton(++count);
        }
        return parent.getButtonCount();
    }

    private Module findRxParent() {
        for (final Module module : modules.values()) {
            if (module.getReceivers().contains(RECEIVER_NAME)) {
                return module; // we know we have just one parent after input analysis...
            }
        }
        throw new IllegalArgumentException();
    }

    private static class Event {
        private final int id;
        private final String source;
        private final String target;
        private final boolean highPulse;

        public Event(final int id, final String source, final String target, final boolean highPulse) {
            this.id = id;
            this.source = source;
            this.target = target;
            this.highPulse = highPulse;
        }

        public int getId() {
            return id;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

        public boolean isHighPulse() {
            return highPulse;
        }
    }

    private static abstract class Module {

        private final String name;
        private final List<String> receivers;

        private Module(final String name, final List<String> receivers) {
            this.name = name;
            this.receivers = receivers;
        }

        public String getName() {
            return name;
        }

        public List<String> getReceivers() {
            return receivers;
        }

        protected List<Event> sendToAll(final int id, final boolean state) {
            return receivers.stream().map(receiver -> new Event(id, name, receiver, state))
                    .collect(Collectors.toList());
        }

        public abstract boolean getState();

        public abstract List<Event> processPulse(Event event);

        public void init(final String sender) {

        }

        public Long getButtonCount() { // not really nice
            return null;
        }
    }

    private static class FlipFlop extends Module {

        private static final char PREFIX = '%';

        private boolean state;

        public FlipFlop(final String name, final List<String> receivers) {
            super(name, receivers);
        }

        @Override
        public boolean getState() {
            return state;
        }

        @Override
        public List<Event> processPulse(final Event event) {
            if (event.isHighPulse()) {
                return List.of();
            }
            state = !state;
            return sendToAll(event.getId(), state);
        }
    }

    private static class Conjunction extends Module {

        private static final char PREFIX = '&';

        private final Map<String, Boolean> states = new HashMap<>();
        private final Map<String, Long> loopLengths = new HashMap<>();

        public Conjunction(final String name, final List<String> receivers) {
            super(name, receivers);
        }

        @Override
        public boolean getState() {
            for (final Boolean value : states.values()) {
                if (!value) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public List<Event> processPulse(final Event event) {
            states.put(event.getSource(), event.isHighPulse());
            if (event.isHighPulse() && !loopLengths.containsKey(event.getSource())) {
                loopLengths.put(event.getSource(), (long) event.getId());
                tryCalculateButtonCount();
            }
            return sendToAll(event.getId(), !getState());
        }

        @Override
        public void init(final String sender) {
            states.put(sender, Boolean.FALSE);
        }

        // we know we need this nasty logic here after input analysis...
        private Long buttonCount;

        private void tryCalculateButtonCount() {
            if (loopLengths.size() == states.size()) {
                buttonCount = 1L;
                for (final Long value : loopLengths.values()) {
                    buttonCount = MathUtil.lcm(buttonCount, value);
                }
            }
        }

        @Override
        public Long getButtonCount() {
            return buttonCount;
        }
    }

    private static class Broadcast extends Module {

        private static final String NAME = "broadcaster";

        public Broadcast(final List<String> receivers) {
            super(NAME, receivers);
        }

        private boolean state;

        @Override
        public boolean getState() {
            return state;
        }

        @Override
        public List<Event> processPulse(final Event event) {
            state = event.isHighPulse();
            return sendToAll(event.getId(), state);
        }
    }

    private static class Output extends Module {

        public Output(final String name) {
            super(name, List.of());
        }

        private boolean state;

        @Override
        public boolean getState() {
            return state;
        }

        @Override
        public List<Event> processPulse(final Event event) {
            state = event.isHighPulse();
            return List.of();
        }
    }
}
