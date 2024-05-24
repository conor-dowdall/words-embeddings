package ie.atu.sw.util;

/**
 * utility class with static methods for vector operations like add, subtract,
 * dot product
 */
public class Vector {
    /**
     * assert that two vectors have equal lengths
     * 
     * @param vector1
     * @param vector2
     * @throws Exception if the vectors have different lengths
     */
    public static void assertEqualLengths(double[] vector1, double[] vector2) throws Exception {
        if (vector1.length != vector2.length)
            throw new Exception("The two vector arrays must be of the same length.");
    }

    /**
     * add two vectors
     * 
     * @param vector1
     * @param vector2
     * @return the result of the vector operation
     * @throws Exception if the vectors have different lengths
     */
    public static double[] add(double[] vector1, double[] vector2) throws Exception {
        assertEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] + vector2[i];

        return result;
    }

    /**
     * subtract two vectors
     * 
     * @param vector1
     * @param vector2
     * @return the result of the vector operation
     * @throws Exception if the vectors have different lengths
     */
    public static double[] subtract(double[] vector1, double[] vector2) throws Exception {
        assertEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] - vector2[i];

        return result;
    }

    /**
     * multiply two vectors
     * 
     * @param vector1
     * @param vector2
     * @return the result of the vector operation
     * @throws Exception if the vectors have different lengths
     */
    public static double[] multiply(double[] vector1, double[] vector2) throws Exception {
        assertEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] * vector2[i];

        return result;
    }

    /**
     * divide a vector by another
     * 
     * @param vector1
     * @param vector2
     * @return the result of the vector operation
     * @throws Exception if the vectors have different lengths
     */
    public static double[] divide(double[] vector1, double[] vector2) throws Exception {
        assertEqualLengths(vector1, vector2);

        double[] result = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] / vector2[i];

        return result;
    }

    /**
     * multiply a vector by itself
     * 
     * @param vector
     * @return the result of the vector operation
     */
    public static double[] square(double[] vector) {
        double[] result = new double[vector.length];

        for (int i = 0; i < vector.length; i++)
            result[i] = vector[i] * vector[i];

        return result;
    }

    /**
     * sum all the values in a vector
     * 
     * @param vector
     * @return the value of the sum of the vector
     */
    public static double sum(double[] vector) {
        double vectorSum = 0.0;
        for (double d : vector)
            vectorSum += d;

        return vectorSum;
    }

    /**
     * calculate the dot product of two vectors
     * 
     * @param vector1
     * @param vector2
     * @return the dot product of two vectors
     * @throws Exception if the vectors have different lengths
     */
    public static double dotProduct(double[] vector1, double[] vector2) throws Exception {
        return sum(multiply(vector1, vector2));
    }

    /**
     * calculate the euclidean distance between two vectors, without applying the
     * final square-root operation
     * 
     * @param vector1
     * @param vector2
     * @return the euclidean distance between two vectors (no square root)
     * @throws Exception if the vectors have different lengths
     */
    public static double euclideanDistanceNoSqrt(double[] vector1, double[] vector2) throws Exception {
        return sum(square(subtract(vector1, vector2)));
    }

    /**
     * calculate the euclidean distance between two vectors
     * 
     * @param vector1
     * @param vector2
     * @return the euclidean distance between two vectors
     * @throws Exception if the vectors have different lengths
     */
    public static double euclideanDistance(double[] vector1, double[] vector2) throws Exception {
        return Math.sqrt(euclideanDistanceNoSqrt(vector1, vector2));
    }

    /**
     * calculate the cosine similarity of two vectors
     * 
     * @param vector1
     * @param vector2
     * @return the cosine similarity of two vectors
     * @throws Exception if the vectors have different lengths
     */
    public static double cosineSimilarity(double[] vector1, double[] vector2) throws Exception {
        return dotProduct(vector1, vector2) / Math.sqrt(sum(square(vector1)) * sum(square(vector2)));
    }
}
