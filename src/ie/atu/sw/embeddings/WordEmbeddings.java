package ie.atu.sw.embeddings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ie.atu.sw.console.ConsoleColour;
import ie.atu.sw.console.ConsoleInputOutput;
import ie.atu.sw.util.Array;
import ie.atu.sw.util.Vector;

public class WordEmbeddings {
    public String fileName;

    public int numberOfWords;
    public String[] words;

    public int numberOfFeatures;
    public double[][] embeddings;

    private static String getDefaultWordEmbeddingsFileName() {
        URL url = WordEmbeddings.class.getResource("./word-embeddings.txt");
        return url.getFile();
    }

    public WordEmbeddings() throws Exception {
        this(getDefaultWordEmbeddingsFileName());
    }

    public WordEmbeddings(String fileName) throws Exception {
        this.fileName = fileName;
        getWordsAndEmbeddingsFromFile();
    }

    public int getWordIndex(String word) throws Exception {
        for (int i = 0; i < this.numberOfWords; i++)
            if (this.words[i].equals(word))
                return i;

        throw new Exception("Cannot find word: '" + word + "'");
    }

    public double[] add(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.add(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    public double[] add(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.add(this.embeddings[wordIndex], embedding);
    }

    public double[] subtract(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.subtract(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    public String[] getSimilarWords(String word, int howMany) throws Exception {
        return getWords(word, howMany, true);
    }

    public String[] getSimilarWords(double[] embedding, int howMany) throws Exception {
        return getWords(embedding, howMany, true);
    }

    public String[] getDissimilarWords(String word, int howMany) throws Exception {
        return getWords(word, howMany, false);
    }

    public String[] getDissimilarWords(double[] embedding, int howMany) throws Exception {
        return getWords(embedding, howMany, false);
    }

    public String[] getWords(String word, int howMany, boolean similar) throws Exception {
        int wordIndex = getWordIndex(word);
        double[] embedding = this.embeddings[wordIndex];
        return getWords(embedding, howMany, similar);
    }

    public String[] getWords(double[] embedding, int howMany, boolean similar) throws Exception {
        double[] similarityRankings = new double[this.numberOfWords];

        for (int i = 0; i < this.numberOfWords; i++)
            similarityRankings[i] = Vector.cosineDistance(
                    embedding,
                    this.embeddings[i]);

        int[] wordIndexes;
        if (similar)
            wordIndexes = Array.getMinValuesIndexes(similarityRankings, howMany);
        else
            wordIndexes = Array.getMaxValuesIndexes(similarityRankings, howMany);

        String[] words = new String[wordIndexes.length];
        for (int i = 0; i < wordIndexes.length; i++)
            words[i] = this.words[wordIndexes[i]];

        return words;
    }

    private void getWordsAndEmbeddingsFromFile() throws Exception {
        this.numberOfWords = (int) countFileLines();
        this.words = new String[this.numberOfWords];

        try (BufferedReader buffer = new BufferedReader(new FileReader(this.fileName))) {
            readBufferLinesData(buffer);
            buffer.close();
        } catch (Exception e) {
            throw e;
        }
    }

    private long countFileLines() throws Exception {
        Path path = Paths.get(this.fileName);

        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            long numberOfWords = stream.count();

            if (numberOfWords > Integer.MAX_VALUE - 1)
                throw new Exception("There are too many words (" + numberOfWords
                        + ") in the provided word-embeddings file: " + fileName);

            return numberOfWords;
        }
    }

    private void readBufferLinesData(BufferedReader buffer) throws Exception {
        String embeddingsFileLine = buffer.readLine();

        this.numberOfFeatures = countEmbeddingsFeatures(embeddingsFileLine);
        this.embeddings = new double[this.numberOfWords][this.numberOfFeatures];

        printFileLoadingHeader();

        for (int i = 0; i < this.numberOfWords; i++) {
            setWordAndEmbeddingsValues(embeddingsFileLine, i);
            ConsoleInputOutput.printProgress(i, this.numberOfWords - 1);
            embeddingsFileLine = buffer.readLine();
        }
    }

    private int countEmbeddingsFeatures(String embeddingsLine) throws Exception {
        String[] stringArray = embeddingsLine.split(", ");
        int stringLength = stringArray.length;

        if (stringLength < 2)
            throw new Exception("The file provided is in the wrong format.");

        return stringLength - 1;
    }

    private void printFileLoadingHeader() {
        System.out.println();
        System.out.println("Loading embeddings from:\t" + this.fileName);
        System.out.println("#words:\t\t\t\t" + this.numberOfWords);
        System.out.println("#features/word:\t\t\t" + this.numberOfFeatures);
    }

    private void setWordAndEmbeddingsValues(String embeddingsFileLine, int lineNumber) throws Exception {
        String[] wordAndEmbeddings = embeddingsFileLine.split(", ");

        if (wordAndEmbeddings.length != this.numberOfFeatures + 1) {
            System.out.println(ConsoleColour.RESET + "\n");
            throw new Exception(
                    "Line #" + (lineNumber + 1) + " in " + this.fileName + " has a different format.");
        }

        this.words[lineNumber] = wordAndEmbeddings[0];

        for (int i = 0; i < this.numberOfFeatures; i++)
            this.embeddings[lineNumber][i] = Double.parseDouble(wordAndEmbeddings[i + 1]);
    }

}
