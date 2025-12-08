package ch.faetzminator.aocutil;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Groups<T> {

    private final Map<T, Set<T>> groups = new HashMap<>();

    public Groups(final Collection<T> elements) {
        for (final T element : elements) {
            groups.put(element, new HashSet<>(Arrays.asList(element)));
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<T> merge(final T element, final T... others) {
        final Set<T> group = groups.get(element);
        for (final T other : others) {
            final Set<T> otherGroup = groups.get(other);
            group.addAll(otherGroup);
            for (final T toUpdate : otherGroup) {
                groups.put(toUpdate, group);
            }
        }
        return group;
    }

    public Set<Set<T>> getGroups() {
        return new HashSet<>(groups.values());
    }

    @Override
    public String toString() {
        return "Groups [groups=" + groups + "]";
    }
}
