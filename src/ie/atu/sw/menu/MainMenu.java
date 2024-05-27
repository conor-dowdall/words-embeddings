package ie.atu.sw.menu;

import java.nio.file.NoSuchFileException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

/**
 * <p>
 * handle user interactions and application launches
 * </p>
 * <p>
 * when launching an application, prompt for a words-embeddings file, if not
 * loaded
 * </p>
 * <ul>
 * <li>[1] Launch 'Find Similar Words'</li>
 * <li>[2] Launch 'Find Dissimilar Words'</li>
 * <li>[3] Launch 'Word Calculator'</li>
 * <li>[4] Settings</li>
 * <li>[q] Quit</li>
 * </ul>
 */
public class MainMenu {
    private Scanner inputScanner = new Scanner(System.in);
    private SettingsMenu settingsMenu = new SettingsMenu(this.inputScanner);

    /** display options to user and process the user's choice */
    public void launchMenu() {
        MainMenuItem.printTitle();
        MainMenuItem.printOptions();

        processInput();
    }

    /**
     * accept user input from terminal, process it, and launch appropriate
     * action/application; handle all errors in the method
     */
    private void processInput() {
        try {

            String input = this.inputScanner.nextLine();
            MainMenuItem item = MainMenuItem.valueOfKey(input);

            switch (item) {
                case SIMILAR_WORDS -> launchSimilarWords();
                case DISSIMILAR_WORDS -> launchDissimilarWords();
                case WORD_CALCULATOR -> launchWordCalculator();
                case SETTINGS -> launchSettingsMenu();
                case QUIT -> quitProgram();
                default -> throw new Exception("This should never happen!");
            }

        } catch (NoSuchFileException e) {
            ConsolePrint.printError("File not found: '" + e.getMessage() + "'");
        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            launchMenu();
        }

    }

    /**
     * Launch 'Find Similar Words'
     * 
     * @throws Exception
     */
    private void launchSimilarWords() throws Exception {
        launchSimilarWords(true);
    }

    /**
     * Launch 'Find Dissimilar Words'
     * 
     * @throws Exception
     */
    private void launchDissimilarWords() throws Exception {
        launchSimilarWords(false);
    }

    /**
     * Find similar-or-dissimilar words to a word, or words, entered in the
     * terminal. Various customizations are available in the settings menu.
     * 
     * @param similar - find similar, or dissimilar words?
     * @throws Exception
     */
    private void launchSimilarWords(boolean similar) throws Exception {
        settingsMenu.initWordsEmbeddings();

        if (similar)
            ConsolePrint.printTitle("Find Similar Words");
        else
            ConsolePrint.printTitle("Find Dissimilar Words");

        settingsMenu.printSettings();

        System.out.print("Enter word(s): ");

        String input = this.inputScanner.nextLine();
        String sanitizedInput = input.replaceAll("[\s]+", " ").toLowerCase();
        ConsolePrint.printInfo("Interpreting input as: '" + sanitizedInput + "'");
        String[] words = sanitizedInput.split(" ");

        // write the first word using the current setting for appending or overwriting
        // the data output file
        try {

            settingsMenu.getWordsEmbeddings().getSimilarWords(
                    words[0],
                    settingsMenu.getNumberOfSimilaritiesToFind(),
                    similar);

            settingsMenu.printDataOutput(words[0], similar);

        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        }

        // temporarily use append mode
        // write the rest of the word-search results, if they exist
        // then set it back to user value
        // so multiple words can be written to data output file from one input
        // without overwriting previous words (from same input)
        boolean appendData = settingsMenu.getAppendDataOutputFile();
        settingsMenu.setAppendDataOutputFile(true);
        for (int i = 1; i < words.length; i++) {
            try {

                settingsMenu.getWordsEmbeddings().getSimilarWords(
                        words[i],
                        settingsMenu.getNumberOfSimilaritiesToFind(),
                        similar);

                settingsMenu.printDataOutput(words[i], similar);

            } catch (Exception e) {
                ConsolePrint.printError(e.getMessage());
            }
        }
        settingsMenu.setAppendDataOutputFile(appendData);
    }

    /**
     * Launch 'Word Calculator'
     * 
     * @throws Exception
     */
    private void launchWordCalculator() throws Exception {
        new WordCalculatorMenu(this.inputScanner, this.settingsMenu).launchMenu();
    }

    /**
     * Display Settings Menu
     * 
     * @throws Exception
     */
    private void launchSettingsMenu() throws Exception {
        settingsMenu.launchMenu();
    }

    /**
     * Quit program
     */
    private void quitProgram() {
        this.inputScanner.close();
        ConsolePrint.printInfo("Quit");
        System.out.println();
        System.exit(0);
    }
}
