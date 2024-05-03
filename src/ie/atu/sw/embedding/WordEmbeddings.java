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
    public String[] words;
    public double[][] embeddings;

    private static String getDefaultWordEmbeddingsFileName() {
        URL url = WordEmbeddings.class.getResource("./word-embeddings.txt");
        return url.getFile();
    }

    public WordEmbeddings() throws Exception {
        this(getDefaultWordEmbeddingsFileName());
    }

    public WordEmbeddings(String fileName) throws Exception {
        getWordsAndEmbeddingsFromFile(fileName);
    }

    private void getWordsAndEmbeddingsFromFile(String fileName) throws Exception {
        long numberOfWords = countFileLines(fileName);
        initWordsArray(numberOfWords, fileName);

        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            readBufferLinesData(buffer, (int) numberOfWords, fileName);
            buffer.close();
        } catch (Exception e) {
            throw e;
        }
    }

    private void initWordsArray(long numberOfWords, String fileName) {
        if (numberOfWords > Integer.MAX_VALUE - 1)
            throw new ArrayIndexOutOfBoundsException(
                    "There are too many words (" + numberOfWords + ") in the provided word-embeddings file: "
                            + fileName);

        int numberOfWordsInt = (int) numberOfWords;
        words = new String[numberOfWordsInt];
    }

    private void readBufferLinesData(BufferedReader buffer, int numberOfWords, String fileName) throws IOException {
        String embeddingsFileLine = buffer.readLine();

        int numberOfFeatures = countEmbeddingsFeatures(embeddingsFileLine);
        embeddings = new double[numberOfWords][numberOfFeatures];

        printFileLoadingHeader(fileName, numberOfWords, numberOfFeatures);

        for (int i = 0; i < numberOfWords; i++) {
            setWordAndEmbeddingsValues(embeddingsFileLine, i, numberOfFeatures, fileName);
            ConsoleInputOutput.printProgress(i, numberOfWords - 1);
            embeddingsFileLine = buffer.readLine();
        }
    }

    private void printFileLoadingHeader(String fileName, int numberOfWords, int numberOfFeatures) {
        System.out.println();
        System.out.println("Loading embeddings from:\t" + fileName);
        System.out.println("#words:\t\t\t\t" + numberOfWords);
        System.out.println("#features/word:\t\t\t" + numberOfFeatures);
    }

    private void setWordAndEmbeddingsValues(String embeddingsFileLine, int lineNumber, int numberOfFeatures,
            String fileName) {
        String[] wordAndEmbeddings = embeddingsFileLine.split(", ");

        if (wordAndEmbeddings.length != numberOfFeatures + 1) {
            System.out.println(ConsoleColour.RESET + "\n");
            throw new IllegalStateException(
                    "Line #" + (lineNumber + 1) + " in " + fileName + " has a different format.");
        }

        words[lineNumber] = wordAndEmbeddings[0];

        for (int i = 0; i < numberOfFeatures; i++)
            embeddings[lineNumber][i] = Double.parseDouble(wordAndEmbeddings[i + 1]);
    }

    private long countFileLines(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            return stream.count();
        }
    }

    private int countEmbeddingsFeatures(String embeddingsLine) {
        String[] stringArray = embeddingsLine.split(", ");
        int stringLength = stringArray.length;
        if (stringLength < 2)
            throw new IllegalStateException("The file provided is in the wrong format.");
        return stringLength - 1;
    }
}
