package ch.faetzminator.aocutil;

import java.math.BigInteger;

public final class MathUtil {

    private MathUtil() {

    }

    public static long lcm(long one, long another) {
        return lcm(BigInteger.valueOf(one), BigInteger.valueOf(another)).longValueExact();
    }

    public static BigInteger lcm(final BigInteger one, final BigInteger another) {
        final BigInteger gcd = one.gcd(another);
        final BigInteger absProduct = one.multiply(another).abs();
        return absProduct.divide(gcd);
    }
}
