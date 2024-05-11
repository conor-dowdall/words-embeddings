package ie.atu.sw.menu;

import java.io.File;
import java.util.Scanner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.embeddings.WordsEmbeddings;

public class SettingsMenu {

    private Scanner inputScanner;
    private Preferences preferences;
    private boolean keepSettingsOpen;
    private WordsEmbeddings wordsEmbeddings;

    public SettingsMenu(Scanner inputScanner) {
        this.inputScanner = inputScanner;
        this.preferences = Preferences.userNodeForPackage(SettingsMenu.class);
    }

    public void launchMenu() throws Exception {
        SettingsMenuItem.printTitle();
        keepSettingsOpen = true;
        printOptionsAndProcessInput();
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

    public boolean getAppendDataOutputFile() {
        return this.preferences.getBoolean("appendDataOutputFile", true);
    }

    private void setAppendDataOutputFile(boolean append) {
        this.preferences.putBoolean("appendDataOutputFile", append);
    }

    private void printOptionsAndProcessInput() throws Exception {

        try {

            SettingsMenuItem.printOptions();

            String input = this.inputScanner.nextLine();
            SettingsMenuItem item = SettingsMenuItem.valueOfKey(input);

            switch (item) {
                case EMBEDDINGS_FILE -> loadNewWordsEmbeddingsFile();
                case SIMILARITIES_NUMBER -> specifySimilarities();
                case SIMILARITIES_ALGORITHM -> System.out.println("TODO");
                case TOGGLE_SIMILARITY_SCORE -> toggleAddSimilarityScore();
                case OUTPUT_FILE -> specifyNewDataOutputFileName();
                case TOGGLE_APPEND -> toggleAppendDataOutputFile();
                case EMPTY_OUTPUT_FILE -> emptyDataOutputFile();
                case RESET -> resetSettings();
                case QUIT -> quitSettings();
                default -> throw new Exception("This should never happen!");
            }

        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            if (keepSettingsOpen)
                launchMenu();
        }

    }

    public void loadNewWordsEmbeddingsFile() throws Exception {
        ConsolePrint.printHeading("Load Words-Embeddings File");

        String wordsEmbeddingsFileName = scanFileName(getWordsEmbeddingsFileName());

        this.wordsEmbeddings = new WordsEmbeddings(wordsEmbeddingsFileName);

        setWordsEmbeddingsFileName(wordsEmbeddingsFileName);

        ConsolePrint.printInfo("Words-Embeddings file loaded: " + wordsEmbeddingsFileName);
    }

    private void specifySimilarities() throws Exception {
        ConsolePrint.printHeading("Specify Number of Similarities to Find");

        int similarities = scanSimilarities();

        setNumberOfSimilaritiesToFind(similarities);

        ConsolePrint.printInfo("Number of Similarities To Find is set to: " + similarities);
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

    private void resetSettings() throws BackingStoreException {
        this.preferences.clear();

        ConsolePrint.printHeading("Settings reset to default values");
        printSettings();
    }

    public void printSettings() {

        String wordsEmbeddingsFileName = this.wordsEmbeddings == null ? "NOT SET" : this.wordsEmbeddings.getFileName();
        String appendOverwrite = getAppendDataOutputFile() ? "append" : "overwrite";

        ConsolePrint.printInfo("Embeddings File: " + wordsEmbeddingsFileName);
        ConsolePrint.printInfo("Number of Words to Find: " + getNumberOfSimilaritiesToFind());
        ConsolePrint.printInfo("Data-Output File: " + getDataOutputFileName());
        ConsolePrint.printInfo("Append/Overwrite Mode: " + appendOverwrite);
        ConsolePrint.printInfo("Include Similarity Score: " + getAddSimilarityScore());
        System.out.println();
    }

    private void quitSettings() {
        keepSettingsOpen = false;

        ConsolePrint.printInfo("Close Settings Menu");
    }

}
