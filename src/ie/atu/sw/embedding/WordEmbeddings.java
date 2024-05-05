package ie.atu.sw.embedding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ie.atu.sw.console.ConsoleColour;
import ie.atu.sw.console.ConsoleInputOutput;

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

    private long countFileLines() throws IOException {
        Path path = Paths.get(this.fileName);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            return stream.count();
        }
    }

    // if (numberOfWords > Integer.MAX_VALUE - 1)
    // throw new ArrayIndexOutOfBoundsException(
    // "There are too many words (" + numberOfWords + ") in the provided
    // word-embeddings file: "
    // + fileName);

    private void readBufferLinesData(BufferedReader buffer) throws IOException {
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

    private int countEmbeddingsFeatures(String embeddingsLine) {
        String[] stringArray = embeddingsLine.split(", ");
        int stringLength = stringArray.length;
        if (stringLength < 2)
            throw new IllegalStateException("The file provided is in the wrong format.");
        return stringLength - 1;
    }

    private void printFileLoadingHeader() {
        System.out.println();
        System.out.println("Loading embeddings from:\t" + this.fileName);
        System.out.println("#words:\t\t\t\t" + this.numberOfWords);
        System.out.println("#features/word:\t\t\t" + this.numberOfFeatures);
    }

    private void setWordAndEmbeddingsValues(String embeddingsFileLine, int lineNumber) {
        String[] wordAndEmbeddings = embeddingsFileLine.split(", ");

        if (wordAndEmbeddings.length != this.numberOfFeatures + 1) {
            System.out.println(ConsoleColour.RESET + "\n");
            throw new IllegalStateException(
                    "Line #" + (lineNumber + 1) + " in " + this.fileName + " has a different format.");
        }

        this.words[lineNumber] = wordAndEmbeddings[0];

        for (int i = 0; i < this.numberOfFeatures; i++)
            this.embeddings[lineNumber][i] = Double.parseDouble(wordAndEmbeddings[i + 1]);
    }

}
