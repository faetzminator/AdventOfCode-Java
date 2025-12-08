package ch.faetzminator.aocutil;

import java.util.Objects;

public class Point3D {

    private final int x;
    private final int y;
    private final int z;

    public Point3D(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public double getXD() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getYD() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public double getZD() {
        return z;
    }

    public double getDistance(final Point3D other) {
        return Math
                .sqrt(Math.pow(other.getXD() - x, 2) + Math.pow(other.getYD() - y, 2) + Math.pow(other.getZD() - z, 2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Point3D other = (Point3D) obj;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
}
