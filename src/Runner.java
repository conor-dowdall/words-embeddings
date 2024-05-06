import ie.atu.sw.embeddings.WordEmbeddings;

public class Runner {
    public static void main(String[] args) throws Exception {
        WordEmbeddings wordEmbeddings = new WordEmbeddings();

        double[] search = wordEmbeddings.add("woman", wordEmbeddings.subtract("king",
                "man"));

        System.out.println("\n\nSimilar\n-------------");
        // changed to cosine distance, which has reversed word rankings!
        String[] similarWords = wordEmbeddings.getDissimilarWords(search, 10);
        for (String word : similarWords)
            System.out.println(word);
    }
}
