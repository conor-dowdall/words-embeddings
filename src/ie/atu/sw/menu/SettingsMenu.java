package ie.atu.sw.menu;

import java.util.Scanner;
import java.util.prefs.Preferences;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.embeddings.WordsEmbeddings;

public class SettingsMenu {

    private boolean keepSettingsOpen = true;
    private Scanner inputScanner;
    private WordsEmbeddings wordsEmbeddings;

    private Preferences preferences;

    public SettingsMenu(Scanner inputScanner) {
        this.inputScanner = inputScanner;
        this.preferences = Preferences.userNodeForPackage(SettingsMenu.class);
    }

    public WordsEmbeddings getWordsEmbeddings() {
        return this.wordsEmbeddings;
    }

    public int getNumberOfSimilaritiesToFind() {
        return this.preferences.getInt("numberOfSimilaritiesToFind", 10);
    }

    public void launchMenu() {
        SettingsMenuItem.printTitle();
        printOptionsAndProcessInput();
    }

    private void printOptionsAndProcessInput() {

        try {

            SettingsMenuItem.printOptions();

            String input = this.inputScanner.nextLine();
            SettingsMenuItem item = SettingsMenuItem.valueOfKey(input);

            switch (item) {
                case EMBEDDINGS_FILE -> loadNewWordEmbeddings();
                case OUTPUT_FILE -> specifyNewOutputFile();
                case CLEAR_OUTPUT_FILE -> System.out.println("TODO");
                case SIMILARITIES_NUMBER -> preferences.putInt("numberOfSimilaritiesToFind", 2);
                case SIMILARITIES_ALGORITHM -> System.out.println("TODO");
                case ADD_WORD_STATS -> System.out.println("TODO");
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

    public void loadNewWordEmbeddings() throws Exception {
        ConsolePrint.printHeading("Load Word-Embeddings File");

        String wordsEmbeddingsFileName = scanFileName();

        this.wordsEmbeddings = new WordsEmbeddings(wordsEmbeddingsFileName);
    }

    private void specifyNewOutputFile() throws Exception {
        ConsolePrint.printHeading("Specify Output-Data File");

        String outputFileName = scanFileName();

        System.out.println("You entered: " + outputFileName);
    }

    private String scanFileName() throws Exception {
        System.out.print("Enter file name: ");
        String fileName = this.inputScanner.nextLine();

        if (fileName.equals(""))
            throw new Exception("No file name provided");

        return fileName;
    }

    private void quitSettings() {
        keepSettingsOpen = false;
    }

}
