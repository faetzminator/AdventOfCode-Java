package ch.faetzminator.aocutil;

public class MovingPosition {

    private final long px;
    private final long py;
    private final long vx;
    private final long vy;

    private final double m;
    private final double b;

    public MovingPosition(final long px, final long py, final long vx, final long vy) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;

        m = ((double) vy) / ((double) vx);
        b = -(m * px) + py;
    }

    public long getPx() {
        return px;
    }

    public double getPxD() {
        return px;
    }

    public long getPy() {
        return py;
    }

    public double getPyD() {
        return py;
    }

    public long getVx() {
        return vx;
    }

    public double getVxD() {
        return vx;
    }

    public long getVy() {
        return vy;
    }

    public double getVyD() {
        return vy;
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
