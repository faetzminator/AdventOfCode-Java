package ch.faetzminator.aocutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class CollectionsUtil {

    private CollectionsUtil() {
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
        return sortByValue(map, false);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map,
            final boolean inverse) {
        return sort(map, Entry.comparingByValue(), inverse);
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(final Map<K, V> map) {
        return sortByKey(map, false);
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(final Map<K, V> map, final boolean inverse) {
        return sort(map, Entry.comparingByKey(), inverse);
    }

    private static <K, V> Map<K, V> sort(final Map<K, V> map, final Comparator<Entry<K, V>> comparator,
            final boolean inverse) {
        final List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(comparator);
        if (inverse) {
            Collections.reverse(list);
        }

        final Map<K, V> result = new LinkedHashMap<>();
        for (final Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <T> int intersectionCount(final Collection<T> someData, final Collection<T> otherData) {
        int count = 0;
        for (final T entry : someData) {
            if (otherData.contains(entry)) {
                count++;
            }
        }
        return count;
    }
}
