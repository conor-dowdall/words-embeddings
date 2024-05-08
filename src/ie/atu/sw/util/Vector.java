package ie.atu.sw.util;

public class Vector {
    public static void ensureEqualLengths(double[] vector1, double[] vector2) throws Exception {
        if (vector1.length != vector2.length)
            throw new Exception("The two vector arrays must be of the same length.");
    }

    public static double[] add(double[] vector1, double[] vector2) throws Exception {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] + vector2[i];

        return result;
    }

    public static double[] subtract(double[] vector1, double[] vector2) throws Exception {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] - vector2[i];

        return result;
    }

    public static double[] multiply(double[] vector1, double[] vector2) throws Exception {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] * vector2[i];

        return result;
    }

    public static double[] divide(double[] vector1, double[] vector2) throws Exception {
        ensureEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] / vector2[i];

        return result;
    }

    public static double[] square(double[] vector) {
        double[] result = new double[vector.length];

        for (int i = 0; i < vector.length; i++)
            result[i] = vector[i] * vector[i];

        return result;
    }

    public static double sum(double[] vector) {
        double vectorSum = 0.0;
        for (double d : vector)
            vectorSum += d;

        return vectorSum;
    }

    public static double dotProduct(double[] vector1, double[] vector2) throws Exception {
        return sum(multiply(vector1, vector2));
    }

    public static double euclideanDistanceNoSqrt(double[] vector1, double[] vector2) throws Exception {
        return sum(square(subtract(vector1, vector2)));
    }

    public static double euclideanDistance(double[] vector1, double[] vector2) throws Exception {
        return Math.sqrt(euclideanDistanceNoSqrt(vector1, vector2));
    }

    public static double cosineDistance(double[] vector1, double[] vector2) throws Exception {
        return dotProduct(vector1, vector2) / Math.sqrt(sum(square(vector1)) * sum(square(vector2)));
    }
}
