package ie.atu.sw.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.prefs.Preferences;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.embeddings.WordsEmbeddings;
import ie.atu.sw.util.SimilarityAlgorithm;

public class SettingsMenu {

    private Scanner inputScanner;
    private Preferences preferences;
    private boolean keepSettingsOpen;
    private WordsEmbeddings wordsEmbeddings;

    public SettingsMenu(Scanner inputScanner) {
        this.inputScanner = inputScanner;
        this.preferences = Preferences.userNodeForPackage(SettingsMenu.class);
    }

    public WordsEmbeddings getWordsEmbeddings() {
        return this.wordsEmbeddings;
    }

    private String getWordsEmbeddingsFileName() {
        return this.preferences.get("wordsEmbeddingsFileName", "./word-embeddings.txt");
    }

    private void setWordsEmbeddingsFileName(String fileName) {
        this.preferences.put("wordsEmbeddingsFileName", fileName);
    }

    public int getNumberOfSimilaritiesToFind() {
        return this.preferences.getInt("numberOfSimilaritiesToFind", 10);
    }

    private void setNumberOfSimilaritiesToFind(int number) {
        this.preferences.putInt("numberOfSimilaritiesToFind", number);
    }

    public SimilarityAlgorithm getSimilarityAlgorithm() throws Exception {
        String algorithmNumber = this.preferences.get("similarityAlgorithmNumber", "4");
        return SimilarityAlgorithmMenuItem.valueOfKey(algorithmNumber);
    }

    private void setSimilarityAlgorithm(SimilarityAlgorithm algorithm) {
        String algorithmNumber = Integer.toString(algorithm.ordinal() + 1);
        this.preferences.put("similarityAlgorithmNumber", algorithmNumber);
    }

    public boolean getAddSimilarityScore() {
        return this.preferences.getBoolean("addSimilarityScore", false);
    }

    private void setAddSimilarityScore(boolean append) {
        this.preferences.putBoolean("addSimilarityScore", append);
    }

    public String getDataOutputFileName() {
        return this.preferences.get("dataOutputFileName", "./out.txt");
    }

    private void setDataOutputFileName(String fileName) {
        this.preferences.put("dataOutputFileName", fileName);
    }

    public BufferedWriter getDataOutputBufferedWriter() throws IOException {
        File dataOutputFile = new File(getDataOutputFileName());
        FileWriter dataOutputFileWriter = new FileWriter(dataOutputFile, getAppendDataOutputFile());
        BufferedWriter dataOutputBufferedWriter = new BufferedWriter(dataOutputFileWriter);
        return dataOutputBufferedWriter;
    }

    public boolean getAppendDataOutputFile() {
        return this.preferences.getBoolean("appendDataOutputFile", true);
    }

    private void setAppendDataOutputFile(boolean append) {
        this.preferences.putBoolean("appendDataOutputFile", append);
    }

    public String getSettingsAsHeading(String dataInput, boolean similar) throws Exception {
        String heading = getNumberOfSimilaritiesToFind() + " ";
        heading += getAddSimilarityScore()
                ? "Scores/"
                : "";
        heading += similar
                ? "Words Similar to '"
                : "Words Dissimilar to '";
        heading += dataInput + "'";
        heading += " using " + getSimilarityAlgorithm() + ":";

        return heading;
    }

    public void printDataOutput(String dataInput, boolean similar) throws Exception {
        BufferedWriter dataOutputBufferedWriter = getDataOutputBufferedWriter();

        String heading = getSettingsAsHeading(dataInput, similar);

        ConsolePrint.printHeading(heading);
        dataOutputBufferedWriter.write(heading);
        dataOutputBufferedWriter.newLine();

        for (int i = 0; i < getWordsEmbeddings().getPreviousSimilarWords().length; i++) {
            if (getAddSimilarityScore()) {
                double score = getWordsEmbeddings().getPreviousSimilarWordsScores()[i];
                String formattedScore = String.format("%24.18f", score);
                System.out.print(formattedScore + " ");
                dataOutputBufferedWriter.write(formattedScore + " ");
            }

            System.out.print(getWordsEmbeddings().getPreviousSimilarWords()[i]);
            dataOutputBufferedWriter.write(getWordsEmbeddings().getPreviousSimilarWords()[i]);

            System.out.println();
            dataOutputBufferedWriter.newLine();
        }

        dataOutputBufferedWriter.newLine();
        dataOutputBufferedWriter.close();
    }

    public void launchMenu() throws Exception {
        keepSettingsOpen = true;

        SettingsMenuItem.printTitle();
        SettingsMenuItem.printOptions();

        processInput();
    }

    private void processInput() throws Exception {

        try {

            String input = this.inputScanner.nextLine();
            SettingsMenuItem item = SettingsMenuItem.valueOfKey(input);

            switch (item) {
                case EMBEDDINGS_FILE -> loadNewWordsEmbeddingsFile();
                case SIMILARITIES_NUMBER -> specifySimilaritiesNumber();
                case SIMILARITIES_ALGORITHM -> specifySimilarityAlgorithm();
                case TOGGLE_SIMILARITY_SCORE -> toggleAddSimilarityScore();
                case OUTPUT_FILE -> specifyNewDataOutputFileName();
                case TOGGLE_APPEND -> toggleAppendDataOutputFile();
                case EMPTY_OUTPUT_FILE -> emptyDataOutputFile();
                case RESET -> resetSettings();
                case PRINT -> printSettings();
                case QUIT -> quitSettings();
                default -> throw new Exception("This should never happen!");
            }

        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            if (this.keepSettingsOpen)
                launchMenu();
        }

    }

