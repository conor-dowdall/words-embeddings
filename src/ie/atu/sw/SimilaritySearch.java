package ie.atu.sw;

import ie.atu.sw.embedding.WordEmbeddings;

public class SimilaritySearch {
    public static void search() throws Exception {
        WordEmbeddings wordEmbeddings = new WordEmbeddings("src/word-embeddings-2.txt");
        System.out.println("##########");
        for (int i = 0; i < wordEmbeddings.words.length; i++)
            System.out.println(wordEmbeddings.words[i] + ", " + wordEmbeddings.embeddings[i][0]);
        System.out.println("##########");
        // WordEmbeddings wordEmbeddings = new WordEmbeddings();

    }
}
