package ie.atu.sw.vector;

public class Vector {
    public static void ensureEqualLengths(double[] vector1, double[] vector2) throws ArrayIndexOutOfBoundsException {
        if (vector1.length != vector2.length)
            throw new ArrayIndexOutOfBoundsException("The two vector arrays must be of the same length.");
    }

    public static double[] add(double[] vector1, double[] vector2) {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] + vector2[i];

        return result;
    }

    public static double[] subtract(double[] vector1, double[] vector2) {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] - vector2[i];

        return result;
    }

    public static double[] multiply(double[] vector1, double[] vector2) {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] * vector2[i];

        return result;
    }

    public static double[] square(double[] vector) {
        double[] result = new double[vector.length];

        for (int i = 0; i < vector.length; i++)
            result[i] = vector[i] * vector[i];

        return result;
    }

    public static double sum(double[] vector) {
        double vectorSum = 0;
        for (double d : vector)
            vectorSum += d;

        return vectorSum;
    }

    public static double dotProduct(double[] vector1, double[] vector2) {
        double[] multiplyVector = multiply(vector1, vector2);
        return sum(multiplyVector);
    }

    public static double euclideanDistance(double[] vector1, double[] vector2) {
        double[] subtractVector = subtract(vector1, vector2);
        double[] squareVector = square(subtractVector);
        return sum(squareVector);
    }
}
