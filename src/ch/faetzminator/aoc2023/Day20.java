package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20 {

    public static void main(final String[] args) {
        final Day20 puzzle = new Day20();

        final List<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
                input.add(line);
            }
        }

        System.out.println("Calculating...");
        for (final String line : input) {
            puzzle.parseModule(line);
        }
        puzzle.pressButtonPlentyTimes();
        System.out.println("Solution: " + puzzle.getPulseProduct());
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

    private long lowPulsesSent;
    private long highPulsesSent;

    public void pressButton() {
        final Queue<Event> queue = new LinkedList<>();
        queue.add(new Event("button", Broadcast.NAME, false));

        while (!queue.isEmpty()) {
            final Event event = queue.poll();
            if (event.isHighPulse()) {
                highPulsesSent++;
            } else {
                lowPulsesSent++;
            }
            final Module module = modules.get(event.getTarget());
            queue.addAll(module.processPulse(event));
        }
    }

    public void pressButtonPlentyTimes() {
        init();
        for (int i = 0; i < 1000; i++) {
            pressButton();
        }
    }

    private long getPulseProduct() {
        return lowPulsesSent * highPulsesSent;
    }

    private static class Event {
        private final String source;
        private final String target;
        private final boolean highPulse;

        public Event(final String source, final String target, final boolean highPulse) {
            this.source = source;
            this.target = target;
            this.highPulse = highPulse;
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

        protected List<Event> sendToAll(final boolean state) {
            return receivers.stream().map(receiver -> new Event(name, receiver, state)).collect(Collectors.toList());
        }

        public abstract boolean getState();

        public abstract List<Event> processPulse(Event event);

        public void init(final String sender) {

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
            return sendToAll(state);
        }
    }

    private static class Conjunction extends Module {

        private static final char PREFIX = '&';

        private final Map<String, Boolean> states = new HashMap<>();

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
            return sendToAll(!getState());
        }

        @Override
        public void init(final String sender) {
            states.put(sender, Boolean.FALSE);
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
            return sendToAll(state);
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
