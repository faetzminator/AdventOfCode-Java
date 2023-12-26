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

    public long getLength() {
        return end - start + 1;
    }

    public void move(final long by) {
        start += by;
        end += by;
    }

    public Range splitUpper(final long upperStart) {
        if (upperStart > end) {
            return null;
        }
        final Range upperRange = new Range(upperStart, end);
        end = upperStart - 1;
        return upperRange;
    }

    public Range splitLower(final long lowerEnd) {
        if (lowerEnd < start) {
            return null;
        }
        final Range lowerRange = new Range(start, lowerEnd);
        start = lowerEnd + 1;
        return lowerRange;
    }

    public Range copy() {
        return new Range(start, end);
    }

    @Override
    public String toString() {
        return "Range [start=" + start + ", end=" + end + "]";
    }
}
