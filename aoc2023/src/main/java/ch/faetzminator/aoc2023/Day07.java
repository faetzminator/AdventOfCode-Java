package ch.faetzminator.aoc2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ch.faetzminator.aocutil.CharEnum;
import ch.faetzminator.aocutil.PuzzleUtil;
import ch.faetzminator.aocutil.ScannerUtil;
import ch.faetzminator.aocutil.Timer;

public class Day07 {

    public static void main(final String[] args) {
        final Day07 puzzle = new Day07();

        final List<String> lines = ScannerUtil.readNonBlankLines();
        final Timer timer = PuzzleUtil.start();
        for (final String line : lines) {
            puzzle.parseHand(line);
        }
        final long solution = puzzle.calculateSum();
        PuzzleUtil.end(solution, timer);
    }

    private final List<Hand> hands = new ArrayList<>();

    public void parseHand(final String line) {
        final String[] parts = line.split(" ");

        final Type type = Type.findType(parts[0]);
        final List<Strength> cards = new ArrayList<>();
        for (final char c : parts[0].toCharArray()) {
            cards.add(Strength.byChar(c));
        }
        hands.add(new Hand(parts[0], type, cards, Long.parseLong(parts[1])));
    }

    public long calculateSum() {
        long sum = 0;
        Collections.sort(hands);
        int rank = hands.size();
        for (final Hand hand : hands) {
            sum += hand.getBid() * rank--;
        }
        return sum;
    }

    private static class Hand implements Comparable<Hand> {

        private final String cardsRaw;
        private final Type type;
        private final List<Strength> cards;
        private final long bid;

        public Hand(final String cardsRaw, final Type type, final List<Strength> cards, final long bid) {
            this.cardsRaw = cardsRaw;
            this.type = type;
            this.cards = cards;
            this.bid = bid;
        }

        public Type getType() {
            return type;
        }

        public List<Strength> getCards() {
            return cards;
        }

        public long getBid() {
            return bid;
        }

        @Override
        public int compareTo(final Hand o) {
            int ordinal = getType().ordinal() - o.getType().ordinal();
            if (ordinal != 0) {
                return ordinal;
            }
            for (int i = 0; i < getCards().size(); i++) {
                ordinal = getCards().get(i).ordinal() - o.getCards().get(i).ordinal();
                if (ordinal != 0) {
                    return ordinal;
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            return cardsRaw;
        }
    }

    private static enum Strength implements CharEnum {

        A,
        K,
        Q,
        J,
        T,
        N9,
        N8,
        N7,
        N6,
        N5,
        N4,
        N3,
        N2;

        private final char label;

        private Strength() {
            label = name().replaceFirst("^N", "").charAt(0);
        }

        @Override
        public char charValue() {
            return label;
        }

        public static Strength byChar(final char c) {
            return CharEnum.byChar(Strength.class, c);
        }
    }

    private static enum Type {

        FIVE_OF_A_KIND(new CountMatcher(5)),
        FOUR_OF_A_KIND(new CountMatcher(4)),
        FULL_HOUSE(new CountMatcher(3, 2)),
        THREE_OF_A_KIND(new CountMatcher(3)),
        TWO_PAIR(new CountMatcher(2, 2)),
        ONE_PAIR(new CountMatcher(2)),
        HIGH_CARD(str -> true);

        private final Function<String, Boolean> matcher;

        private Type(final Function<String, Boolean> matcher) {
            this.matcher = matcher;
        }

        public Function<String, Boolean> getMatcher() {
            return matcher;
        }

        public static Type findType(final String str) {
            for (final Type type : values()) {
                if (type.getMatcher().apply(str)) {
                    return type;
                }
            }
            return null;
        }
    }

    private static class CountMatcher implements Function<String, Boolean> {

        private final int[] countNeeded;

        public CountMatcher(final int... count) {
            countNeeded = count;
        }

        private Map.Entry<Character, Integer> findEntry(final Map<Character, Integer> chars, final int count) {
            for (final Map.Entry<Character, Integer> entry : chars.entrySet()) {
                if (entry.getValue() >= count) {
                    return entry;
                }
            }
            return null;
        }

        @Override
        public Boolean apply(final String t) {
            final Map<Character, Integer> chars = new HashMap<>();
            for (final char c : t.toCharArray()) {
                if (!chars.containsKey(c)) {
                    chars.put(c, 0);
                }
                chars.put(c, chars.get(c) + 1);
            }
            for (final int count : countNeeded) {
                final Map.Entry<Character, Integer> entry = findEntry(chars, count);
                if (entry == null) {
                    return false;
                }
                chars.remove(entry.getKey());
            }
            return true;
        }
    }
}
