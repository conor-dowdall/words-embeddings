package ie.atu.sw.util;

public enum SimilarityAlgorithm {
    DOT_PRODUCT("Dot Product") {
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.dotProduct(vector1, vector2);
        }
    },
    EUCLIDEAN_DISTANCE_NO_SQRT("Euclidean Distance (No Square Root)") {
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.euclideanDistanceNoSqrt(vector1, vector2);
        }
    },
    EUCLIDEAN_DISTANCE("Euclidean Distance") {
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.euclideanDistance(vector1, vector2);

        }
    },
    COSINE_SIMILARITY("Cosine Similarity") {
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return Vector.cosineSimilarity(vector1, vector2);
        }
    };

    private String name;

    SimilarityAlgorithm(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public abstract double calculate(double[] vector1, double[] vector2) throws Exception;
}
