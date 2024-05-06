import ie.atu.sw.embeddings.WordsEmbeddings;
import ie.atu.sw.util.SimilarityAlgorithm;

public class Runner {
    public static void main(String[] args) throws Exception {
        // WordsEmbeddings wordsEmbeddings = new WordsEmbeddings(
        // "words-embeddings-reduced.txt");
        WordsEmbeddings wordsEmbeddings = new WordsEmbeddings();

        // String search = "test";

        // double[] search = wordsEmbeddings.add("bug", "life");

        double[] search = wordsEmbeddings.multiply("woman", "power");

        // double[] search = wordsEmbeddings.add("woman",
        // wordsEmbeddings.subtract("king",
        // "man"));

        int howMany = 10;

        wordsEmbeddings.setSimilarityAlgorithm(SimilarityAlgorithm.DOT_PRODUCT);
        String[] dotStrings = wordsEmbeddings.getSimilarWords(search, howMany);

        wordsEmbeddings.setSimilarityAlgorithm(SimilarityAlgorithm.EUCLIDEAN_DISTANCE_NO_SQRT);
        String[] euclidXStrings = wordsEmbeddings.getSimilarWords(search, howMany);

        wordsEmbeddings.setSimilarityAlgorithm(SimilarityAlgorithm.EUCLIDEAN_DISTANCE);
        String[] euclidStrings = wordsEmbeddings.getSimilarWords(search, howMany);

        wordsEmbeddings.setSimilarityAlgorithm(SimilarityAlgorithm.COSINE_DISTANCE);
        String[] cosStrings = wordsEmbeddings.getSimilarWords(search, howMany);

        System.out.println("Similar Words");
        System.out.printf("%20s %20s %20s %20s\n", "DOT", "EUCLIDX", "EUCLID", "COSINE");
        System.out.printf("%20s %20s %20s %20s\n", "---", "-------", "------", "------");
        for (int i = 0; i < howMany; i++) {
            System.out.printf("%20s %20s %20s %20s\n",
                    dotStrings[i],
                    euclidXStrings[i],
                    euclidStrings[i],
                    cosStrings[i]);
        }
    }
}
