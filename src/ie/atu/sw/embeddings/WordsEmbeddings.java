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

    private SimilarityAlgorithm similarityAlgorithm;

    public WordsEmbeddings() throws Exception {
        this(getDefaultWordEmbeddingsFileName());
    }

    public WordsEmbeddings(String fileName) throws Exception {
        setFileName(fileName);
        setWordsAndEmbeddings();
        setSimilarityAlgorithm(SimilarityAlgorithm.COSINE_DISTANCE);
    }

    private static String getDefaultWordEmbeddingsFileName() {
        URL url = WordsEmbeddings.class.getResource("./words-embeddings.txt");
        return url.getFile();
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String[] getWords() {
        return words;
    }

    private void setWords(String[] words) {
        this.words = words;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    private void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public double[][] getEmbeddings() {
        return embeddings;
    }

    private void setEmbeddings(double[][] embeddings) {
        this.embeddings = embeddings;
    }

    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    private void setNumberOfFeatures(int numberOfFeatures) {
        this.numberOfFeatures = numberOfFeatures;
    }

    public SimilarityAlgorithm getSimilarityAlgorithm() {
        return similarityAlgorithm;
    }

    public void setSimilarityAlgorithm(SimilarityAlgorithm similarityAlgorithm) {
        this.similarityAlgorithm = similarityAlgorithm;
    }

    public int getWordIndex(String word) throws Exception {
        for (int i = 0; i < getNumberOfWords(); i++)
            if (getWords()[i].equals(word))
                return i;

        throw new Exception("Cannot find word: '" + word + "'");
    }

    public double[] add(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.add(getEmbeddings()[wordIndex1], getEmbeddings()[wordIndex2]);
    }

    public double[] add(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.add(getEmbeddings()[wordIndex], embedding);
    }

    public double[] add(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.add(embedding, getEmbeddings()[wordIndex]);
    }

    public double[] add(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.add(embedding1, embedding2);
    }

    public double[] subtract(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.subtract(getEmbeddings()[wordIndex1], getEmbeddings()[wordIndex2]);
    }

    public double[] subtract(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.subtract(getEmbeddings()[wordIndex], embedding);
    }

    public double[] subtract(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.subtract(embedding, getEmbeddings()[wordIndex]);
    }

    public double[] subtract(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.subtract(embedding1, embedding2);
    }

    public double[] multiply(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.multiply(getEmbeddings()[wordIndex1], getEmbeddings()[wordIndex2]);
    }

    public double[] multiply(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.multiply(getEmbeddings()[wordIndex], embedding);
    }

    public double[] multiply(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.multiply(embedding, getEmbeddings()[wordIndex]);
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
        double[] embedding = getEmbeddings()[wordIndex];
        return getSimilarWords(embedding, howMany, similar);
    }

    public String[] getSimilarWords(double[] embedding, int howMany, boolean similar) throws Exception {
        double[] similarityRankings = new double[getNumberOfWords()];

        for (int i = 0; i < getNumberOfWords(); i++)
            similarityRankings[i] = getSimilarityAlgorithm().calculate(embedding, getEmbeddings()[i]);

        boolean useMinimums = (getSimilarityAlgorithm() == SimilarityAlgorithm.COSINE_DISTANCE
                || getSimilarityAlgorithm() == SimilarityAlgorithm.DOT_PRODUCT) ? !similar : similar;

        int[] wordIndexes = useMinimums ? Array.getMinValuesIndexes(similarityRankings, howMany)
                : Array.getMaxValuesIndexes(similarityRankings, howMany);

        String[] words = new String[wordIndexes.length];
        for (int i = 0; i < wordIndexes.length; i++)
            words[i] = getWords()[wordIndexes[i]];

        return words;
    }

    public void setWordsAndEmbeddings() throws Exception {
        setNumberOfWords((int) countFileLines());
        setWords(new String[getNumberOfWords()]);

        try (BufferedReader buffer = new BufferedReader(new FileReader(getFileName()))) {
            readBufferLinesData(buffer);
            buffer.close();
        }
    }

    private long countFileLines() throws Exception {
        Path path = Paths.get(getFileName());

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

        setNumberOfFeatures(countEmbeddingsFeatures(embeddingsFileLine));
        setEmbeddings(new double[getNumberOfWords()][getNumberOfFeatures()]);

        printFileLoadingHeader();

        for (int i = 0; i < getNumberOfWords(); i++) {
            setWordAndEmbeddingsValues(embeddingsFileLine, i);
            ConsoleProgressMeter.printProgress(i, getNumberOfWords() - 1);
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
        System.out.println("Loading embeddings from:\t" + getFileName());
        System.out.println("#words:\t\t\t\t" + getNumberOfWords());
        System.out.println("#features/word:\t\t\t" + getNumberOfFeatures());
    }

    private void setWordAndEmbeddingsValues(String embeddingsFileLine, int lineNumber) throws Exception {
        String[] wordAndEmbeddings = embeddingsFileLine.split(", ");

        if (wordAndEmbeddings.length != getNumberOfFeatures() + 1) {
            System.out.println(ConsoleColour.RESET + "\n");
            throw new Exception(
                    "Line #" + (lineNumber + 1) + " in " + getFileName() + " has a different format.");
        }

        getWords()[lineNumber] = wordAndEmbeddings[0];

        for (int i = 0; i < getNumberOfFeatures(); i++)
            getEmbeddings()[lineNumber][i] = Double.parseDouble(wordAndEmbeddings[i + 1]);
    }

}
