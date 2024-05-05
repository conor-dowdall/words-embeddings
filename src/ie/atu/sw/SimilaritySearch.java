package ie.atu.sw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ie.atu.sw.embedding.WordEmbeddings;
import ie.atu.sw.vector.Vector;

public class SimilaritySearch {
    public static void search() throws Exception {
        // WordEmbeddings wordEmbeddings = new WordEmbeddings();
        WordEmbeddings wordEmbeddings = new WordEmbeddings("src/word-embeddings-2.txt");

        double vector[] = new double[wordEmbeddings.numberOfWords];

        // word = funny
        // int wordIndex = 20611;
        int wordIndex = 0;

        for (int i = 0; i < wordEmbeddings.numberOfWords; i++)
            vector[i] = Vector.euclideanDistance(wordEmbeddings.embeddings[wordIndex], wordEmbeddings.embeddings[i]);

        List<String> wordsList = Arrays.asList(wordEmbeddings.words);
        ArrayList<String> sortedWordsList = new ArrayList<String>(wordsList);
        Collections.sort(sortedWordsList, Comparator.comparingDouble(s -> vector[wordsList.indexOf(s)]));

        System.out.println("Not Similar to '" + wordEmbeddings.words[wordIndex] + "'");
        System.out.println("-----------------------------");
        for (int i = wordEmbeddings.numberOfWords - 1; i > wordEmbeddings.numberOfWords - 11; i--)
            System.out.println(sortedWordsList.get(i));

        System.out.println();
        System.out.println();
        System.out.println("Similar to '" + wordEmbeddings.words[wordIndex] + "'");
        System.out.println("-----------------------------");
        for (int i = 0; i < 10; i++)
            System.out.println(sortedWordsList.get(i));

    }
}
