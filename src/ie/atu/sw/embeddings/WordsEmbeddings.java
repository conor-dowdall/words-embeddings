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
import ie.atu.sw.console.ConsoleProgressMeter;
import ie.atu.sw.util.Array;
import ie.atu.sw.util.SimilarityAlgorithm;
import ie.atu.sw.util.Vector;

public class WordsEmbeddings {

    private String fileName;

    private String[] words;
    private int numberOfWords;

    private double[][] embeddings;
    private int numberOfFeatures;

    private SimilarityAlgorithm similarityAlgorithm = SimilarityAlgorithm.COSINE_DISTANCE;

    public WordsEmbeddings() throws Exception {
        this(getDefaultWordEmbeddingsFileName());
    }

    public WordsEmbeddings(String fileName) throws Exception {
        setFileName(fileName);
    }

    private static String getDefaultWordEmbeddingsFileName() {
        URL url = WordsEmbeddings.class.getResource("./words-embeddings.txt");
        return url.getFile();
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) throws Exception {
        this.fileName = fileName;
        setWordsAndEmbeddings();
    }

    public String[] getWords() {
        return words;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public double[][] getEmbeddings() {
        return embeddings;
    }

    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    public SimilarityAlgorithm getSimilarityAlgorithm() {
        return similarityAlgorithm;
    }

    public void setSimilarityAlgorithm(SimilarityAlgorithm similarityAlgorithm) {
        this.similarityAlgorithm = similarityAlgorithm;
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

    public double[] add(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.add(embedding, this.embeddings[wordIndex]);
    }

    public double[] add(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.add(embedding1, embedding2);
    }

    public double[] subtract(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.subtract(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    public double[] subtract(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.subtract(this.embeddings[wordIndex], embedding);
    }

    public double[] subtract(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.subtract(embedding, this.embeddings[wordIndex]);
    }

    public double[] subtract(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.subtract(embedding1, embedding2);
    }

    public double[] multiply(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.multiply(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    public double[] multiply(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.multiply(this.embeddings[wordIndex], embedding);
    }

    public double[] multiply(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.multiply(embedding, this.embeddings[wordIndex]);
    }

    public double[] multiply(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.multiply(embedding1, embedding2);
    }

    public String[] getSimilarWords(String word, int howMany) throws Exception {
        return getSimilarWords(word, howMany, true);
    }

    public String[] getSimilarWords(double[] embedding, int howMany) throws Exception {
        return getSimilarWords(embedding, howMany, true);
    }

    public String[] getDissimilarWords(String word, int howMany) throws Exception {
        return getSimilarWords(word, howMany, false);
    }

    public String[] getDissimilarWords(double[] embedding, int howMany) throws Exception {
        return getSimilarWords(embedding, howMany, false);
    }

    public String[] getSimilarWords(String word, int howMany, boolean similar) throws Exception {
        int wordIndex = getWordIndex(word);
        double[] embedding = this.embeddings[wordIndex];
        return getSimilarWords(embedding, howMany, similar);
    }

    public String[] getSimilarWords(double[] embedding, int howMany, boolean similar) throws Exception {
        double[] similarityRankings = new double[this.numberOfWords];

        for (int i = 0; i < this.numberOfWords; i++)
            similarityRankings[i] = this.similarityAlgorithm.calculate(embedding, this.embeddings[i]);

        boolean useMinimums = (this.similarityAlgorithm == SimilarityAlgorithm.COSINE_DISTANCE
                || this.similarityAlgorithm == SimilarityAlgorithm.DOT_PRODUCT) ? !similar : similar;

        int[] wordIndexes = useMinimums ? Array.getMinValuesIndexes(similarityRankings, howMany)
                : Array.getMaxValuesIndexes(similarityRankings, howMany);

        String[] words = new String[wordIndexes.length];
        for (int i = 0; i < wordIndexes.length; i++)
            words[i] = this.words[wordIndexes[i]];

        return words;
    }

    public void setWordsAndEmbeddings() throws Exception {
        this.numberOfWords = (int) countFileLines();
        this.words = new String[this.numberOfWords];

        try (BufferedReader buffer = new BufferedReader(new FileReader(this.fileName))) {
            readBufferLinesData(buffer);
            buffer.close();
        }
    }

    private long countFileLines() throws Exception {
        Path path = Paths.get(this.fileName);

        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            long numberOfWords = stream.count();

            if (numberOfWords > Integer.MAX_VALUE - 1)
                throw new Exception("There are too many words (" + numberOfWords
                        + ") in the provided word-embeddings file: " + getFileName());

            return numberOfWords;
        }
    }

    private void readBufferLinesData(BufferedReader buffer) throws Exception {
        String embeddingsFileLine = buffer.readLine();

        this.numberOfFeatures = countEmbeddingsFeatures(embeddingsFileLine);
        this.embeddings = new double[this.numberOfWords][getNumberOfFeatures()];

        printFileLoadingHeader();

        for (int i = 0; i < this.numberOfWords; i++) {
            setWordAndEmbeddingsValues(embeddingsFileLine, i);
            ConsoleProgressMeter.printProgress(i, this.numberOfWords - 1);
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
        System.out.println("Loading Embeddings From:\t" + this.fileName);
        System.out.println("#Words:\t\t\t\t" + this.numberOfWords);
        System.out.println("#Features/Word:\t\t\t" + this.numberOfFeatures);
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
