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

/**
 * class to store user preferences with persistent values (using
 * java.util.prefs.Preferences); default settings are included
 */
public class SettingsMenu {

    private Scanner inputScanner;
    private Preferences preferences = Preferences.userNodeForPackage(SettingsMenu.class);
    private boolean keepSettingsOpen;
    private WordsEmbeddings wordsEmbeddings;

    /**
     * create a new settings menu instance and set up the input scanner
     * 
     * @param inputScanner - pass in a scanner to read input from the terminal
     */
    public SettingsMenu(Scanner inputScanner) {
        this.inputScanner = inputScanner;
    }

    /**
     * get the WordsEmbeddings instance used in this class
     * 
     * @return the instance of WordsEmbeddings used in this class
     */
    public WordsEmbeddings getWordsEmbeddings() {
        return this.wordsEmbeddings;
    }

    /**
     * get the stored file name used when the words-embeddings file was loaded
     * 
     * @return - the words-embeddings file name
     */
    private String getWordsEmbeddingsFileName() {
        return this.preferences.get("wordsEmbeddingsFileName", "./word-embeddings.txt");
    }

    /**
     * set and store the words-embeddings file name
     * 
     * @param fileName - the words-embeddings file name
     */
    private void setWordsEmbeddingsFileName(String fileName) {
        this.preferences.put("wordsEmbeddingsFileName", fileName);
    }

    /**
     * get the stored number of similarities to find (defaults to 10)
     * 
     * @return the stored number of similarities to find (defaults to 10)
     */
    public int getNumberOfSimilaritiesToFind() {
        return this.preferences.getInt("numberOfSimilaritiesToFind", 10);
    }

    /**
     * set and store the number of similarities to find
     * 
     * @param number the number of similarities to find
     */
    private void setNumberOfSimilaritiesToFind(int number) {
        this.preferences.putInt("numberOfSimilaritiesToFind", number);
    }

    /**
     * get the stored similarity algorithm to use in word searches (defaults to
     * cosine similarity, which is represented with the shortcut '4' in
     * SimilarityAlgorithmMenuItem)
     * 
     * @return the current similarity algorithm to use in word searches
     * @throws Exception
     */
    public SimilarityAlgorithm getSimilarityAlgorithm() throws Exception {
        String algorithmNumber = this.preferences.get("similarityAlgorithmNumber", "4");
        return SimilarityAlgorithmMenuItem.valueOfKey(algorithmNumber);
    }

    /**
     * set and store the similarity algorithm to use in word searches (stored as an
     * integer, which corresponds to a shortcut in SimilarityAlgorithmMenuItem)
     * 
     * @param algorithm - the similarity algorithm to use in word searches
     */
    private void setSimilarityAlgorithm(SimilarityAlgorithm algorithm) {
        String algorithmNumber = Integer.toString(algorithm.ordinal() + 1);
        this.preferences.put("similarityAlgorithmNumber", algorithmNumber);
    }

    /**
     * get the boolean representing whether the similarity score should be used in
     * the data output (defaults to false)
     * 
     * @return whether the similarity score should be used in the data output
     */
    public boolean getAddSimilarityScore() {
        return this.preferences.getBoolean("addSimilarityScore", false);
    }

    /**
     * set and store whether the similarity score should be used in the data output
     * 
     * @param append - whether the similarity score should be used in the data
     *               output
     */
    private void setAddSimilarityScore(boolean append) {
        this.preferences.putBoolean("addSimilarityScore", append);
    }

    /**
     * get the currently set data-output file name (defaults to ./out.txt)
     * 
     * @return the currently set data-output file name (defaults to ./out.txt)
     */
    public String getDataOutputFileName() {
        return this.preferences.get("dataOutputFileName", "./out.txt");
    }

    /**
     * set the data-output file name to use
     * 
     * @param fileName - the data-output file name to use
     */
    private void setDataOutputFileName(String fileName) {
        this.preferences.put("dataOutputFileName", fileName);
    }

    /**
     * get a BufferedWriter pointing to the currently set data-output file name
     * 
     * @return a BufferedWriter pointing to the currently set data-output file name
     * @throws IOException
     */
    public BufferedWriter getDataOutputBufferedWriter() throws IOException {
        File dataOutputFile = new File(getDataOutputFileName());
        FileWriter dataOutputFileWriter = new FileWriter(dataOutputFile, getAppendDataOutputFile());
        BufferedWriter dataOutputBufferedWriter = new BufferedWriter(dataOutputFileWriter);
        return dataOutputBufferedWriter;
    }

    /**
     * get a boolean representing whether data should be appended to (true) or
     * should overwrite (false) the data-output file (defaults to true)
     * 
     * @return whether data should be appended to (true) or should overwrite (false)
     *         the data-output file
     */
    public boolean getAppendDataOutputFile() {
        return this.preferences.getBoolean("appendDataOutputFile", true);
    }

    /**
     * set whether data should be appended to (true) or should overwrite (false) the
     * data-output file
     * 
     * @param append - whether data should be appended to (true) or should overwrite
     *               (false) the data-output file
     */
    private void setAppendDataOutputFile(boolean append) {
        this.preferences.putBoolean("appendDataOutputFile", append);
    }

    /**
     * format the search parameter used for display as a heading, along with other
     * relevant settings
     * 
     * @param dataHeadingText - text representing the search value(s) used, to
     *                        appear in the heading, along with other heading info
     * @param similar         - were similarities or dissimilarities found?
     * @return the formatted heading text
     * @throws Exception
     */
    public String getSettingsAsHeading(String dataHeadingText, boolean similar) throws Exception {
        String heading = getNumberOfSimilaritiesToFind() + " ";
        heading += getAddSimilarityScore()
                ? "Scores/"
                : "";
        heading += similar
                ? "Words Similar to '"
                : "Words Dissimilar to '";
        heading += dataHeadingText + "'";
        heading += " using " + getSimilarityAlgorithm() + ":";

        return heading;
    }

    /**
     * neatly print the most recently searched-for WordsEmbeddings matches to the
     * stored data-output file
     * 
     * @param dataHeadingText - text representing the search value(s) used, to
     *                        appear in the heading, along with other heading info
     * @param similar         - were similarities or dissimilarities found?
     * @throws Exception
     */
    public void printDataOutput(String dataHeadingText, boolean similar) throws Exception {
        BufferedWriter dataOutputBufferedWriter = getDataOutputBufferedWriter();

        String heading = getSettingsAsHeading(dataHeadingText, similar);

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

    /** print the menu title, offer options to user, and process user input */
    public void launchMenu() throws Exception {
        keepSettingsOpen = true;

        SettingsMenuItem.printTitle();
        SettingsMenuItem.printOptions();

        processInput();
    }

    /**
     * read the user input, and take appropriate action
     * 
     * @throws Exception
     */
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

    /**
     * check if a words-embeddings file was loaded and, if not, offer to load one;
     * also apply the currently set similarity algorithm to the current
     * WordsEmbeddings class
     * 
     * @throws Exception
     */
    public void initWordsEmbeddings() throws Exception {
        if (this.wordsEmbeddings == null)
            loadNewWordsEmbeddingsFile();

        this.wordsEmbeddings.setSimilarityAlgorithm(getSimilarityAlgorithm());
    }

    /**
     * load a new words-embeddings file from user text-input in the terminal
     * 
     * @throws Exception
     */
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

        if (getAddSimilarityScore())
            ConsolePrint.printInfo("Similarity algorithm scores will be used in output data");
        else
            ConsolePrint.printInfo("Similarity algorithm scores will NOT be used in output data");
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
