package ch.faetzminator.aocutil;

public class LRNode<T> {

    private final T left;
    private final T right;

    public LRNode(final T left, final T right) {
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
    public String toString() {
        return "LRNode [left=" + left + ", right=" + right + "]";
    }
}
