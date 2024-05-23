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

/**
 * <p>
 * load-and-interact-with a words-embeddings file
 * </p>
 * <ul>
 * <li>csv format detection</li>
 * <li>word vector operations (search, add, subtract, multiply, divide)</li>
 * <li>similarity algorithms to find similar/dissimilar words</li>
 * </ul>
 */
public class WordsEmbeddings {

    private String fileName;
    private String delimiter;

    private String[] words;
    private int numberOfWords;

    private double[][] embeddings;
    private int numberOfFeatures;

    private String[] previousSimilarWords;
    private double[] previousSimilarWordsScores;

    private SimilarityAlgorithm similarityAlgorithm = SimilarityAlgorithm.COSINE_SIMILARITY;

    /**
     * <p>
     * create a WordsEmbeddings instance by loading a words-embeddings file from
     * a file name
     * </p>
     * <p>
     * stores the file name
     * </p>
     * <p>
     * loads the words, and word vectors from the file name
     * </p>
     * 
     * @param fileName - a file name to load words-embeddings from
     * @throws Exception
     */
    public WordsEmbeddings(String fileName) throws Exception {
        setFileName(fileName);
    }

    /**
     * get the loaded words-embeddings file name
     * 
     * @return the loaded words-embeddings file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * set, and load, a words-embeddings file name
     * 
     * @param fileName - a file name to load words-embeddings from
     * @throws Exception
     */
    private void setFileName(String fileName) throws Exception {
        this.fileName = fileName;
        setWordsAndEmbeddings();
    }

    /**
     * get the words array, which was loaded from the words-embeddings file
     * 
     * @return the words array, which was loaded from the words-embeddings file
     */
    public String[] getWords() {
        return words;
    }

    /**
     * get the number of words loaded from the words-embeddings file
     * 
     * @return the number of words loaded from the words-embeddings file
     */
    public int getNumberOfWords() {
        return numberOfWords;
    }

    /**
     * get the array of words-embeddings arrays
     * 
     * @return the words-embeddings array loaded from the words-embeddings file
     */
    public double[][] getEmbeddings() {
        return embeddings;
    }

    /**
     * get the number of data points/features that were loaded from the
     * words-embeddings file
     * 
     * @return the number of data points/features that were loaded from the
     *         words-embeddings file
     */
    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    /**
     * get the current similarity algorithm, e.g. dot product; cosine similarity
     * 
     * @return the current similarity algorithm - defaults to cosine similarity
     */
    public SimilarityAlgorithm getSimilarityAlgorithm() {
        return similarityAlgorithm;
    }

    /**
     * set the current similarity algorithm, e.g. dot product; cosine similarity
     */
    public void setSimilarityAlgorithm(SimilarityAlgorithm similarityAlgorithm) {
        this.similarityAlgorithm = similarityAlgorithm;
    }

    /**
     * get the words string-array of the latest similar-or-dissimilar words search
     * 
     * @return the words string-array of the latest similar-or-dissimilar words
     *         search
     */
    public String[] getPreviousSimilarWords() {
        return previousSimilarWords;
    }

    /**
     * get the double-array scores-results of the latest similar-or-dissimilar words
     * search
     * 
     * @return the double-array scores-results of the latest similar-or-dissimilar
     *         words search
     */
    public double[] getPreviousSimilarWordsScores() {
        return previousSimilarWordsScores;
    }

    /**
     * get the index of a word from the array of words that were loaded from the
     * words-embeddings file
     * 
     * @param word - the word to find
     * @return the index of the word to find in the array of words that were loaded
     *         from the words-embeddings file
     * @throws Exception if the word is not found
     */
    public int getWordIndex(String word) throws Exception {
        for (int i = 0; i < this.numberOfWords; i++)
            if (this.words[i].equals(word))
                return i;

        throw new Exception("Cannot find word: '" + word + "'");
    }

