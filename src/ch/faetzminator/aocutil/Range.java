package ch.faetzminator.aocutil;

public class Range {

    private long start;
    private long end;

    public Range(final long start, final long end) {
        if (end < start) {
            throw new IllegalArgumentException("start " + start + " greather than end " + end);
        }
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void move(final long by) {
        start += by;
        end += by;
    }

    public Range split(final long upperStart) {
        final Range upperRange = new Range(upperStart, end);
        end = upperStart - 1;
        return upperRange;
    }

    @Override
    public String toString() {
        return "Range [start=" + start + ", end=" + end + "]";
    }
}
