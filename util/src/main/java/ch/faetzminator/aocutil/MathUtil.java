package ch.faetzminator.aocutil;

import java.math.BigInteger;

public final class MathUtil {

    private MathUtil() {

    }

    public static int gcd(final int one, final int... others) {
        int result = one;
        for (final int another : others) {
            result = gcd(result, another);
        }
        return result;
    }

    public static int gcd(final int one, final int another) {
        return BigInteger.valueOf(one).gcd(BigInteger.valueOf(another)).intValueExact();
    }

    public static long lcm(final long one, final long another) {
        return lcm(BigInteger.valueOf(one), BigInteger.valueOf(another)).longValueExact();
    }

    public static BigInteger lcm(final BigInteger one, final BigInteger another) {
        final BigInteger gcd = one.gcd(another);
        final BigInteger absProduct = one.multiply(another).abs();
        return absProduct.divide(gcd);
    }

    private static final double EPSILON = 1e-10;

    public static double[] gaussianElimination(final double[][] A, final double[] b) {
        // https://introcs.cs.princeton.edu/java/95linear/GaussianElimination.java.html
        // Copyright © 2000–2022, Robert Sedgewick and Kevin Wayne.
        final int n = b.length;

        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            final double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            final double t = b[p];
            b[p] = b[max];
            b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                final double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        final double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

    public static long pow2(final int exponent) {
        return (long) Math.pow(2, exponent);
    }

    public static long pow10(final int exponent) {
        return (long) Math.pow(10, exponent);
    }

    public static int countDigits(final long number) {
        return 1 + (int) Math.log10(number);
    }
}
