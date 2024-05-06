package ie.atu.sw.util;

public enum SimilarityAlgorithm {
    DOT_PRODUCT {
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.dotProduct(vector1, vector2);
        }
    },
    EUCLIDEAN_DISTANCE_NO_SQRT {
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.euclideanDistanceNoSqrt(vector1, vector2);
        }
    },
    EUCLIDEAN_DISTANCE {
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.euclideanDistance(vector1, vector2);

        }
    },
    COSINE_DISTANCE {
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.cosineDistance(vector1, vector2);
        }
    };

    public abstract double calculate(double[] vector1, double[] vector2) throws Exception;
}
