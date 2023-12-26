package ch.faetzminator.aocutil;

public class MovingPosition {

    private final long px;
    private final long vx;

    private final double m;
    private final double b;

    public MovingPosition(final long px, final long py, final long vx, final long vy) {
        this.px = px;
        this.vx = vx;

        m = ((double) vy) / ((double) vx);
        b = -(m * px) + py;
    }

    public double getM() {
        return m;
    }

    public double getB() {
        return b;
    }

    public double getCollisionX(final MovingPosition other) {
        return (other.getB() - b) / (m - other.getM());
    }

    public double getY(final double x) {
        return m * x + b;
    }

    public boolean inPast(final double x) {
        return vx > 0 ? x < px : x > px;
    }

    @Override
    public String toString() {
        return "MovingPosition [px=" + px + ", vx=" + vx + ", m=" + m + ", b=" + b + "]";
    }
}