    /**
     * get the word-embedding array representing a word, which was loaded from the
     * words-embeddings file
     * 
     * @param word - the word to find an embedding for
     * @return a word-embedding array representing a word
     * @throws Exception if the word is not found
     */
    public double[] getWordEmbedding(String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return this.embeddings[wordIndex];
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word1 - a string to find in the loaded words-embeddings file
     * @param word2 - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] add(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.add(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word      - a string to find in the loaded words-embeddings file
     * @param embedding - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] add(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.add(this.embeddings[wordIndex], embedding);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding - a vector representation of a word
     * @param word      - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] add(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.add(embedding, this.embeddings[wordIndex]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding1 - a vector representation of a word
     * @param embedding2 - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if the word-vector arrays are not of the same length
     */
    public double[] add(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.add(embedding1, embedding2);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word1 - a string to find in the loaded words-embeddings file
     * @param word2 - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] subtract(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.subtract(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word      - a string to find in the loaded words-embeddings file
     * @param embedding - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] subtract(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.subtract(this.embeddings[wordIndex], embedding);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding - a vector representation of a word
     * @param word      - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] subtract(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.subtract(embedding, this.embeddings[wordIndex]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding1 - a vector representation of a word
     * @param embedding2 - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if the word-vector arrays are not of the same length
     */
    public double[] subtract(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.subtract(embedding1, embedding2);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word1 - a string to find in the loaded words-embeddings file
     * @param word2 - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] multiply(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.multiply(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word      - a string to find in the loaded words-embeddings file
     * @param embedding - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] multiply(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.multiply(this.embeddings[wordIndex], embedding);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding - a vector representation of a word
     * @param word      - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] multiply(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.multiply(embedding, this.embeddings[wordIndex]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding1 - a vector representation of a word
     * @param embedding2 - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if the word-vector arrays are not of the same length
     */
    public double[] multiply(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.multiply(embedding1, embedding2);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word1 - a string to find in the loaded words-embeddings file
     * @param word2 - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] divide(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return Vector.divide(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param word      - a string to find in the loaded words-embeddings file
     * @param embedding - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] divide(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.divide(this.embeddings[wordIndex], embedding);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding - a vector representation of a word
     * @param word      - a string to find in the loaded words-embeddings file
     * @return the result of the vector operation
     * @throws Exception if a word is not found, or if the word-vector arrays are
     *                   not of the same length
     */
    public double[] divide(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return Vector.divide(embedding, this.embeddings[wordIndex]);
    }

    /**
     * perform a vector operation on vector representations of words
     * 
     * @param embedding1 - a vector representation of a word
     * @param embedding2 - a vector representation of a word
     * @return the result of the vector operation
     * @throws Exception if the word-vector arrays are not of the same length
     */
    public double[] divide(double[] embedding1, double[] embedding2) throws Exception {
        return Vector.divide(embedding1, embedding2);
    }

    /**
     * get 'howMany' similar words to a word that was loaded from the
     * words-embeddings file
     * 
     * @param word    - the word to search for
     * @param howMany - the number of similarities to find
     * @return an array of similar words, found using the set similarity algorithm
     * @throws Exception
     */
    public String[] getSimilarWords(String word, int howMany) throws Exception {
        return getSimilarWords(word, howMany, true);
    }

    /**
     * * get 'howMany' similar words to a vector representation of a word
     * 
     * @param embedding - the vector representation of a word
     * @param howMany   - the number of similarities to find
     * @return an array of similar words, found using the set similarity algorithm
     * @throws Exception
     */
    public String[] getSimilarWords(double[] embedding, int howMany) throws Exception {
        return getSimilarWords(embedding, howMany, true);
    }

    /**
     * get 'howMany' dissimilar words to a word that was loaded from the
     * words-embeddings file
     * 
     * @param word    - the word to search for
     * @param howMany - the number of similarities to find
     * @return an array of disssimilar words, found using the set similarity
     *         algorithm
     * @throws Exception
     */
    public String[] getDissimilarWords(String word, int howMany) throws Exception {
        return getSimilarWords(word, howMany, false);
    }

    /**
     * * get 'howMany' dissimilar words to a vector representation of a word
     * 
     * @param embedding - the vector representation of a word
     * @param howMany   - the number of similarities to find
     * @return an array of dissimilar words, found using the set similarity
     *         algorithm
     * @throws Exception
     */
    public String[] getDissimilarWords(double[] embedding, int howMany) throws Exception {
        return getSimilarWords(embedding, howMany, false);
    }

    /**
     * get 'howMany' similar-or-dissimilar words to a word that was loaded from the
     * words-embeddings file, depending on the 'similar' boolean
     * 
     * @param word    - the word to search for
     * @param howMany - the number of similarities to find
     * @param similar - search for similar or dissimilar words?
     * @return an array of similar-or-dissimilar words, found using the set
     *         similarity algorithm
     * @throws Exception
     */
    public String[] getSimilarWords(String word, int howMany, boolean similar) throws Exception {
        int wordIndex = getWordIndex(word);
        double[] embedding = this.embeddings[wordIndex];
        return getSimilarWords(embedding, howMany, similar);
    }

    /**
     * get 'howMany' similar-or-dissimilar words to a vector representation of a
     * word, depending on the 'similar' boolean
     * 
     * @param embedding - the vector representation of a word
     * @param howMany   - the number of similarities to find
     * @param similar   - search for similar or dissimilar words?
     * @return an array of similar-or-dissimilar words, found using the set
     *         similarity algorithm
     * @throws Exception
     */
    public String[] getSimilarWords(double[] embedding, int howMany, boolean similar) throws Exception {
        double[] similarityScores = new double[this.numberOfWords];

        for (int i = 0; i < this.numberOfWords; i++)
            similarityScores[i] = this.similarityAlgorithm.calculate(embedding, this.embeddings[i]);

        // Euclidean Distances use smallest values for best similarity
        // and largest for best dissimilarity
        // Others use opposite
        boolean usingEuclidean = this.similarityAlgorithm == SimilarityAlgorithm.EUCLIDEAN_DISTANCE
                || this.similarityAlgorithm == SimilarityAlgorithm.EUCLIDEAN_DISTANCE_NO_SQRT;
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

    /**
     * after setting a words-embeddings file name, load all relevant data from that
     * file, including number of words; the words themselves; and the
     * words-embeddings vectors
     * 
     * @throws Exception
     */
    public void setWordsAndEmbeddings() throws Exception {
        this.numberOfWords = (int) countFileLines();
        this.words = new String[this.numberOfWords];

        try (BufferedReader buffer = new BufferedReader(new FileReader(this.fileName))) {
            readBufferLinesData(buffer);
            buffer.close();
        }
    }

    /**
     * count the number of lines/number-of-words in the provided words-embeddings
     * file
     * 
     * @return the number of lines/number-of-words in the provided words-embeddings
     *         file
     * @throws Exception
     */
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

    /**
     * read each line in the provided words-embeddings file, and set relevant data,
     * e.g. number of vector features; size of the embeddings arrays needed
     * 
     * @param buffer - a buffer of the data in the provided words-embeddings file
     * @throws Exception
     */
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

    /**
     * check if a string is delimited by a comma-and-space; a comma; or a space; and
     * store the value for later use
     * 
     * @param delimitedString
     * @return the array of split strings, split with the found delimiter
     * @throws Exception if the delimiter is not detected
     */
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

    /**
     * count the number of features in the vector representations of the words in
     * the loaded words-embeddings file
     * 
     * @param embeddingsLine - one line from the loaded words-embeddings file
     * @return the number of features in the vector representations of the words
     * @throws Exception
     */
    private int countEmbeddingsFeatures(String embeddingsLine) throws Exception {
        String[] stringArray = splitWithCommasOrSpaces(embeddingsLine);
        int stringLength = stringArray.length;

        return stringLength - 1;
    }

    /**
     * print information detected in the loaded words-embeddings file
     */
    private void printFileLoadingHeader() {
        System.out.println();
        System.out.println("Loading Embeddings From:\t" + this.fileName);
        System.out.println("Delimiter:\t\t\t'" + this.delimiter + "'");
        System.out.println("#Words:\t\t\t\t" + this.numberOfWords);
        System.out.println("#Features/Word:\t\t\t" + this.numberOfFeatures);
    }

    /**
     * set the words, and word-vectors, after loading a words-embeddings file
     * 
     * @param embeddingsFileLine - a line from the words-embeddings file
     * @param lineNumber         - the line number that is currently being read
     * @throws Exception if a line has a different number of values than the first
     *                   read line
     */
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