    public void initWordsEmbeddings() throws Exception {
        if (this.wordsEmbeddings == null)
            loadNewWordsEmbeddingsFile();

        this.wordsEmbeddings.setSimilarityAlgorithm(getSimilarityAlgorithm());
    }

    public void loadNewWordsEmbeddingsFile() throws Exception {
        ConsolePrint.printHeading("Load Words-Embeddings File");

        String wordsEmbeddingsFileName = scanFileName(getWordsEmbeddingsFileName());

        this.wordsEmbeddings = new WordsEmbeddings(wordsEmbeddingsFileName);

        setWordsEmbeddingsFileName(wordsEmbeddingsFileName);

        ConsolePrint.printInfo("Words-Embeddings file loaded: " + wordsEmbeddingsFileName);
    }

    private void specifySimilaritiesNumber() throws Exception {
        ConsolePrint.printHeading("Specify Number of Similarities to Find");

        int similarities = scanSimilarities();

        setNumberOfSimilaritiesToFind(similarities);

        ConsolePrint.printInfo("Number of Similarities To Find is set to: " + similarities);
    }

    private void specifySimilarityAlgorithm() throws Exception {
        SimilarityAlgorithmMenuItem.printTitle();
        SimilarityAlgorithmMenuItem.printOptions();

        String input = this.inputScanner.nextLine();

        SimilarityAlgorithm algorithm = SimilarityAlgorithmMenuItem.valueOfKey(input);

        setSimilarityAlgorithm(algorithm);

        ConsolePrint.printInfo("Similarity Algorithm set to: " + algorithm);
    }

    private String scanFileName(String defaultFileName) {
        ConsolePrint.printInfo("hit ENTER for default: " + defaultFileName);
        System.out.print("Enter file name: ");
        String input = this.inputScanner.nextLine();

        if (input.equals(""))
            input = defaultFileName;

        return input;
    }

    private int scanSimilarities() throws Exception {
        try {

            System.out.print("Enter an integer (greater than 0): ");
            String input = this.inputScanner.nextLine();

            int number = Integer.parseInt(input);

            if (number < 1)
                throw new Exception("Number must be greater than 0");

            if (wordsEmbeddings != null) {
                if (number > this.wordsEmbeddings.getNumberOfWords())
                    throw new Exception(
                            "Number to big. Maximum words available = " + this.wordsEmbeddings.getNumberOfWords());
            } else {
                ConsolePrint
                        .printWarning("Make sure to load a words-embeddings file with at least " + number + " word(s)");
            }

            return number;

        } catch (NumberFormatException e) {
            throw new Exception("Invalid number. Using previous value: " + getNumberOfSimilaritiesToFind());
        }

    }

    private void toggleAddSimilarityScore() {
        setAddSimilarityScore(!getAddSimilarityScore());

        ConsolePrint.printInfo("Similarity algorithm scores will be used in output data");
    }

    private void specifyNewDataOutputFileName() {
        ConsolePrint.printHeading("Specify Data-Output File");

        String dataOutputFileName = scanFileName(getDataOutputFileName());

        setDataOutputFileName(dataOutputFileName);

        if (getAppendDataOutputFile())
            ConsolePrint.printInfo("Data output will now be appended to " + getDataOutputFileName());
        else
            ConsolePrint.printInfo("Data output will now overwrite " + getDataOutputFileName());
    }

    private void toggleAppendDataOutputFile() {
        setAppendDataOutputFile(!getAppendDataOutputFile());

        if (getAppendDataOutputFile())
            ConsolePrint.printInfo("Data output will now be appended to " + getDataOutputFileName());
        else
            ConsolePrint.printInfo("Data output will now overwrite " + getDataOutputFileName());
    }

    private void emptyDataOutputFile() throws Exception {
        try {

            File dataOutputFile = new File(getDataOutputFileName());

            if (dataOutputFile.delete())
                dataOutputFile.createNewFile();

        } catch (Exception e) {
            throw new Exception("Problem emptying data-output file: " + getDataOutputFileName());
        }

        ConsolePrint.printInfo("Data-Output file is now empty: " + getDataOutputFileName());
    }

    private void resetSettings() throws Exception {
        this.preferences.clear();

        ConsolePrint.printHeading("Settings reset to default values");
        printSettings();
    }

    public void printSettings() throws Exception {

        String wordsEmbeddingsFileName = this.wordsEmbeddings == null ? "NOT SET" : this.wordsEmbeddings.getFileName();
        String appendOverwrite = getAppendDataOutputFile() ? "append" : "overwrite";

        ConsolePrint.printInfo("Embeddings File: " + wordsEmbeddingsFileName);
        ConsolePrint.printInfo("Number of Words to Find: " + getNumberOfSimilaritiesToFind());
        ConsolePrint.printInfo("Data-Output File: " + getDataOutputFileName());
        ConsolePrint.printInfo("Append/Overwrite Mode: " + appendOverwrite);
        ConsolePrint.printInfo("Include Similarity Score: " + getAddSimilarityScore());
        ConsolePrint.printInfo("Similarity Algorithm: " + getSimilarityAlgorithm());
        System.out.println();
    }

    private void quitSettings() {
        this.keepSettingsOpen = false;

        ConsolePrint.printInfo("Close Settings Menu");
    }

}
