import ie.atu.sw.embeddings.WordEmbeddings;

public class Runner {
    public static void main(String[] args) throws Exception {
        WordEmbeddings wordEmbeddings = new WordEmbeddings();
        try {
            double[] search = wordEmbeddings.add("woman", wordEmbeddings.subtract("king", "man"));

            System.out.println("\n\nSimilar\n-------------");
            String[] similarWords = wordEmbeddings.getSimilarWords(search, 10);
            for (String word : similarWords)
                System.out.println(word);

            System.out.println("\n\nDissimilar\n-------------");

            String[] dissimilarWords = wordEmbeddings.getDissimilarWords(search, 10);
            for (String word : dissimilarWords)
                System.out.println(word);

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
