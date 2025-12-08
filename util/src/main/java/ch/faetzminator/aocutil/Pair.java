package ch.faetzminator.aocutil;

import java.util.Objects;

public class Pair<T> {

    private final T left;
    private final T right;

    public Pair(final T left, final T right) {
        super();
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        final Pair other = (Pair) obj;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

    @Override
    public String toString() {
        return "Pair [left=" + left + ", right=" + right + "]";
    }
}
