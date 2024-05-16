package ie.atu.sw.embeddings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ie.atu.sw.console.ConsoleColor;
import ie.atu.sw.console.ConsoleProgressMeter;
import ie.atu.sw.util.Array;
import ie.atu.sw.util.SimilarityAlgorithm;
import ie.atu.sw.util.Vector;

public class WordsEmbeddings {

    private String fileName;
    private String delimiter;

    private String[] words;
    private int numberOfWords;

    private double[][] embeddings;
    private int numberOfFeatures;

    private String[] previousSimilarWords;
    private double[] previousSimilarWordsScores;

    private SimilarityAlgorithm similarityAlgorithm = SimilarityAlgorithm.COSINE_DISTANCE;

    public WordsEmbeddings(String fileName) throws Exception {
        setFileName(fileName);
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

    public String[] getPreviousSimilarWords() {
        return previousSimilarWords;
    }

    public double[] getPreviousSimilarWordsScores() {
        return previousSimilarWordsScores;
    }

    public int getWordIndex(String word) throws Exception {
        for (int i = 0; i < this.numberOfWords; i++)
            if (this.words[i].equals(word))
                return i;

        throw new Exception("Cannot find word: '" + word + "'");
    }

    public double[] getWordEmbedding(String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return this.embeddings[wordIndex];
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

    public double[] divide(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.divide(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    public double[] divide(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.divide(this.embeddings[wordIndex], embedding);
    }

    public double[] divide(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.divide(embedding, this.embeddings[wordIndex]);
    }

    public double[] divide(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.divide(embedding1, embedding2);
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
        double[] similarityScores = new double[this.numberOfWords];

        for (int i = 0; i < this.numberOfWords; i++)
            similarityScores[i] = this.similarityAlgorithm.calculate(embedding, this.embeddings[i]);

        boolean usingEuclidean = this.similarityAlgorithm == SimilarityAlgorithm.EUCLIDEAN_DISTANCE
                || this.similarityAlgorithm == SimilarityAlgorithm.EUCLIDEAN_DISTANCE_NO_SQRT;

        // Euclidean Distances use smallest values for best similarity
        // and largest for best dissimilarity
        // Others use opposite
        boolean useMinimums = usingEuclidean ? similar : !similar;

        int[] wordIndexes = useMinimums ? Array.getMinValuesIndexes(similarityScores, howMany)
                : Array.getMaxValuesIndexes(similarityScores, howMany);

        String[] similarWords = new String[wordIndexes.length];
        double[] similarWordsScores = new double[wordIndexes.length];

        for (int i = 0; i < wordIndexes.length; i++) {
            similarWords[i] = this.words[wordIndexes[i]];
            similarWordsScores[i] = similarityScores[wordIndexes[i]];
        }

        this.previousSimilarWords = similarWords;
        this.previousSimilarWordsScores = similarWordsScores;

        return similarWords;
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

    private String[] splitWithCommasOrSpaces(String delimitedString) throws Exception {
        String[] delimiters = {
                ", ",
                ",",
                " "
        };

        String[] splitString = null;
        String tempDelimiter = null;

        for (String delimiter : delimiters) {
            splitString = delimitedString.split(delimiter);
            if (splitString.length > 1) {
                tempDelimiter = delimiter;
                break;
            }
        }

        if (tempDelimiter == null)
            throw new Exception("Cannot determine the delimiter for the input data.");

        this.delimiter = tempDelimiter;
        return splitString;
    }

    private int countEmbeddingsFeatures(String embeddingsLine) throws Exception {
        String[] stringArray = splitWithCommasOrSpaces(embeddingsLine);
        int stringLength = stringArray.length;

        return stringLength - 1;
    }

    private void printFileLoadingHeader() {
        System.out.println();
        System.out.println("Loading Embeddings From:\t" + this.fileName);
        System.out.println("Delimiter:\t\t\t'" + this.delimiter + "'");
        System.out.println("#Words:\t\t\t\t" + this.numberOfWords);
        System.out.println("#Features/Word:\t\t\t" + this.numberOfFeatures);
    }

    private void setWordAndEmbeddingsValues(String embeddingsFileLine, int lineNumber) throws Exception {
        String[] wordAndEmbeddings = embeddingsFileLine.split(this.delimiter);

        if (wordAndEmbeddings.length != this.numberOfFeatures + 1) {
            System.out.println(ConsoleColor.RESET + "\n");
            throw new Exception(
                    "Line #" + (lineNumber + 1) + " in " + this.fileName + " has a different format.");
        }

        this.words[lineNumber] = wordAndEmbeddings[0];

        for (int i = 0; i < this.numberOfFeatures; i++)
            this.embeddings[lineNumber][i] = Double.parseDouble(wordAndEmbeddings[i + 1]);
    }

}
